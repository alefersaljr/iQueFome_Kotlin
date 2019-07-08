package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Pratos
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Reservas
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Usuário
import com.google.firebase.auth.FirebaseAuth
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
    var userId = FirebaseAuth.getInstance().currentUser?.uid
    var reserva_Cliente_Nome = ""
    var reserva_Cliente_Sobrenome = ""
    var reserva_Cliente_Pontos = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_prato_composicao)

        mToolbar = findViewById(R.id.composition_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prato = intent.getParcelableExtra<Pratos>(ClienteInicial.PRATO_KEY)

        getPrato(prato.pratoId)

        favoritar(prato)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                composition_ProgressBar.visibility = View.GONE
            }
        }, 4000)

        composition_Share.setOnClickListener { view ->
            sharePrato(prato)
        }

        composition_Button_Reservar.setOnClickListener {
            Toast.makeText(this, "Acompanhamentos em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }

        composition_Button_Reservar.setOnClickListener {
            //            Toast.makeText(this, "Carrinho em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
//            startActivity(
//                Intent(this, ClienteCarrinho::class.java)
//            )
            if (userId.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "É necessário realizar o login primeiro", Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext, ClienteLogin::class.java))
            } else {
                doReserva(prato)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        userId = FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun getClienteData() {
        val ref = FirebaseDatabase.getInstance().getReference("users/cadastros/clientes/$userId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Usuário::class.java)

                try {
                    if (user != null) {
                        reserva_Cliente_Nome = user.cliente_Nome
                        reserva_Cliente_Sobrenome = user.cliente_Sobrenome
                        reserva_Cliente_Pontos = user.cliente_Pontos
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@ClientePratoComposicao, "${ex.message}", Toast.LENGTH_SHORT).show()
                    composition_ProgressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun doReserva(prato: Pratos) {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_reserva, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()

        mDialog.reserva_popup_Button_Enviar.setOnClickListener {
            composition_ProgressBar.visibility = View.VISIBLE
            mDialog.reserva_popup_ProgressBar.visibility = View.VISIBLE

            getClienteData()

            reservar(prato, mAlertDialog, mDialog)
        }

        mDialog.reserva_popup_Button_Close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun reservar(prato: Pratos, mAlertDialog: AlertDialog, mDialog: View) {
        val reserva_Id = UUID.randomUUID().toString()
        val reserva_Hora = mDialog.reserva_popup_hora_Data.text.toString().replace(".", ":")
        val reserva_Cliente_Alteracao = mDialog.reserva_popup_alteracoes.text.toString()
        val reserva_Prato_Nome = prato.pratoNome
        val reserva_Prato_Preco = prato.pratoPreco
        val reserva_Restaurante_Nome = prato.pratoRestaurante

        if (userId != null) {

            val ref = FirebaseDatabase.getInstance().getReference("/reservas/restaurante/$reserva_Id")
            val refHistorico = FirebaseDatabase.getInstance().getReference("/reservas/historico/$userId/$reserva_Id")

            val reserva = Reservas(
                reserva_Id,
                userId!!,
                reserva_Hora,
                reserva_Cliente_Nome,
                reserva_Cliente_Sobrenome,
                reserva_Cliente_Alteracao,
                reserva_Cliente_Pontos,
                reserva_Prato_Nome,
                reserva_Prato_Preco,
                reserva_Restaurante_Nome
            )

            ref.setValue(reserva).addOnSuccessListener {
                Log.d("CadastroTESTE", "Finalmente deu boa")
                Toast.makeText(applicationContext, "Reservado com sucesso", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                mDialog.reserva_popup_ProgressBar.visibility = View.GONE
                Toast.makeText(this, "Erro na reserva, ${it.message}", Toast.LENGTH_LONG).show()
            }

            refHistorico.setValue(reserva).addOnSuccessListener {
                Log.d("CadastroTESTE", "Finalmente deu boa")
                Toast.makeText(applicationContext, "Reservado com sucesso", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                mDialog.reserva_popup_ProgressBar.visibility = View.GONE
                Toast.makeText(this, "Erro na reserva, ${it.message}", Toast.LENGTH_LONG).show()
            }

            mAlertDialog.dismiss()
        }
        mDialog.reserva_popup_ProgressBar.visibility = View.GONE
        composition_ProgressBar.visibility = View.GONE
        finish()
    }

    private fun favoritar(prato: Pratos) {
        float_button_favorito.setOnClickListener {
            isFavorito = !isFavorito
            if (isFavorito) {
                float_button_favorito.setImageResource(R.drawable.icon_star_on)
                setFavorito(prato)
            } else if (!isFavorito) {
                float_button_favorito.setImageResource(R.drawable.icon_star_off)
                removeFavorito(prato)
            }
//            Toast.makeText(this@ClientePratoComposicao, "$isFavorito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFavorito(prato: Pratos) {
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/favoritos/$userId/${prato.pratoId}")

        ref.removeValue()

        Toast.makeText(applicationContext, "Prato Desfavoritado", Toast.LENGTH_SHORT).show()
    }

    private fun setFavorito(prato: Pratos) {
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/favoritos/$userId/${prato.pratoId}")

        val pratoFavorito = Pratos(
            prato.pratoId,
            prato.pratoNome,
            prato.pratoPreco,
            prato.pratoRestaurante,
            prato.pratoRestauranteId,
            prato.pratoUrlFoto,
            prato.pratoDescricao,
            prato.pratoTipo,
            prato.pratoTipoComida
        )

        ref.setValue(pratoFavorito).addOnSuccessListener {
            Log.d("Favoritar", "Favoritado")
            Toast.makeText(applicationContext, "Prato Favoritado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Log.d("Favoritar", "Erro ao favoritar")
            Toast.makeText(applicationContext, "Erro ao Favoritar", Toast.LENGTH_SHORT).show()
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

        getClienteData()

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}