package copernic.cat.kingsleague.models

import com.google.firebase.firestore.Exclude

data class Jugador(
    @Exclude val nomEquip:String,
    val nom:String
)
