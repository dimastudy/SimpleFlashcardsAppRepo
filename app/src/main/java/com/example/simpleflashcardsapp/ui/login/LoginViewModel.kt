package com.example.simpleflashcardsapp.ui.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleflashcardsapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    fun signedIn(){
        _isSignedIn.value = true
    }
    fun signedOut(){
        _isSignedIn.value = false
    }

}