package com.example.asdsda.models

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

data class ProductModel(
    val disc: String = "",
    val id : String = "",
    val image: String = "",
    val price: String = "",
    val title: String = "",
    val weight: String = "",
) {
    constructor() : this("", "", "", "", "", "")
}

fun addItemToCart(id: String, context : Context) {


    val userDoc = Firebase.firestore.collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid!!)

    userDoc.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val currentCart = task.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val currentQuantity = currentCart[id]?:0
            val updatedQuality = currentQuantity + 1;

            val updatedCart = mapOf("cartItems.$id" to updatedQuality)


            userDoc.update(updatedCart)

        }
    }
}

fun removeFromCart(id: String, context : Context) {


    val userDoc = Firebase.firestore.collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid!!)

    userDoc.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val currentCart = task.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val currentQuantity = currentCart[id]?:0
            val updatedQuality = currentQuantity - 1;



            val updatedCart =
                if (updatedQuality<=0) {
                    mapOf("cartItems.$id" to FieldValue.delete())
                }
                else {
                    mapOf("cartItems.$id" to updatedQuality)
                }



            userDoc.update(updatedCart)

        }
    }
}