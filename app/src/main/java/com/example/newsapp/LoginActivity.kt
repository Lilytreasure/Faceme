package com.example.newsapp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View


class LoginActivity : AppCompatActivity() {
    //add firebase auth
    // Google auth
    //Save the user login state
    //Log out the user when   they request to log out
    //save the log state
    //Google auth --- use-prompt user  for email password
    private lateinit var NewUserRegisterBtn : Button
    private lateinit var TxtUsername: TextInputEditText
    private lateinit var TxtPassword: TextInputEditText
    private lateinit var mAuth:FirebaseAuth
    private lateinit var BtnLogin: Button
    private  lateinit var dialog: ProgressDialog
    private lateinit var textError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        NewUserRegisterBtn=findViewById(R.id.NewUserRegisterBtn)

        TxtUsername=findViewById(R.id.TxtUsername)
        TxtPassword=findViewById(R.id.TxtPassword)
        BtnLogin=findViewById(R.id.BtnLogin)
        textError=findViewById(R.id.textError)




        //create an instance of firebase auth

        mAuth=FirebaseAuth.getInstance()
        dialog = ProgressDialog(this)

        //validate username and password
        //only take variables  that match the  email format
        //if  the password field is empty catch  the error
        BtnLogin.setOnClickListener {

            //add a progress dialog and dispatch it when the request is successfull

            dialog.setTitle("Habari")
            dialog.setMessage("Loading....")
            dialog.show()



           val email= TxtUsername.text.toString()
           val password= TxtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                TxtUsername.setError("email/username is empty")
                TxtUsername.requestFocus()
                TxtPassword.setError("password cannot  be empty")
                TxtPassword.requestFocus()
                dialog.dismiss()

            }else{

                login(email,password)
            }
        }
        //clear the text Error
        TxtUsername.setOnClickListener {
            textError.isVisible=false
        }
        TxtPassword.setOnClickListener {
            textError.isVisible=false
        }

        

        NewUserRegisterBtn.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }



//End of onCreate Method
    }

    private fun login(email: String, password: String) {
        //reference from the credentials in firebase
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()

                    // Sign in success, update UI with the signed-in user's information
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                    textError.isVisible=true
                    dialog.dismiss()
                }
            }


    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }


}