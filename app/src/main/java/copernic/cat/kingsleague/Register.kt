package copernic.cat.kingsleague

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.ActivityRegisterBinding
import copernic.cat.kingsleague.models.Usuari
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var bd = FirebaseFirestore.getInstance()
    private val utils = Utils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //esconde la ActionBar
        if (supportActionBar != null)
            supportActionBar!!.hide()

        auth = Firebase.auth
        //array tutors


        binding.registreGuardar.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Unconfined) {//llegir dades de la base de dades

                    var correu = binding.registreEditnomUsuari.text.toString()
                    var contrasenya = binding.registreEditContrasenya.text.toString()
                    var contrasenyaBis = binding.registreEditRepetirContrasenya.text.toString()

                    campEsBuit(correu,contrasenya,contrasenyaBis)

                    if (contrasenya.equals(contrasenyaBis) && campEsBuit(
                            correu,
                            contrasenya,
                            contrasenyaBis
                        )
                    ) {

                        var usuaris = llegirDades()


                        //Si hem introduit un codi (és l'identifiacdor del departament, per tant ha de ser obligatori).
                        //En el nostre cas, els altres camps no cal que tinguin contingut

                        if (!utils.isValidEmail(usuaris.gmail)) {
                            binding.registreEditnomUsuari.error =
                                "email incorrecte!!"
                        } else if (usuaris.gmail.isNotEmpty()) {
                            //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.

                            //Afegim el departament mitjançant eñ mètode afegirDepartament que hem creat nosaltres

                            registrar(correu, contrasenya, usuaris)

                        }

                    }
                }
            }
        }
        binding.registreCancelar.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    fun campEsBuit(correu: String, contrasenya: String, contrasenyaBis: String): Boolean {

        if (correu.isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Cal introduir un correu")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()

        } else if (contrasenya.isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Cal introduir una contrasenya")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        } else if (contrasenyaBis.isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Cal introduir la contrasenya repetida")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        } else if (contrasenyaBis != contrasenya) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Cal introduir la contrasenya correcta")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        }

        return correu.isNotEmpty() && contrasenya.isNotEmpty() && contrasenyaBis.isNotEmpty()
    }

    //Funció que llegeix les dades introduïdes per un usuari i retorna el departament instanciat amb aquestes
    //dades.
    fun llegirDades(): Usuari {
        //Guardem les dades introduïdes per l'usuari
        var nomUsuari = binding.registreEditnomUsuari.text.toString()


        //Afegim els Tutors introduïts per l'usuari a l'atribut treballadors
        return Usuari(nomUsuari)
    }

    fun afegirUsuari(usuaris: Usuari) {

        //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.
        bd.collection("Usuaris").document(usuaris.gmail).set(usuaris).addOnSuccessListener {
        }
            .addOnFailureListener { //No s'ha afegit el departament...
                /* val builder = AlertDialog.Builder(this)
                 builder.setMessage("NO SE AÑADE NADA")
                 builder.setPositiveButton("Aceptar", null)
                 val dialog = builder.create()
                 dialog.show()*/
            }
    }

    //Funció per registrar a un usuari mitjançant Firebase Authentication
    fun registrar(correu: String, contrasenya: String, usuaris: Usuari) {
        auth.createUserWithEmailAndPassword(correu, contrasenya)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //afegir les dades del usuaris al Firestore
                    afegirUsuari(usuaris)
                    startActivity(Intent(this, Menu::class.java))
                    finish()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("El Registre ha fallat")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }

}