package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    // add a  model class to collect the log in credentials
    //validated using firebase authorization
    //enable username and password authentication
    private lateinit var NewUserRegisterBtn : Button
    private lateinit var TxtUsername: TextInputEditText
    private lateinit var TxtPassword: TextInputEditText
    private lateinit var    FirstName: TextInputEditText

    private lateinit var mAuth: FirebaseAuth

    //Todo-- Add the password confrim
    //Todo--reject entries that do  not follow  the email policy




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //create the firebase instance
        mAuth= FirebaseAuth.getInstance()


        NewUserRegisterBtn=findViewById(R.id.NewUserRegisterBtn)
        TxtUsername=findViewById(R.id.TxtUsername)
        TxtPassword=findViewById(R.id.TxtPassword)
        FirstName=findViewById(R.id.FirstName)


        NewUserRegisterBtn.setOnClickListener {
            //collect user entries from the textview

            //show a progress dialog and dispatch it when the request is successful

            val email=TxtUsername.text.toString()
            val password=TxtPassword.text.toString()

            signUp(email, password)


        }



    }

    private fun signUp(email: String, password: String) {
        //register the user to firebase
        //the details will be used as a reference when logging in
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //redirect to MainActivity on successful registration
                    val intent=Intent(this,MainActivity::class.java)


                } else {
                    // If sign up fails, display a message to the user.
                    //add a hidden textview to populate when error occurs
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()


                }
            }







    }
}