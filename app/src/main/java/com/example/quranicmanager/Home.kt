package com.example.quranicmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            drawGraph()
        }
        catch (e:Exception)
        {
            //Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


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
                    val moveToFiles = Intent(this, fileMenu::class.java)
                    startActivity(moveToFiles)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    print(e.message)
                }

            }
            R.id.users -> {

                val moveToFiles = Intent(this, Register::class.java)
                startActivity(moveToFiles)
            }
            R.id.traffic -> {

            }
            R.id.feedback -> {
                try {
                    val users = Intent(this, feedBack::class.java)
                    startActivity(users)


                }
                catch (e:Exception)
                {
                    Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
                }

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun drawGraph()
    {
        val graph = findViewById(R.id.graph) as GraphView
        val series = BarGraphSeries(
            arrayOf(

                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(5.0, 6.0)
            )
        )

        graph.addSeries(series)

        series.isDrawValuesOnTop = true
        series.spacing = 50
        series.valuesOnTopColor = Color.RED

    }
}
