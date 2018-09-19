package com.example.admin.practize

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import org.json.JSONException
import org.json.JSONObject

class Register : AppCompatActivity() {
    internal var username: EditText?=null
    internal var password: EditText?=null
    internal var registerButton: Button?=null
    internal var user: String?=""
    internal var pass: String?=""
    internal var login: TextView?=null
    internal var reference: DatabaseReference?=null
    internal var database: FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById<View>(R.id.username) as EditText
        password = findViewById<View>(R.id.password) as EditText
        registerButton = findViewById<View>(R.id.registerButton) as Button
        login = findViewById<View>(R.id.login) as TextView

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }

        login?.setOnClickListener { startActivity(Intent(this@Register, Login::class.java)) }

        registerButton?.setOnClickListener {
            user = username?.text.toString()
            pass = password?.text.toString()

            if (user == "") {
                username?.error = "can't be blank"
            } else if (pass == "") {
                password?.error = "can't be blank"
            } else if (!(user?.matches("[A-Za-z0-9]+".toRegex()))!!) {
                username?.error = "only alphabet or number allowed"
            } else {
                val pd = ProgressDialog(this@Register)
                pd.setMessage("Loading...")
                pd.show()
                database = FirebaseDatabase.getInstance()
                reference = database?.reference
                val url = reference.toString() + "/users.json"

                val request = StringRequest(Request.Method.GET, url, Response.Listener { s ->
                    database = FirebaseDatabase.getInstance()
                    reference = database?.getReference("users")

                    if (s == "null") {
                        reference?.child(user)?.child("password")?.setValue(pass)
                        Toast.makeText(this@Register, "registration successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@Register, Login::class.java))
                    } else {
                        try {
                            val obj = JSONObject(s)
                            if (!obj.has(user)) {
                                reference?.child(user)?.child("password")?.setValue(pass)
                                Toast.makeText(this@Register, "registration successful", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@Register, "username already exists", Toast.LENGTH_LONG).show()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }

                    pd.dismiss()
                }, Response.ErrorListener { volleyError ->
                    println("" + volleyError)
                    pd.dismiss()
                })

                val rQueue = Volley.newRequestQueue(this@Register)
                rQueue.add(request)
            }
        }
    }
}