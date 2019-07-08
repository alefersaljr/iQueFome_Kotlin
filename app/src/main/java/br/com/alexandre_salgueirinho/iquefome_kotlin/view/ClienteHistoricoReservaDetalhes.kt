package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Feedback
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cliente_historico_reserva_detalhes.*
import kotlinx.android.synthetic.main.activity_cliente_prato_composicao.*
import kotlinx.android.synthetic.main.popup_feedback.view.*
import kotlinx.android.synthetic.main.popup_recuperar.view.*
import java.util.*

class ClienteHistoricoReservaDetalhes : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val db = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_historico_reserva_detalhes)

        mToolbar = findViewById(R.id.historico_Detalhes_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reserva = intent.getParcelableExtra<Reservas>(ClienteHistoricoReserva.RESERVA_KEY)

        getReservaData(reserva)

        historico_Detalhes_Button_Feedback.setOnClickListener {
            goToFeedback(reserva)
        }
    }

    private fun getReservaData(reserva: Reservas?) {
        historico_Detalhes_Cliente_Data_Nome_Cliente.text = reserva?.cliente_Nome
        historico_Detalhes_Cliente_Data_Sobrenome.text = reserva?.cliente_Sobrenome
        historico_Detalhes_Cliente_Data_Nome_Restaurante.text = reserva?.restaurante_Nome
        historico_Detalhes_Cliente_Data_Nome_Prato.text = reserva?.prato_Nome
        historico_Detalhes_Cliente_Data_Preco_Prato.text = reserva?.prato_Preco
        historico_Detalhes_Cliente_Data_Hora.text = reserva?.reserva_Hora
        historico_Detalhes_Cliente_Numero_Pontuacao.text = reserva?.cliente_pontos
        historico_Detalhes_alteracao_Data.text = reserva?.cliente_Alteracao
    }

    private fun goToFeedback(reserva: Reservas) {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_feedback, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()

        val feedback_Id = UUID.randomUUID().toString()

        val ref = db.getReference("/feedbacks/${reserva.restaurante_Id}/$feedback_Id")

        mDialog.feedback_popup_Button_Enviar.setOnClickListener {

            val feedback_Cliente_Alteracao = mDialog.feedback_popup_alteracoes.text.toString()
            val feedback_Prato_Nome = reserva.prato_Nome

            val feedback = Feedback(
                feedback_Id,
                feedback_Cliente_Alteracao,
                feedback_Prato_Nome,
                reserva.restaurante_Id
            )
            ref.setValue(feedback).addOnSuccessListener {
                Toast.makeText(applicationContext, "Feedback enviado", Toast.LENGTH_SHORT).show()
                mAlertDialog.dismiss()
                historico_Detalhes_Button_Feedback.isEnabled = false
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Falha ao enviar o feedback", Toast.LENGTH_SHORT).show()
            }
        }

        mDialog.feedback_popup_Button_Close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
