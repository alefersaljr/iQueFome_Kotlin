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
//import br.com.alexandre_salgueirinho.iquefome_kotlin.model.PratoItem
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Pratos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_cliente_inicial.*
import kotlinx.android.synthetic.main.item_prato.view.*

class ClienteInicial : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mToolbar = findViewById(R.id.menu_Toolbar)
        setSupportActionBar(mToolbar)

        carregaPratos()

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                inicial_ProgressBar.visibility = View.GONE
            }
        }, 4000)

        composition_Refresh.setOnClickListener {
            Toast.makeText(this, "Atualizando Pratos", Toast.LENGTH_SHORT).show()
            inicial_ProgressBar.visibility = View.VISIBLE
            carregaPratos()
            inicial_ProgressBar.visibility = View.GONE
        }
    }

    companion object{
        val PRATO_KEY = "PRATO_KEY"
    }

    private fun carregaPratos() {
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/clientes")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("ClienteInicial", it.toString())
                    val pratoItem = it.getValue(Pratos::class.java)

                    if (pratoItem != null) {
                        adapter.add(PratoItem(pratoItem))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    inicial_ProgressBar.visibility = View.VISIBLE

                    val pratoI = item as PratoItem

                    val intent = Intent(view.context, ClientePratoComposicao::class.java)
                    intent.putExtra(PRATO_KEY, pratoI.prato)

                    inicial_ProgressBar.visibility = View.GONE
                    startActivity(intent)
                }
                inicial_RecyclerView.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_icon_account -> {
                if (FirebaseAuth.getInstance().currentUser != null) startActivity(
                    Intent(
                        this,
                        ClienteProfile::class.java
                    )
                )
                else if (FirebaseAuth.getInstance().currentUser == null) startActivity(
                    Intent(
                        this,
                        ClienteLogin::class.java
                    )
                )
            }
        }
        return true
    }
}

class PratoItem (val prato:Pratos): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val precoPrato = "R\$ " + prato.pratoPreco
        viewHolder.itemView.textView_Nome_Prato.text = prato.pratoNome
        viewHolder.itemView.textView_Restaurante.text = prato.pratoRestaurante
        viewHolder.itemView.textView_Preco.text = precoPrato

        Picasso.get().load(prato.pratoUrlFoto).into(viewHolder.itemView.circleImage_Perfil)
    }

    override fun getLayout(): Int {
        return R.layout.item_prato
    }
}


