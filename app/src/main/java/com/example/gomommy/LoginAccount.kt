package com.example.gomommy

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.sign

class LoginAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    //private lateinit var googleSignIn: Button
    private lateinit var createAccountTextView: TextView
    //private lateinit var forgotPasswordTextView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        findViewById<Button>(R.id.googleSignIn).setOnClickListener {
            signInGoogle()
        }



        usernameEditText = findViewById(R.id.loginUsernameEditText)
        passwordEditText = findViewById(R.id.loginPasswordEditText)
        //forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)
        createAccountTextView = findViewById(R.id.createAccountTextView)
        loginButton = findViewById(R.id.loginButton)
        loginButton.isEnabled = false // Initially disable the sign-up yesButton


        // Add text change listeners to the username and password EditText fields
        usernameEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        passwordEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform sign-up logic here
            if (username.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        // Redirect to the desired activity
                        val intent = Intent(this, Homepage::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please fill the fields", Toast.LENGTH_SHORT).show()
            }

        }

        createAccountTextView.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
            finish()
        }




    }

//    override fun onStart() {
//        super.onStart()
//
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, MomExperience::class.java)
//            startActivity(intent)
//        }
//    }
    private fun updateSignUpButtonState() {
        val isUsernameFilled = usernameEditText.text.isNotEmpty()
        val isPasswordFilled = passwordEditText.text.isNotEmpty()
        this.loginButton.isEnabled = isUsernameFilled && isPasswordFilled
        if(this.loginButton.isEnabled){
            this.loginButton.setBackgroundResource(R.drawable.log_button)
            this.loginButton.setTextColor(Color.parseColor("#FE5065"))
        }else {
            this.loginButton.setBackgroundResource(R.drawable.disable_button)
            this.loginButton.setTextColor(Color.parseColor("#D2D1D1"))
        }
        val showPasswordToggleButton = findViewById<ToggleButton>(R.id.showPasswordToggleButton)
        val passwordEditText = findViewById<EditText>(R.id.loginPasswordEditText)

        showPasswordToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordEditText.transformationMethod = null
            } else {
                // Hide password
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        // Set cursor position to the end of the password field
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent: Intent = Intent(this, Homepage::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }
}