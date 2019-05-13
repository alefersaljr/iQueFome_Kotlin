package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cliente_inicial.*

class ClienteInicial : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mToolbar = findViewById(R.id.menu_Toolbar)

        setSupportActionBar(mToolbar)

        recyclerView_inicial.layoutManager = LinearLayoutManager(this)
        recyclerView_inicial.adapter = InicialAdapter ()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_profile -> startActivity(Intent(this, ClienteProfile :: class.java))
        }
        return true
    }
}
