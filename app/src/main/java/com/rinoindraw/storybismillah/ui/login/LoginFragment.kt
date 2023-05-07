package com.rinoindraw.storybismillah.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rinoindraw.storybismillah.R
import com.rinoindraw.storybismillah.databinding.FragmentLoginBinding
import com.rinoindraw.storybismillah.ui.main.MainActivity
import com.rinoindraw.storybismillah.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.rinoindraw.storybismillah.utils.ConstVal
import com.rinoindraw.storybismillah.utils.ConstVal.KEY_EMAIL
import com.rinoindraw.storybismillah.utils.SessionManager
import com.rinoindraw.storybismillah.utils.ext.gone
import com.rinoindraw.storybismillah.utils.ext.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private var loginJob: Job = Job()

    private lateinit var pref: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())

        playAnimation()
        initAction()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initAction() {
        binding.apply {
            tvToRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )

            btnLogin.setOnClickListener {
                if (edtPassword.error.isNullOrEmpty() && edtEmail.error.isNullOrEmpty()) {
                    loginUser()
                    findNavController().popBackStack(R.id.loginFragment, false)
                }else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_register_login),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome =
            ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).setDuration(500)
        val welcomeDesc =
            ObjectAnimator.ofFloat(binding.tvWelcomeDesc, View.ALPHA, 1f).setDuration(500)
        val emailTitle =
            ObjectAnimator.ofFloat(binding.tvEmailTitle, View.ALPHA, 1f).setDuration(500)
        val passwordTitle =
            ObjectAnimator.ofFloat(binding.tvPasswordTitle, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val isHavent = ObjectAnimator.ofFloat(binding.tvIsHaventAccount, View.ALPHA, 1f).setDuration(500)
        val toRegister = ObjectAnimator.ofFloat(binding.tvToRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(welcome, welcomeDesc)
        }


        AnimatorSet().apply {
            playSequentially(together, emailTitle, edtEmail, passwordTitle, edtPassword, buttonLogin, isHavent, toRegister)
            start()
        }

    }

    private fun loginUser() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString()

        showLoading(true)

        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()

            loginJob = launch {
                loginViewModel.loginUser(email, password).collect { result ->
                    result.onSuccess { credentials ->
                        credentials.loginResult?.token?.let { token ->
                            pref.saveAuthToken(token)
                            pref.setStringPreference(KEY_EMAIL, email)
                            pref.setBooleanPreference(ConstVal.KEY_IS_LOGIN, true)

                            Intent(requireContext(), MainActivity::class.java).also { intent ->
                                intent.putExtra(EXTRA_TOKEN, token)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_success_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.edtEmail.isClickable = !isLoading
        binding.edtEmail.isEnabled = !isLoading
        binding.edtPassword.isClickable = !isLoading
        binding.edtPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }
}