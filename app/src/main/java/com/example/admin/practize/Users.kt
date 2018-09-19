package com.example.admin.practize

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.*

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class Users : AppCompatActivity() {
    internal var usersList: ListView?=null
    internal var noUsersText: TextView?=null
    internal var al = ArrayList<String>()
    internal var totalUsers = 0
    internal var pd: ProgressDialog?=null
    internal var reference: DatabaseReference?=null
    internal var database: FirebaseDatabase?=null
    var applesQuery: Query?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        usersList = findViewById<View>(R.id.usersList) as ListView
        noUsersText = findViewById<View>(R.id.noUsersText) as TextView

        pd = ProgressDialog(this@Users)
        pd?.setMessage("Loading...")
        pd?.show()
        database = FirebaseDatabase.getInstance()
        reference = database?.reference
        val url = reference.toString() + "/users.json"

        val request = StringRequest(Request.Method.GET, url, Response.Listener { s -> doOnSuccess(s) }, Response.ErrorListener { volleyError -> println("" + volleyError) })

        val rQueue = Volley.newRequestQueue(this@Users)
        rQueue.add(request)

        usersList?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            UserDetails.chatWith = al[position]
            startActivity(Intent(this@Users, Chat::class.java))
        }
    }

    fun doOnSuccess(s: String) {
        try {
            val obj = JSONObject(s)

            val i = obj.keys()
            var key = ""

            while (i.hasNext()) {
                key = i.next().toString()

                if (key != UserDetails.username) {
                    al.add(key)
                }

                totalUsers++
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (totalUsers <= 1) {
            noUsersText?.visibility = View.VISIBLE
            usersList?.visibility = View.GONE
        } else {
            noUsersText?.visibility = View.GONE
            usersList?.visibility = View.VISIBLE
            usersList?.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, al)
        }

        pd?.dismiss()
    }
    fun deleteusere(){
        val ref = FirebaseDatabase.getInstance().getReference();
        applesQuery = ref.child("firebase-test").orderByChild("title").equalTo("Apple");
          applesQuery?.addListenerForSingleValueEvent(object: ValueEventListener{
                           override fun onDataChange(dataSnapshot: DataSnapshot) {
                               for (appleSnapshot in dataSnapshot.getChildren())
                               {
                                   appleSnapshot.getRef().removeValue()
                               }
                           }
                           override fun onCancelled(error: DatabaseError) {
                               // Failed to read value
                                Log.e("useractivity", "onCancelled", error.toException());
                           }
                       })

    }
}