package copernic.cat.kingsleague.models

import copernic.cat.kingsleague.Jugadors

data class Equip(
    val nom:String,
    val puntuacio:String,
    val jugadors:ArrayList<Jugador>
)