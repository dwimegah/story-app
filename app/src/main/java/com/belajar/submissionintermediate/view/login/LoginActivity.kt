package com.belajar.submissionintermediate.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.belajar.submissionintermediate.data.pref.UserModel
import com.belajar.submissionintermediate.databinding.ActivityLoginBinding
import com.belajar.submissionintermediate.view.ViewModelFactory
import com.belajar.submissionintermediate.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isEmpty() && password.isEmpty()) {
                binding.emailEditText.error = "Fill the email"
                binding.passwordEditText.error = "Fill the password"
            } else {
                viewModel.postLogin(email, password)
                viewModel.login.observe(this) { data ->
                    binding.loading.visibility = View.GONE
                    if (data.error != true) {
                        viewModel.saveSession(
                            UserModel(
                                data?.loginResult?.name.toString(),
                                "Bearer " + data?.loginResult?.token,
                                true
                            )
                        )

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        finish()
                    } else {
                        viewModel.errorMsg.observe(this){
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}