package br.com.alexandre_salgueirinho.iquefome_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cliente_cadastro.*

class ClienteCadastro : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private var CHOOSE_IMAGE = 101
    val idBackButton = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_cadastro)

        mToolbar = findViewById(R.id.cadastro_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userRegister()

        cadastro_TextView_Login.setOnClickListener {
            val intent = Intent(this, ClienteLogin::class.java)
            startActivity(intent)
            finish()
        }

        cadastro_CircleImage_ProfileImage.setOnClickListener {
            val intent = Intent (Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode ==0 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val bitmapDrawable = BitmapDrawable(bitmap)

            cadastro_CircleImage_ProfileImage.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun userRegister() {
        cadastro_Button_Cadastrar.setOnClickListener {
            val email = cadastro_EditText_Email.text.toString()
            val password = cadastro_EditText_Password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    cadastro_EditText_Email.requestFocus()
                    Toast.makeText(
                        this,
                        "É necessário informar um email",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (password.isEmpty()) {
                    cadastro_EditText_Password.requestFocus()
                    Toast.makeText(
                        this,
                        "É necessário informar uma senha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
            }.addOnFailureListener {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageChooser() {
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
//        startActivity(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
