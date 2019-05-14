package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_login.*

class ClienteLogin : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_login)

        mToolbar = findViewById(R.id.login_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        login_Button_Login.setOnClickListener {
            userLogin()
        }

        login_TextView_Registre.setOnClickListener {
            val intent = Intent(this, ClienteCadastro::class.java)
            startActivity(intent)
            finish()
        }

        login_TextView_Recuperar.setOnClickListener {
            var email = login_EditText_Email.text.toString()
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Um email de recuperação de senha foi enviado para você",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Informe um email para que lhe seja enviado a recuperação de senha",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun userLogin() {
        val email = login_EditText_Email.text.toString()
        val password = login_EditText_Password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "É necessário preencher todos os campos.\nPor favor, verifique", Toast.LENGTH_SHORT)
                .show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = mAuth.currentUser?.uid
                    Log.d("ClienteLoginActivity", "Usuário $uid logado")
                    finish()
                }
                .addOnFailureListener {
                    Log.d("ClienteLoginActivity", "${it.message}")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
