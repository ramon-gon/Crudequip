package copernic.cat.kingsleague.models

import com.google.firebase.firestore.Exclude
import copernic.cat.kingsleague.Jugadors

data class Equip(
    @Exclude val id:String,
    val nom:String,
    var puntuacio:Int,
    val jugadors:ArrayList<Jugador>
)