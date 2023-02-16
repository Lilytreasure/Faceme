package com.example.newsapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.newsapp.firebase.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    // add a  model class to collect the log in credentials
    //validated using firebase authorization
    //enable username and password authentication
    private lateinit var NewUserRegisterBtn : Button
    private lateinit var TxtRegEmail: TextInputEditText
    private lateinit var TxtRegPassword: TextInputEditText
    private lateinit var    txtRegFirstName: TextInputEditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    //Todo-- Add the password confrim
    //Todo--reject entries that do  not follow  the email policy




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //create the firebase instance
        mAuth= FirebaseAuth.getInstance()


        NewUserRegisterBtn=findViewById(R.id.BtnRegToFirebase)

        txtRegFirstName=findViewById(R.id.txtRegFirstName)
        TxtRegEmail=findViewById(R.id.TxtRegEmail)
        TxtRegPassword=findViewById(R.id.TxtRegPassword)



        NewUserRegisterBtn.setOnClickListener {
            //collect user entries from the textview

            //show a progress dialog and dispatch it when the request is successful
            val name=txtRegFirstName.text.toString()
            val email=TxtRegEmail.text.toString()
            val password=TxtRegPassword.text.toString()

            signUp(email, name, password)

        }



    }

    private fun signUp( email: String,name: String, password: String) {
        //register the user to firebase
        //the details will be used as a reference when logging in
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //redirect to MainActivity on successful registration
                    //Add the users in the database
                    //while adding  new users ensure you log out the current user
                    addUserToDatabase(name,email , mAuth.currentUser?.uid!!)

                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    // If sign up fails, display a message to the user.
                    //add a hidden textview to populate when error occurs
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()

                }
            }


    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef=FirebaseDatabase.getInstance().getReference()

        //create children entries to add multiple users in the  database
        mDbRef.child("credentials").child(uid).setValue(User(name, email, uid))

    }
}