package ru.zako.questionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.user.UserDto

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        enableEdgeToEdge()

        val emailI: EditText = findViewById(R.id.loginField)
        val password: EditText = findViewById(R.id.passwordField)
        val retryPassword: EditText = findViewById(R.id.retryPasswordField)

        val registerButton: Button = findViewById(R.id.registerButtonSubmit)

        registerButton.setOnClickListener {
            if (!isValidEmail(emailI.text.toString())) {
                Toast.makeText(this, "Неверный формат почты", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.text.length < 6) {
                Toast.makeText(this, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.text.toString() != retryPassword.text.toString()) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            registerUser(emailI.text.toString(), password.text.toString())
        }


    }

    private fun registerUser(username: String, password: String) {
        val userDto = UserDto(username, password)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.register(userDto)
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@RegisterActivity, "Регистрация успешна!", Toast.LENGTH_LONG).show()
                    back()
                } else if (response.code() == 400) {
                    Toast.makeText(this@RegisterActivity, "Аккаунт с этим ящиком уже существует", Toast.LENGTH_LONG).show()
                } else if (response.code() == 500) {
                    Toast.makeText(this@RegisterActivity, "Ошибка на стороне сервера", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@RegisterActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun back() {
        val intent = Intent(this, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailRegex.toRegex())
    }
}