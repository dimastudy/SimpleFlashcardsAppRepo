package com.example.simpleflashcardsapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private val TAG = "LOGIN FRAGMENT TAG"
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private var isSignedIn = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)
        createRequest()
        auth = Firebase.auth

        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                signIn()
            }
        }

        viewModel.isSignedIn.observe(viewLifecycleOwner){
            isSignedIn = it
            requireActivity().invalidateOptionsMenu()
        }

        setHasOptionsMenu(true)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            viewModel.signedIn()
        }
        else{
            viewModel.signedOut()
        }
        updateUI(currentUser)
    }

    fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }

    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    viewModel.signedIn()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    viewModel.signedOut()
                    updateUI(null)
                }
            }
    }


    private fun updateUI(user: FirebaseUser?){
        when{
            user == null -> {
                binding.apply {
                    tvLoginMail.isVisible = false
                    tvLoginNickname.isVisible = false
                    cardImageLogin.isVisible = false
                    tvWelcomeLogin.isVisible = true
                    btnGoogleSignIn.isVisible = true
                }
            }
            else -> {
                binding.apply {
                    tvLoginMail.isVisible = true
                    tvLoginNickname.isVisible = true
                    cardImageLogin.isVisible = true
                    tvWelcomeLogin.isVisible = false
                    btnGoogleSignIn.isVisible = false

                    tvLoginNickname.text = auth.currentUser?.displayName
                    tvLoginMail.text = auth.currentUser?.email
                    Glide.with(requireContext()).load(auth.currentUser?.photoUrl).into(imageLogin)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_login, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout -> {
                Firebase.auth.signOut()
                val currentUser = auth.currentUser
                updateUI(currentUser)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val item = menu.findItem(R.id.action_logout)
        item.isVisible = isSignedIn
    }

}