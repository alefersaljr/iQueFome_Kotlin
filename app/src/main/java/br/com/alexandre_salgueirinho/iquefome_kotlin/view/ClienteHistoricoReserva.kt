package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.ReservaItem
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_cliente_historico_reserva.*

class ClienteHistoricoReserva : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val db = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_historico_reserva)

        mToolbar = findViewById(R.id.historico_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        carregaReservas()

        historico_Refresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                carregaReservas()
                historico_RecyclerView.adapter?.notifyDataSetChanged()
                historico_Refresh.isRefreshing = false
            }, 3000)
        }
    }

    private fun carregaReservas() {
        val ref = db.getReference("/reservas/historico/${mAuth.currentUser?.uid}")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                historico_ProgressBar.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    Log.d("Reservas", it.toString())
                    val reservaItem = it.getValue(Reservas::class.java)

                    if (reservaItem != null) {
                        adapter.add(ReservaItem(reservaItem))
                    }
                }

                adapter.setOnItemClickListener{ item, view ->

                    historico_ProgressBar.visibility = View.VISIBLE

                    val reservaIt = item as ReservaItem

                    val intent = Intent(view.context, ClienteHistoricoReservaDetalhes::class.java)
                    intent.putExtra(RESERVA_KEY, reservaIt.reserva)

                    historico_ProgressBar.visibility = View.GONE
                    startActivity(intent)

                }

                historico_RecyclerView.adapter = adapter

                historico_ProgressBar.visibility = View.GONE
            }
        })
    }

    companion object{
        val RESERVA_KEY = "RESERVA_KEY"
    }

    override fun onResume() {
        super.onResume()

        carregaReservas()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
