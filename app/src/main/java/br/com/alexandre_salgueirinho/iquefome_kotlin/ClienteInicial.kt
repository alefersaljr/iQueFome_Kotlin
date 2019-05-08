package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

class ClienteInicial : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mToolbar = findViewById(R.id.toolbar)

        setSupportActionBar(mToolbar)

//        mDrawerLayout = findViewById(R.id.drawer)
//        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
//        mDrawerLayout.addDrawerListener(mToggle)
//        mToggle.syncState()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
//            R.id.action_menu_profile -> Toast.makeText(this, "Clicou em perfil", Toast.LENGTH_SHORT).show()
            R.id.action_menu_profile -> startActivity(Intent(this, ClienteProfile :: class.java))
        }
        return true
    }
}
