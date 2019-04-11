package com.example.quranicmanager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.text.SimpleDateFormat
import java.util.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var db: FirebaseFirestore
    private lateinit var  header:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val value = intent.getStringExtra("userEmail")
        val intent = getIntent();




        setHomeContent();
        db = FirebaseFirestore.getInstance()//code for showing notification on the application
        val docRef = db.collection("feednotification").document("notify")
        docRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if (e != null) {
                Log.w("this is val", "Listen failed.", e)
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                return@EventListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("this is val", "Current data: ${snapshot.data}")
                val notification = findViewById<TextView>(R.id.noticationView);
                notification.text = snapshot["value"].toString()
            } else {
                Log.d("this is val", "Current data: null")
            }
        })
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        if(value!=null) {

            Log.d("Intent VAlue",value)
            //view.text =  value;
            val profilename =    getprofilename(value)

            Log.d("email", value)
        }
        else{
            Log.d("TAG", "intent does not hava value")
        }


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here
        when (item.itemId) {
            R.id.files -> {
                try {
                    val moveToFiles = Intent(this, fileAdd::class.java)
                    startActivity(moveToFiles)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    print(e.message)
                }

            }
            R.id.managefiles -> {
                try {
                    val moveToFiles = Intent(this, FileView::class.java)
                    startActivity(moveToFiles)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    print(e.message)
                }

            }
            R.id.updateprofile -> {

                val moveToFiles = Intent(this, Register::class.java)
                startActivity(moveToFiles)
            }
            R.id.traffic -> {
                try {
                    val users = Intent(this, traffic::class.java)
                    startActivity(users)


                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
            R.id.feedback -> {
                try {
                    val users = Intent(this, feedBack::class.java)
                    startActivity(users)


                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
            R.id.RateManage -> {
                try {
                    val users = Intent(this, viewRating::class.java)
                    startActivity(users)


                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
            R.id.signoutitem -> {
                try {
                    val auth = FirebaseAuth.getInstance();
                    if (auth.currentUser != null) {
                        auth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun drawGraph() {

    }

    fun handleNotification(view: View) {
        val docRef = db.collection("feednotification").document("notify");
        docRef.update("value", 0).addOnSuccessListener {
            intent = Intent(this, feedBack::class.java)
            startActivity(intent)

        }.addOnFailureListener {
            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_LONG).show()
        };
    }

    fun setHomeContent() {
        val monthName = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

        var ans = 0;
        var totalfeedback = 0;
        var totalrating = 0;
        var totalrat = 0;
        val numuser = "5"
        val date = getCurrentDateTime()
        val dateInString = date.toString("MM")
        val dayInString = date.toString("dd")
        try {
            val titledate  =  findViewById<TextView>(R.id.monthTitle);
            titledate.text = monthName.get(dateInString.toInt()+1).toString()+ "  "+titledate
        }
        catch (e:Exception)
        {
            Log.d("Exception",e.localizedMessage)
        }
        val fs = FirebaseFirestore.getInstance();
        fs.collection("apptraffic").get().addOnCompleteListener {
            val result = it.result!!.documents
            for (doc in result) {
                ans = ans + doc.getString("users")!!.toInt();
            }

        }.addOnSuccessListener {
            val totaluses = findViewById<TextView>(R.id.usertotal)
            totaluses.text = ans.toString()
            Log.d("Ansis", ans.toString())
        }
        fs.collection("Feedback").get().addOnCompleteListener {
            val res = it.result!!.documents
            for (doc in res) {
                totalfeedback = totalfeedback + 1;
            }

        }.addOnSuccessListener {
            val feedback = findViewById<TextView>(R.id.feedbackusers)
            feedback.text = totalfeedback.toString()
        }
        fs.collection("Rating").get().addOnCompleteListener {
            val res = it.result!!.documents
            for (doc in res) {
                totalrating = totalrating + 1;
            }

        }.addOnSuccessListener {
            val feedback = findViewById<TextView>(R.id.rateusers)
            feedback.text = totalrating.toString()
        }

    }

    fun getprofilename(string: String):String {
        val storageReference = FirebaseStorage.getInstance().reference
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0);
        val txtview = headerView.findViewById<TextView>(R.id.headerEmail);
        val txtviewprofile = headerView.findViewById<TextView>(R.id.hederUsername)
        txtviewprofile.text = "this is forfile"
        val img = headerView.findViewById<ImageView>(R.id.userimage);


        var ans = " "
        var query = "";
        val fs = FirebaseFirestore.getInstance()
        var ref = fs.collection("users").document(string)
        ref.get().addOnSuccessListener {


            ans = it.getString("profilename").toString()
            query = it.getString("storageref").toString()
            if (query != null) {
                storageReference.child(query).downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it).resize(img.width, img.height).into(img)

                }.addOnFailureListener {
                Log.d("Exception", it.localizedMessage)
            }
        }
            else
                img.setImageResource(R.drawable.ic_user)
            txtview.text = string
            txtviewprofile.text = ans
            Log.d("TAG",ans)
        }
        return  ans
    }


    override fun onResume() {




        super.onResume()
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}

