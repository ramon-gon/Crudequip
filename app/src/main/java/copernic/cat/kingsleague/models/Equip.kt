package copernic.cat.kingsleague.models

import com.google.firebase.firestore.Exclude


data class Equip(
    @Exclude val id:String,
    val nom:String,
    var puntuacio:Int,
    val jugadors:ArrayList<Jugador>
)