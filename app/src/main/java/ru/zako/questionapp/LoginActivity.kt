package ru.zako.questionapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.TokenManager
import ru.zako.questionapp.api.auth.AuthRequest

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val loginField: EditText = findViewById(R.id.loginField)
        val passwordField: EditText = findViewById(R.id.passwordField)
        val submitButton: Button = findViewById(R.id.loginButton)
        submitButton.setOnClickListener {
            loginUser(loginField.text.toString(), passwordField.text.toString())
        }

        val registerLink: TextView = findViewById(R.id.registerButton)
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loginUser(username: String, password: String) {
        val authRequest = AuthRequest(username, password)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.login(authRequest)
                if (response.isSuccessful && response.body()?.success == true) {
                    val token = response.body()?.data
                    Toast.makeText(this@LoginActivity, "Авторизация успешна! Токен: $token", Toast.LENGTH_LONG).show()

                    val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("auth_token", token).apply()
                    if (token != null) {
                        TokenManager.setToken(token)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Ошибка: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}