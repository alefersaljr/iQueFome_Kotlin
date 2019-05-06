package br.com.alexandre_salgueirinho.iquefome_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

class ClienteInicial : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mDrawerLayout = findViewById(R.id.drawer)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (mToggle.onOptionsItemSelected(item)) {
            when (item?.itemId){
                R.id.menu_carrinho -> Toast.makeText(this, "Clicou em Carrinho", Toast.LENGTH_SHORT).show()
                R.id.menu_historico -> Toast.makeText(this, "Clicou em Historico", Toast.LENGTH_SHORT).show()
                R.id.menu_inicial -> Toast.makeText(this, "Clicou em Inicial", Toast.LENGTH_SHORT).show()
                R.id.menu_logout -> Toast.makeText(this, "Clicou em Logout", Toast.LENGTH_SHORT).show()
                R.id.menu_profile -> Toast.makeText(this, "Clicou em Profile", Toast.LENGTH_SHORT).show()
                R.id.menu_share -> Toast.makeText(this, "Clicou em Share", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
