package copernic.cat.kingsleague

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.LoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var correu: String
    private var bd = FirebaseFirestore.getInstance()
    private val utils = Utils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //esconde la ActionBar
        if (supportActionBar != null)
            supportActionBar!!.hide()

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val builder = AlertDialog.Builder(this)
        auth = Firebase.auth


        //Implementem els listeners per quan l'usuari cliqui un dels botons

        binding.buttonLoginAccedir.setOnClickListener {

            //Guardem les dades introduïdes per l'usuari en el formulari mitjançant text i les transformem amb un String (toString())
            correu = binding.textInputEditTextLoginGmail.text.toString().replace(" ", "")
            var contrasenya =
                binding.textInputEditTextLoginPassword.text.toString().replace(" ", "")

            //Comprovem que els camps no estan buit
            if (correu.isNotEmpty() && contrasenya.isNotEmpty()) {
                //Loguinem a l'usuari mitjançant la funció loguinar creada per nosaltres
                loguinar(correu, contrasenya)
            } else if (correu.isEmpty()) {
                binding.textInputEditTextLoginGmail.error = "El correo esta vacio"
            } else {
                binding.textInputEditTextLoginPassword.error =
                    "El campo de la contrasenaya esta vacia"
            }
        }
        //boton pagina de registrarse
        binding.buttonLoginRegister.setOnClickListener {
            //Anem a l'activity del registre
            startActivity(Intent(this, Register::class.java))
            finish()
        }
        binding.recuperarContrasenya.setOnClickListener {
            //Anem a l'activity del registre
            startActivity(Intent(this, RecuperarContrasenya::class.java))
            finish()
        }
    }

    override fun onStart() {

        super.onStart() //Cridem al la funció onStart() perquè ens mostri per pantalla l'activity
        //currentUser és un atribut de la classe FirebaseAuth que guarda l'usuari autenticat. Si aquest no està autenticat, el seu valor serà null.
        val currentUser = auth.currentUser
        auth = Firebase.auth
        if (currentUser != null) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                   isAdmin()
                }
            }

        }
    }

    //Funció per loguinar a un usuari mitjançant Firebase Authentication
    fun loguinar(correu: String, contrasenya: String) {
        //Loguinem a l'usuari

        auth.signInWithEmailAndPassword(correu, contrasenya)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //El loguin (task) s'ha completat amb exit...
                    //Anem al mainActivity des d'aquesta pantalla
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            isAdmin()
                        }
                    }
                    //finish() //Alliberem memòria un cop finalitzada aquesta tasca.
                } else { //El loguin (task) ha fallat...
                    //Mostrem un missatge a l'usuari mitjançant un Toast
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.correu_error))
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }

    suspend fun isAdmin() {

            var ola = bd.collection("Usuaris").document(utils.getCorreoUserActural()).get().await()
            if (ola.get("admin") as Boolean) {
                val intentAdmin = Intent(applicationContext, MenuA::class.java)

                startActivity(intentAdmin)
                //generamos una animacion que hemos generado en la carpeta res/anim llamada zoom_in
            } else {
                val intent = Intent(applicationContext, Menu::class.java)

                startActivity(intent)
        }
    }
}