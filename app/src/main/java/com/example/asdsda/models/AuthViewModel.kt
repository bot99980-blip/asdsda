package com.example.asdsda.models

import android.os.Handler
import android.os.Looper
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>(AuthState.Checking)
    val authState : LiveData<AuthState> = _authState

    private val _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail

    init {
        checkAuthStatus()

        auth.addAuthStateListener { firebaseAuth ->

            val user = firebaseAuth.currentUser

            _userEmail.value = user?.email

            if (user != null && _authState.value !is AuthState.Authenticated) {
                _authState.value = AuthState.Authenticated
            }
        }
    }

    fun checkAuthStatus() {
        val user = auth.currentUser
        _userEmail.value = user?.email

        if (user != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email : String, password : String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("ТЫ ЖЕ НИЧЕГО НЕ НАПИСАЛ")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Я ХЗ КТО ТЫ")
                }
            }
    }

    fun signup(email : String, password : String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("ТЫ ЖЕ НИЧЕГО НЕ НАПИСАЛ")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userID = task.result?.user?.uid

                    val usersModel = UsersModel(email, userID!! )
                    Firebase.firestore.collection("users").document(userID)
                        .set(usersModel)

                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Я ХЗ КТО ТЫ")
                }
            }
    }

    fun signout() {
        try {
            auth.signOut()
            viewModelScope.launch {
                delay(100)
                _authState.value = AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Ошибка выхода: ${e.message}")
        }
    }
}


sealed class AuthState {
    object Checking : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}