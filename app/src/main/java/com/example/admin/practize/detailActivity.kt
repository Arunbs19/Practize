package com.example.admin.practize

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.collections.HashMap

class detailActivity : AppCompatActivity() {
    var database:FirebaseDatabase?=null
    var myRef:DatabaseReference?=null
    val TAG="detailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val txt_detail1 =findViewById<View>(R.id.txt_detail1) as TextView
        txt_detail1.setOnClickListener { view ->
             database = FirebaseDatabase.getInstance();
             myRef = database?.getReference("message");
             myRef?.setValue("Hello, World!");
        }
        val txt_detail2 =findViewById<View>(R.id.txt_detail2) as TextView
        txt_detail2.setOnClickListener { view ->
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
          /*  myRef?.addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val value = dataSnapshot.getValue(String::class.java)
                        Log.d(TAG, "Value is: " + value)
                        txt_detail2.text=value
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException())
                    }
                })*/

    }
}
