package com.example.task58

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.example.task58.Models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rengwuxian.materialedittext.MaterialEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {
    lateinit var btnSignIn: Button
    lateinit var btnRegister: Button
    lateinit var root: RelativeLayout

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseDatabase
    lateinit var database: DatabaseReference
    lateinit var users: DatabaseReference

    companion object {
        lateinit var currentUserId: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        initView()
        initData()

        btnRegister.setOnClickListener { showRegisterWindow() }
        btnSignIn.setOnClickListener { showSignInWindow() }
    }

    private fun showSignInWindow() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Sign in")

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val sign_in_window: android.view.View? = inflater.inflate(R.layout.sign_in_window, null)
        dialog.setView(sign_in_window)

        val email: MaterialEditText = sign_in_window!!.findViewById(R.id.emailFieldLogin)
        val password: MaterialEditText = sign_in_window.findViewById(R.id.passFieldLogin)

        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton(
            "Sign in",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                    if (TextUtils.isEmpty(email.text.toString())) {
                        Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    if (password.text.toString().isEmpty()) {
                        Snackbar.make(root, "Enter your password", Snackbar.LENGTH_SHORT).show()
                        return
                    }

                    logIn(email, password)
                }
            })
        dialog.show()
    }

    private fun showRegisterWindow() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Registration")

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val registerWindow: android.view.View? = inflater.inflate(R.layout.register_window, null)
        dialog.setView(registerWindow)

        val email: MaterialEditText = registerWindow!!.findViewById(R.id.emailFieldRegister)
        val password: MaterialEditText = registerWindow.findViewById(R.id.passFieldRegister)
        val name: MaterialEditText = registerWindow.findViewById(R.id.nameFieldRegister)

        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Add",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                    if (TextUtils.isEmpty(email.text.toString())) {
                        Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    if (TextUtils.isEmpty(name.text.toString())) {
                        Snackbar.make(root, "Enter your name", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    if (password.text.toString().isEmpty()) {
                        Snackbar.make(root, "Enter your password", Snackbar.LENGTH_SHORT).show()
                        return
                    }

                    if (password.text.toString().length in 1..5) {
                        password.text?.append("00000")
                    }

                    registerUser(email, name, password)
                }
            })
        dialog.show()
    }

    private fun registerUser(
        email: MaterialEditText,
        name: MaterialEditText,
        password: MaterialEditText
    ) {
        auth.createUserWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnSuccessListener {
            val registrationDate = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
            val lastLogin = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
            val user: User = User(
                auth.currentUser!!.uid,
                name.text.toString(),
                email.text.toString(),
                registrationDate,
                lastLogin,
                false,
                password.text.toString()
            )

            FirebaseAuth.getInstance().currentUser.let {
                users.child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user)
                    .addOnSuccessListener {
                        Snackbar.make(root, "User added", Snackbar.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Snackbar.make(root, "Error." + it.message, Snackbar.LENGTH_SHORT).show()
                    }
            }

        }.addOnFailureListener {
            Snackbar.make(root, "Error." + it.message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun logIn(
        email: MaterialEditText,
        password: MaterialEditText
    ) {
        var pass = password.text.toString()
        if (pass.length in 1..5) {
            pass += "00000"
        }

        auth.signInWithEmailAndPassword(email.text.toString(), pass)
            .addOnSuccessListener {
                currentUserId = auth.currentUser!!.uid

                database.child(currentUserId).get().addOnSuccessListener {
                    val status = it.child("status").value.toString().toBoolean()

                    if (status) {
                        Snackbar.make(root, "This user is blocked", Snackbar.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    } else {
                        val lastLogin = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
                        val newUser = mapOf(
                            "lastLogin" to lastLogin
                        )

                        database.child(currentUserId).updateChildren(newUser)
                        switchActivity()
                    }
                }
            }.addOnFailureListener {
                Snackbar.make(root, "Authorisation error. " + it.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
    }


    private fun switchActivity() {
        val intent = Intent(this, UserlistActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        btnSignIn = findViewById<Button>(R.id.btnSignIn)
        btnRegister = findViewById<Button>(R.id.btnRegister)
        root = findViewById(R.id.relativeLayoutLogin)
    }

    private fun initData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}