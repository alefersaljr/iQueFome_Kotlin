package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
//import br.com.alexandre_salgueirinho.iquefome_kotlin.model.PratoItem
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Pratos
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    lateinit var anim_open: Animation
    lateinit var anim_close: Animation
    lateinit var anim_rotate: Animation
    lateinit var anim_antirotate: Animation
    var isOpen: Boolean = false
    var isFavorito: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_inicial)

        mToolbar = findViewById(R.id.menu_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inicializaFloatingMenu()

        carregaPratos()

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                inicial_ProgressBar.visibility = View.GONE
            }
        }, 4000)

        inicial_Refresh.setOnRefreshListener {
            Toast.makeText(this, "Atualizando Lista de Pratos", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                carregaPratos()
                inicial_RecyclerView.adapter?.notifyDataSetChanged()
                inicial_Refresh.isRefreshing = false
                Toast.makeText(this, "Lista de Pratos Atualizada", Toast.LENGTH_SHORT).show()
            }, 3000)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun inicializaFloatingMenu() {
        anim_open = AnimationUtils.loadAnimation(applicationContext, R.anim.open_menu)
        anim_close = AnimationUtils.loadAnimation(applicationContext, R.anim.close_menu)
        anim_rotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_menu)
        anim_antirotate = AnimationUtils.loadAnimation(applicationContext, R.anim.anti_rotate_menu)

        floating_Filter_Menu.setOnClickListener {
            if (isOpen) {
                floating_Filter_Menu.setImageResource(R.drawable.icon_filter)
                floating_Filter_Menu.startAnimation(anim_antirotate)
                floating_Filter_one.startAnimation(anim_close)
                floating_Filter_two.startAnimation(anim_close)
                floating_Filter_one.visibility = View.GONE
                floating_Filter_two.visibility = View.GONE
                isOpen = false
            } else {
                floating_Filter_Menu.startAnimation(anim_rotate)
                floating_Filter_one.startAnimation(anim_open)
                floating_Filter_two.startAnimation(anim_open)
                floating_Filter_one.visibility = View.VISIBLE
                floating_Filter_two.visibility = View.VISIBLE
                floating_Filter_one.isClickable = true
                floating_Filter_two.isClickable = true
                isOpen = true
                floating_Filter_Menu.setImageResource(R.drawable.icon_close)
            }
        }

        floating_Filter_one.setOnClickListener {
            isFavorito = !isFavorito
            if (isFavorito) {
                floating_Filter_one.setImageResource(R.drawable.icon_star_on)
            }else if (!isFavorito){
                floating_Filter_one.setImageResource(R.drawable.icon_star_off)
            }
            Toast.makeText(this@ClienteInicial, "$isFavorito", Toast.LENGTH_SHORT).show()
        }

        floating_Filter_two.setOnClickListener {
            Toast.makeText(this@ClienteInicial, "Clicou no segundo", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class PratoItem(val prato: Pratos) : Item<ViewHolder>() {

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


