package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cliente_historico_reserva_detalhes.*

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
            goToFeedback()
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

    private fun goToFeedback() {
        Toast.makeText(applicationContext, "Em dev. aguarde", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
