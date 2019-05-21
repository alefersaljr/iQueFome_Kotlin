package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Pratos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cliente_prato_composicao.*

class ClientePratoComposicao : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_prato_composicao)

        mToolbar = findViewById(R.id.composition_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prato = intent.getParcelableExtra<Pratos>(ClienteInicial.PRATO_KEY)

        getPrato(prato.pratoId)

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
            Toast.makeText(this, "Carrinho em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePrato(prato: Pratos) {
        val intent = Intent ()
        val message = "Olá, experimentei o prato '${prato.pratoNome}' e gostei bastante. Recomendo"

        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Compartilhar com: "))
    }

    private fun getPrato(pratoID: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/pratos/$pratoID")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(p0: DataSnapshot) {
                val prato = p0.getValue(Pratos::class.java)

                try {
                    if (prato != null) {
                        val preco = "R\$" + prato.pratoPreco

                        composition_Prato_Nome.text = prato.pratoNome
                        composition_Restaurante_Nome.text = prato.pratoRestaurante
                        composition_Prato_Descricao_Data.text = prato.pratoDescricao
                        composition_Prato_Preco.text = preco
//                        textViewIndicado_Data.text = prato.

                        Picasso.get().load(prato.pratoUrlFoto).into(composition_Prato_Foto)

                        val handler = Handler()
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                composition_ProgressBar.visibility = View.GONE
                            }
                        }, 2000)

                    } else {
                        Toast.makeText(this@ClientePratoComposicao, "Por favor, realize o login", Toast.LENGTH_SHORT).show()
                        composition_ProgressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@ClientePratoComposicao, "${ex.message}", Toast.LENGTH_SHORT).show()
                    composition_ProgressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_shop, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            idBackButton -> onBackPressed()
            R.id.action_menu_icon_shop -> {
                Toast.makeText(this, "Carrinho em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}