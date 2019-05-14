package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alexandre_salgueirinho.iquefome_kotlin.InicialAdapter
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_inicial.*

class ClienteInicial : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mToolbar = findViewById(R.id.menu_Toolbar)

        setSupportActionBar(mToolbar)

        recyclerView_inicial.layoutManager = LinearLayoutManager(this)
        recyclerView_inicial.adapter = InicialAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_profile -> {
                if(FirebaseAuth.getInstance().currentUser != null) startActivity(Intent(this, ClienteProfile:: class.java))
                else if(FirebaseAuth.getInstance().currentUser == null) startActivity(Intent(this, ClienteLogin:: class.java))
            }
        }
        return true
    }
}
