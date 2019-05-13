package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class ClienteProfile : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_profile)

        mToolbar = findViewById(R.id.profile_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_profile_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            idBackButton -> onBackPressed()
            R.id.action_menu_profile_page_deslogar -> startActivity ( Intent (this, ClienteLogin::class.java) )
//            R.id.action_menu_profile_page_deslogar -> Toast.makeText(this, "Clicou em deslogar", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
