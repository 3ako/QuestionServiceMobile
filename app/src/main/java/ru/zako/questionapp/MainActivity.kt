package ru.zako.questionapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.zako.questionapp.api.TokenManager
import ru.zako.questionapp.profile.ProfileFragment
import ru.zako.questionapp.test.TestListFragment

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        val token = TokenManager.getToken()

        if (token.isNullOrEmpty()) {
            navigateToLogin()
            return
        }

        // Если авторизован, продолжаем
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        setupInsets()
        setupBottomNavigation()
    }

    // Настройка отступов для Edge-to-Edge
//    private fun setupInsets() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }

    // Настройка нижнего меню
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.testlist -> {
                    replaceFragment(TestListFragment())
                    true
                }
                else -> false
            }
        }

        // Отображаем фрагмент по умолчанию
        if (supportFragmentManager.fragments.isEmpty()) {
            replaceFragment(ProfileFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Навигация на экран логина
    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}