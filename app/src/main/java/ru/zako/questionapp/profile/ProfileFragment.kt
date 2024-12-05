package ru.zako.questionapp.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.zako.questionapp.MainActivity
import ru.zako.questionapp.R
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.TokenManager

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lifecycleScope.launch {
            try {
                val profile = RetrofitClient.profileApi.getProfile()
                if (profile.code() == 403) {
                    TokenManager.clearToken()
                    Toast.makeText(requireContext(), "Токен недействителен", Toast.LENGTH_SHORT).show()
                    (requireActivity() as MainActivity).navigateToLogin()
                }
                else if (profile.code() != 200) {
                    Toast.makeText(requireContext(), "Ошибка получения профиля: ${profile.code()}", Toast.LENGTH_SHORT).show()
                }
                if (profile.isSuccessful && profile.body()?.data != null) {
                    profile.body()?.data?.let { data ->
                        ProfileManager.set(data)
                        view?.let { updateMainMenu(it) }
                    }
                } else {
                    val errorMessage = when (profile.code()) {
                        403 -> "Доступ запрещен. Возможно, истек токен."
                        404 -> "Профиль не найден."
                        else -> "Неизвестная ошибка: ${profile.code()}"
                    }

                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                println("Ошибка $e")
            }
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.logout_button)

        updateMainMenu(view)

        button.setOnClickListener {
            lifecycleScope.launch {
                TokenManager.clearToken()
                (requireActivity() as MainActivity).navigateToLogin()
            }
        }
    }

    private fun updateMainMenu(view: View) {
        val profileMail: TextView = view.findViewById(R.id.profile_email)

        profileMail.text = ProfileManager.email

        val idText = view.findViewById<TextView>(R.id.profile_name)
        idText.text = ProfileManager.id.toString()
    }
}