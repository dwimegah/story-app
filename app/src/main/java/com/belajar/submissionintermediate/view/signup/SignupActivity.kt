package com.belajar.submissionintermediate.view.signup

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.belajar.submissionintermediate.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel>()

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                binding.nameEditText.error = "Fill the name"
                binding.emailEditText.error = "Fill the email"
                binding.passwordEditText.error = "Fill the password"
            } else {
                viewModel.register(name, email, password)
                viewModel.signup.observe(this) { data ->
                    binding.loading.visibility = View.GONE
                    if (data.error != true) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Success!")
                            setMessage("Your account successfully registered.")
                            setPositiveButton("Continue") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
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