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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    internal var registerUser: TextView?=null
    internal var username: EditText?=null
    internal var password: EditText?=null
    internal var loginButton: Button?=null
    internal var user: String?=""
    internal var pass: String?=""
    internal var reference: DatabaseReference?=null
    internal var database: FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerUser = findViewById<View>(R.id.register) as TextView
        username = findViewById<View>(R.id.username) as EditText
        password = findViewById<View>(R.id.password) as EditText
        loginButton = findViewById<View>(R.id.loginButton) as Button

        registerUser?.setOnClickListener { startActivity(Intent(this@Login, Register::class.java)) }

        loginButton?.setOnClickListener {
            user = username?.text.toString()
            pass = password?.text.toString()

            if (user == "") {
                username?.error = "can't be blank"
            } else if (pass == "") {
                password?.error = "can't be blank"
            } else {
                database = FirebaseDatabase.getInstance()
                reference = database?.reference
                val url = reference.toString() + "/users.json"
                val pd = ProgressDialog(this@Login)
                pd.setMessage("Loading...")
                pd.show()

                val request = StringRequest(Request.Method.GET, url, Response.Listener { s ->
                    if (s == "null") {
                        Toast.makeText(this@Login, "user not found", Toast.LENGTH_LONG).show()
                    } else {
                        try {
                            val obj = JSONObject(s)

                            if (!obj.has(user)) {
                                Toast.makeText(this@Login, "user not found", Toast.LENGTH_LONG).show()
                            } else if (obj.getJSONObject(user).getString("password") == pass) {
                                UserDetails.username = user as String
                                UserDetails.password = pass as String
                                startActivity(Intent(this@Login, Users::class.java))
                            } else {
                                Toast.makeText(this@Login, "incorrect password", Toast.LENGTH_LONG).show()
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

                val rQueue = Volley.newRequestQueue(this@Login)
                rQueue.add(request)
            }
        }
    }
}
