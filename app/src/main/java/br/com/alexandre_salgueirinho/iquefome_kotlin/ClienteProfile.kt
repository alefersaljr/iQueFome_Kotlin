package br.com.alexandre_salgueirinho.iquefome_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class ClienteProfile : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_profile)

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_profile_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_profile_page_deslogar -> Toast.makeText(this, "Clicou em deslogar", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
