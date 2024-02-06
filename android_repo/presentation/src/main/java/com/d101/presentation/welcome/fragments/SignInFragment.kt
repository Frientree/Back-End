package com.d101.presentation.welcome.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d101.domain.model.Result
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSignInBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.music.BackgroundMusicService
import com.d101.presentation.music.BackgroundMusicService.Companion.PLAY
import com.d101.presentation.welcome.event.SignInViewEvent
import com.d101.presentation.welcome.viewmodel.SignInViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.suspendCancellableCoroutine
import utils.repeatOnStarted
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectEvent()
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    SignInViewEvent.FindPasswordClicked -> navigateToFindPassword()
                    SignInViewEvent.SignInAttemptByFrientree -> viewModel.signInByFrientree()
                    SignInViewEvent.SignInAttemptByNaver -> viewModel.onNaverSignInCompleted(
                        authenticateWithNaver(requireContext()),
                    )

                    is SignInViewEvent.SignInFailed -> showToast(event.message)
                    SignInViewEvent.SignInSuccess -> navigateToMainScreen()
                    SignInViewEvent.SignUpClicked -> navigateToTermsAgree()
                }
            }
        }
    }

    private suspend fun authenticateWithNaver(context: Context): Result<String> =
        suspendCancellableCoroutine { continuation ->
            val callback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    val accessToken = NaverIdLoginSDK.getAccessToken() ?: ""
                    continuation.resume(Result.Success(accessToken))
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    continuation.resumeWithException(
                        Exception("Authentication failed: $message (HTTP $httpStatus)"),
                    )
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            NaverIdLoginSDK.authenticate(context, callback)

            continuation.invokeOnCancellation {
            }
        }

    private fun navigateToMainScreen() {
        requireActivity().startService(
            Intent(
                requireContext(),
                BackgroundMusicService::class.java
            ).apply {
                action = PLAY
            })
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToTermsAgree() =
        findNavController().navigate(R.id.action_signInFragment_to_termsAgreeFragment)

    private fun navigateToFindPassword() =
        findNavController().navigate(R.id.action_signInFragment_to_findPasswordFragment)

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
