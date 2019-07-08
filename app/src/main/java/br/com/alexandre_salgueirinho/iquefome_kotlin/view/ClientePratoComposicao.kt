package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Pratos
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cliente_prato_composicao.*
import kotlinx.android.synthetic.main.popup_reserva.view.*
import java.util.*

class ClientePratoComposicao : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    var isFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_prato_composicao)

        mToolbar = findViewById(R.id.composition_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prato = intent.getParcelableExtra<Pratos>(ClienteInicial.PRATO_KEY)

        getPrato(prato.pratoId)

        getClienteData()

        favoritar()

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                composition_ProgressBar.visibility = View.GONE
            }
        }, 4000)

        composition_Share.setOnClickListener { view ->
            sharePrato(prato)
        }

        composition_Button_Editar.setOnClickListener {
            Toast.makeText(this, "Acompanhamentos em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }

        composition_Button_Adicionar.setOnClickListener {
            //            Toast.makeText(this, "Carrinho em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
//            startActivity(
//                Intent(this, ClienteCarrinho::class.java)
//            )
            doReserva(prato)
        }
    }

    private fun getClienteData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun doReserva(prato: Pratos) {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_reserva, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()

        mDialog.reserva_popup_Button_Enviar.setOnClickListener {
            mDialog.reserva_popup_ProgressBar.visibility = View.VISIBLE

            reservar(prato, mAlertDialog, mDialog)
        }

        mDialog.reserva_popup_Button_Close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun reservar(prato: Pratos, mAlertDialog: AlertDialog, mDialog: View) {
        val reserva_Id = UUID.randomUUID().toString()
        val reserva_Hora = ""
        val reserva_Cliente_Nome = ""
        val reserva_Cliente_Sobrenome = ""
        val reserva_Cliente_Alteracao = ""
        val reserva_Cliente_Pontos = ""
        val reserva_Prato_Nome = prato.pratoNome
        val reserva_Prato_Preco = prato.pratoPreco

        mAlertDialog.dismiss()
        mDialog.reserva_popup_ProgressBar.visibility = View.GONE
    }

    private fun favoritar() {
        float_button_favorito.setOnClickListener {
            isFavorito = !isFavorito
            if (isFavorito) {
                float_button_favorito.setImageResource(R.drawable.icon_star_on)
            } else if (!isFavorito) {
                float_button_favorito.setImageResource(R.drawable.icon_star_off)
            }
            Toast.makeText(this@ClientePratoComposicao, "$isFavorito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePrato(prato: Pratos) {
        val intent = Intent()
        val message = "Olá, experimentei o prato '${prato.pratoNome}' e gostei bastante. Recomendo"

        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Compartilhar com: "))
    }

    private fun getPrato(pratoID: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/pratos/clientes/$pratoID")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val prato = p0.getValue(Pratos::class.java)

                try {
                    if (prato != null) {
                        val preco = "R\$" + prato.pratoPreco

                        composition_Prato_Nome.text = prato.pratoNome
                        composition_Restaurante_Nome.text = prato.pratoRestaurante
                        composition_Prato_Descricao_Data.text = prato.pratoDescricao
                        composition_Prato_Preco.text = preco

                        Picasso.get().load(prato.pratoUrlFoto).into(composition_Prato_Foto)

                        val handler = Handler()
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                composition_ProgressBar.visibility = View.GONE
                            }
                        }, 2000)

                    } else {
                        Toast.makeText(this@ClientePratoComposicao, "Por favor, realize o login", Toast.LENGTH_SHORT)
                            .show()
                        composition_ProgressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@ClientePratoComposicao, "${ex.message}", Toast.LENGTH_SHORT).show()
                    composition_ProgressBar.visibility = View.GONE
                }
            }
        })
    }
}