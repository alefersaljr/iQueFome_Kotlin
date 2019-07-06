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
import kotlinx.android.synthetic.main.activity_cliente_carrinho.*
import kotlinx.android.synthetic.main.popup_reserva.view.*

class ClienteCarrinho : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_carrinho)

        mToolbar = findViewById(R.id.carrinho_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        doReserva()
    }

    private fun doReserva() {
        carrinho_Button_Reserva.setOnClickListener {
            val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_reserva, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialog)
            val mAlertDialog = mBuilder.show()


            mDialog.reserva_popup_Button_Enviar.setOnClickListener {

                val celularRestaurante = "988619505"

                mDialog.reserva_popup_ProgressBar.visibility = View.VISIBLE


                if (!celularRestaurante.isNullOrEmpty()) {

                    enviaReserva(celularRestaurante)
                    Log.d("Popup", "Celular não vazio")
                    Thread.sleep(5000)
//                    mAuth.sendPasswordResetEmail(email)
//                        .addOnSuccessListener {
//                            Log.d("Popup", "sucesso")
//                            Toast.makeText(
//                                this,
//                                "Um Cliente_Email de recuperação de senha foi enviado para você",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
                            mDialog.reserva_popup_ProgressBar.visibility = View.GONE
                            mAlertDialog.dismiss()
//
//                        }.addOnFailureListener {
//                            mDialog.reserva_popup_ProgressBar.visibility = View.GONE
//                            Log.d("Popup", "falha")
//                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
//                        }
                } else {
                    mDialog.reserva_popup_ProgressBar.visibility = View.GONE
                    Log.d("Popup", "Erro")
                    Toast.makeText(
                        this,
                        "Erro no envio da reserva",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            mDialog.reserva_popup_Button_Close.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun enviaReserva(celularRestaurante: String) {
        val intent = Intent ()
        val message = "Olá, experimentei o prato '$celularRestaurante' e gostei bastante. Recomendo"

        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Compartilhar com: "))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
