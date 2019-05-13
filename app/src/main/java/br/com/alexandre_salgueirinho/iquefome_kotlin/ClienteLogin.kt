package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_login.*

class ClienteLogin : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_login)

        mToolbar = findViewById(R.id.login_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        userLogin()

        login_TextView_Registre.setOnClickListener {
            val intent = Intent(this, ClienteCadastro::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun userLogin() {
        val email = login_EditText_Email.text.toString()
        val password = login_EditText_Password.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { }
//            .addOnFailureListener { }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
