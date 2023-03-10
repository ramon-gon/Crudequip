package copernic.cat.kingsleague

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.RecuperarContrasenyaBinding


class RecuperarContrasenya : AppCompatActivity() {

    /*Declarem els atributs que inicialitzarem més tard (lateinit) per guardar els components del formulari del activity_login.
      És recomanable per seguretat i facilitar-nos la feina que els noms d'aquests atributs siguin els mateixos que els noms dels
      id dels components del fitxer xml*/

    private lateinit var correuRecuperarContrasenya: EditText
    private lateinit var botoRecuperarContrasenya: Button
    private lateinit var  botocancel: Button

    private lateinit var binding: RecuperarContrasenyaBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = RecuperarContrasenyaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicialitzem la variable de tipus FirebaseAuth amb una instància d'aquesta classe
        auth = Firebase.auth

        //Implementem els listeners per quan l'usuari cliqui un dels botons

        binding.buttonRecuperarContrasenya.setOnClickListener {
            val correu = binding.textInputEditTextEmail.text.toString() //Guardem el correu introduït per l'usuari

            if (correu.isNotEmpty()) {
                resetContrasenya(correu)
            }
        }
        binding.buttonCancelarRecuperarcontrasenya.setOnClickListener {
            //Anem al mainActivity des d'aquesta pantalla
            startActivity(Intent(this, Login::class.java))
            finish() //Alliberem memòria un cop finalitzada aquesta tasca.

        }
    }

    private fun resetContrasenya (email: String) {
        auth.setLanguageCode("ca")
        auth.sendPasswordResetEmail(email).addOnCompleteListener() { task ->
            if (task.isSuccessful)
            {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("S'ha enviat un correu per canviar la contrasenya")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
                //Anem al mainActivity des d'aquesta pantalla
                startActivity(Intent(this, Login::class.java))
                finish() //Alliberem memòria un cop finalitzada aquesta tasca.


            }
            else
            {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("No s'ha pogut enviar el correu")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()

            }
        }
    }

}

