package br.com.alexandre_salgueirinho.iquefome_kotlin.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_kotlin.R
import br.com.alexandre_salgueirinho.iquefome_kotlin.model.Usuário
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cliente_cadastro.*
import java.lang.Exception
import java.util.*

class ClienteCadastro : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    var uriImagemSelecionada: Uri? = null

    //#region User Indicante
    lateinit var indicante_Obj: Usuário

    var indicante_Id = ""
    var indicante_Nome = ""
    var indicante_Sobrenome = ""
    var indicante_Email = ""
    var indicante_Password = ""
    var indicante_Celular = ""
    var indicante_Indicado = ""
    var indicante_UrlImagemPerfil = ""
    var indicante_Pontos = ""

    //endregion

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
            Log.d("ClienteCadastroActivity", "Try to show photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("ClienteCadastroActivity", "Photo was selected")

            uriImagemSelecionada = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriImagemSelecionada)

            cadastro_CircleImage_ProfileImage.setImageBitmap(bitmap)
        }
    }

    private fun userRegister() {
        cadastro_Button_Cadastrar.setOnClickListener {
            val email = cadastro_EditText_Email.text.toString()
            val password = cadastro_EditText_Password.text.toString()

            if (validaCampos(email, password)) {
                if (indicante_Id.isNotEmpty())
                    verificaUserIndicado()
//            if (!PhoneNumberUtils.isGlobalPhoneNumber(cliente_Celular) || !Patterns.PHONE.matcher(cliente_Celular).matches()) {
//                Toast.makeText(
//                    this,
//                    "É necessário informar um cliente_Celular certo",
//                    Toast.LENGTH_SHORT
//                ).show()

                Log.d("ClienteCadastroActivity", "Email: $email")
                Log.d("ClienteCadastroActivity", "Password: $password")

                cadastro_ProgressBar.visibility = View.VISIBLE

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            cadastro_ProgressBar.visibility = View.GONE
                            return@addOnCompleteListener
                        }

                        Log.d("ClienteCadastroActivity", "Usuário criado, cliente_Id: ${it.result!!.user.uid}")

                        uploadImage()

                    }.addOnFailureListener {
                        cadastro_ProgressBar.visibility = View.GONE
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
//                Toast.makeText(this, "Erro no preenchimento dos campos \n Verifique.", Toast.LENGTH_SHORT).show()
                Log.d("ClienteCadastroActivity", "Erro no preenchimento dos campos")
            }

        }
    }

    private fun validaCampos(email: String, password: String): Boolean {
        val nome = cadastro_EditText_Nome.text.toString()
        val sobrenome = cadastro_EditText_Sobrenome.text.toString()
        val celular = cadastro_EditText_Celular.text.toString()
        indicante_Id = cadastro_EditText_Indicado.text.toString()

        if (sobrenome.isEmpty() || nome.isEmpty() || email.isEmpty() || password.isEmpty() || uriImagemSelecionada == null) {

            when (sobrenome.isEmpty() || nome.isEmpty() || email.isEmpty() || password.isEmpty() || uriImagemSelecionada == null || celular.isEmpty()) {

                nome.isEmpty() -> {
                    cadastro_EditText_Nome.requestFocus()
                    cadastro_Textfild_Nome.error = "É necessário informar um Nome"
                }

                sobrenome.isEmpty() -> {
                    cadastro_EditText_Sobrenome.requestFocus()
                    cadastro_Textfild_Nome.error = "É necessário informar um Sobrenome"
                }

                email.isEmpty() -> {
                    cadastro_EditText_Email.requestFocus()
                    cadastro_Textfild_Email.error = "É necessário informar um Email"
                }

                password.isEmpty() -> {
                    cadastro_EditText_Password.requestFocus()
                    cadastro_Textfild_Password.error = "É necessário informar uma Senha"
                }

                uriImagemSelecionada == null -> {
                    cadastro_CircleImage_ProfileImage.requestFocus()
                    Toast.makeText(
                        this,
                        "É necessário escolher uma imagem de perfil",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                celular.isEmpty() -> {
                    cadastro_EditText_Celular.requestFocus()
                    cadastro_Textfild_Celular.error = "É necessário informar um Celular"
                }
            }
            return false
        }
        return true
    }

    private fun verificaUserIndicado() {
        val refIndicado = FirebaseDatabase.getInstance().getReference("/users/cadastros/clientes/$indicante_Id")

        refIndicado.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val userIndicado = p0.getValue(Usuário::class.java)

                    if (userIndicado != null) {
                        Log.d("CadastroTESTE", "User Indicação - ${userIndicado.cliente_Nome}")

                        indicante_Obj = userIndicado

                        indicante_Nome = userIndicado.cliente_Nome
                        indicante_Sobrenome = userIndicado.cliente_Sobrenome
                        indicante_Email = userIndicado.cliente_Email
                        indicante_Password = userIndicado.cliente_Password
                        indicante_Celular = userIndicado.cliente_Celular
                        indicante_Indicado = userIndicado.cliente_Indicado
                        indicante_UrlImagemPerfil = userIndicado.cliente_UrlImagemPerfil
                        indicante_Pontos = userIndicado.cliente_Pontos
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@ClienteCadastro, "Erro. ${ex.message}", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        })
    }

    private fun uploadImage() {
        if (uriImagemSelecionada == null) return

        val nomeArquivo = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$nomeArquivo")

        ref.putFile(uriImagemSelecionada!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("ClienteCadastroActivity", "Link do arquivo: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }.addOnFailureListener {
                cadastro_ProgressBar.visibility = View.GONE
                Log.d("ClienteCadastroActivity", "Erro no upload")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDatabase(urlImagemPerfil: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/cadastros/clientes/$uid")

        val user = Usuário(
            uid,
            cadastro_EditText_Nome.text.toString(),
            cadastro_EditText_Sobrenome.text.toString(),
            cadastro_EditText_Email.text.toString(),
            cadastro_EditText_Password.text.toString(),
            cadastro_EditText_Celular.text.toString(),
            indicante_Id,
            urlImagemPerfil,
            "0"
        )

        ref.setValue(user).addOnSuccessListener {
            Log.d("CadastroTESTE", "Finalmente deu boa")
            if (indicante_Id.isNotEmpty()) {
                updateIndicante()
            } else {
                finish()
                cadastro_ProgressBar.visibility = View.GONE
            }
        }.addOnFailureListener {
            cadastro_ProgressBar.visibility = View.GONE
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateIndicante() {

        Log.d("CadastroTESTE", "passou pelo update do user indicante")

        try {
            var pontosDoIndicante = indicante_Obj.cliente_Pontos.toIntOrNull()
            if (pontosDoIndicante != null) {
                pontosDoIndicante += 1
                indicante_Pontos = pontosDoIndicante.toString()

                Log.d("CadastroTESTE", "user indicante - pontos $indicante_Pontos")

                var userInd = Usuário(
                    indicante_Id,
                    indicante_Nome,
                    indicante_Sobrenome,
                    indicante_Email,
                    indicante_Password,
                    indicante_Celular,
                    indicante_Indicado,
                    indicante_UrlImagemPerfil,
                    indicante_Pontos
                )

                val refIndicado = FirebaseDatabase.getInstance().getReference("/users/cadastros/clientes/$indicante_Id")

                refIndicado.setValue(userInd).addOnSuccessListener {
                    Log.d("CadastroTESTE", "atualizado o indicante")
                    finish()
                    cadastro_ProgressBar.visibility = View.GONE
                }.addOnFailureListener {
                    cadastro_ProgressBar.visibility = View.GONE
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }

            } else
                Toast.makeText(this, "Erro - pontosIndicante é nulo", Toast.LENGTH_SHORT).show()


        } catch (ex: Exception) {
            Log.d("CadastroTESTE", "user indicante - catch")
            Toast.makeText(this, "Erro - ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}