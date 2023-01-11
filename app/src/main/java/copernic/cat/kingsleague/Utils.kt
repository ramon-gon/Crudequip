package copernic.cat.kingsleague

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class Utils {
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    //funcion que devuelve una cadena de texto que representa el correo electrónico del usuario actual.
    fun getCorreoUserActural(): String {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        return currentUser!!.email.toString()
    }

    companion object {
        private lateinit var auth: FirebaseAuth

        fun notification(nom: String, tipo: String, context: Context) {
            val notification = NotificationCompat.Builder(context,"1").also{ noti ->
                noti.setContentTitle("$tipo creada")
                noti.setContentText("S'ha creat l' $tipo $nom")
                noti.setSmallIcon(R.drawable.logo)
            }.build()
            val notificationManageer = NotificationManagerCompat.from(context)
            notificationManageer.notify(1,notification)
        }

        fun getCorreoUserActural(): String {
            auth = Firebase.auth
            val currentUser = auth.currentUser
            return currentUser!!.email.toString()
        }
    }

    //validar con regex correo electronico
    fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "^([\\w]*[\\w\\.]*(?!\\.)@gmail.com)"
        )
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    //validar con regex numero de telefono
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Pattern.compile("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*\$")
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
    //validar con regex la calle
    fun isValidStreetAddress(streetAddress: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z0-9\\s,'-]*\$")
        val matcher = pattern.matcher(streetAddress)
        return matcher.matches()
    }
    //validar con regex que es un nombre y no contenga numeros ni simbolos
    fun isAlpha(string: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z]*\$")
        val matcher = pattern.matcher(string)
        return matcher.matches()
    }


}