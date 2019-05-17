package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_login.*
import kotlinx.android.synthetic.main.popup_recuperar.view.*

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

        login_TextView_Cadastrese.setOnClickListener {
            val intent = Intent(this, ClienteCadastro::class.java)
            startActivity(intent)
            finish()
        }

        recuperarSenha()

    }

    private fun recuperarSenha() {
        login_TextView_Recuperar.setOnClickListener {
            val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_recuperar, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialog)
            val mAlertDialog = mBuilder.show()


            mDialog.popup_Button_Enviar.setOnClickListener {

                val email = mDialog.popup_EditText_Email.text.toString()

                mDialog.recuperar_ProgressBar.visibility = View.VISIBLE


                if (!email.isNullOrEmpty()) {
                    Log.d("Popup", "Email não vazio")
                    mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            Log.d("Popup", "sucesso")
                            Toast.makeText(
                                this,
                                "Um userEmail de recuperação de senha foi enviado para você",
                                Toast.LENGTH_SHORT
                            ).show()

                            mDialog.recuperar_ProgressBar.visibility = View.GONE
                            mAlertDialog.dismiss()

                        }.addOnFailureListener {
                            mDialog.recuperar_ProgressBar.visibility = View.GONE
                            Log.d("Popup", "falha")
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    mDialog.recuperar_ProgressBar.visibility = View.GONE
                    Log.d("Popup", "Erro")
                    Toast.makeText(
                        this,
                        "Informe um userEmail para que lhe seja enviado a recuperação de senha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            mDialog.popup_Button_Close.setOnClickListener {
                mAlertDialog.dismiss()
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
            login_ProgressBar.visibility = View.VISIBLE
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = mAuth.currentUser?.uid
                    Log.d("ClienteLoginActivity", "Usuário $uid logado")
                    finish()
                    login_ProgressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    login_ProgressBar.visibility = View.GONE
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
