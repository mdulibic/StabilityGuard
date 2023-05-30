package com.example.stabilityGuard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.stabilityGuard.R
import com.example.stabilityGuard.databinding.FragmentLoginBinding
import com.example.stabilityGuard.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.checkUserStatus()

        setOnClickListener()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            svm.navigate(LoginFragmentDirections.actionLoginFragmentToNavigationHome())
        }
    }

    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.loginUser(
                username = binding.etUsername.text.toString(),
                password = binding.etPassword.text.toString(),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
