package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_profile.*

class ClienteProfile : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_profile)

        mToolbar = findViewById(R.id.profile_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profile_Button_Historico.setOnClickListener {
            Toast.makeText(this, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }

        //get user data
//        FirebaseAuth.getInstance().currentUser.get
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_profile_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            idBackButton -> onBackPressed()
            R.id.action_menu_profile_page_deslogar -> {
                FirebaseAuth.getInstance().signOut()
                Log.d("ClienteProfileActivity", "Usuário deslogado")
                finish()
            }
        }
        return true
    }
}
