package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Usuário
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cliente_profile.*

class ClienteProfile : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_profile)

        getUsers()

        mToolbar = findViewById(R.id.profile_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profile_Button_Historico.setOnClickListener {
            Toast.makeText(this, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUsers() {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userID")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Usuário::class.java)

                if (user != null) {
                    textViewNome.text = user.userNome
                    textViewSobrenome.setText(user.userSobrenome)
                    textViewCelular_Data.setText(user.userCelular)
                    textViewEmail_Data.setText(user.userEmail)
                    textViewIndicado_Data.setText(user.userIndicado)

                    Picasso.get().load(user.userUrlImagemPerfil).into(perfil_image)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_profile_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            idBackButton -> onBackPressed()
            R.id.action_menu_profile_page_deslogar -> {
                FirebaseAuth.getInstance().signOut()
                Log.d("ClienteProfileActivity", "Usuário deslogado")
                finish()
            }
        }
        return true
    }
}
