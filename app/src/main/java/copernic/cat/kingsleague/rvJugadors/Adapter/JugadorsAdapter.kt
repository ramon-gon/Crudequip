package copernic.cat.kingsleague.rvJugadors.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import copernic.cat.kingsleague.databinding.ItemJugadorsBinding
import copernic.cat.kingsleague.rvJugadors.Jugadors
import copernic.cat.kingsleague.rvJugadors.JugadorsProvider


class JugadorsAdapter  (private val jugadorsList: List<Jugadors>): RecyclerView.Adapter<JugadorsAdapter.JugadorsViewHolder>() {

    var context: Context? = null

    inner class JugadorsViewHolder(val binding: ItemJugadorsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorsViewHolder{

        val binding = ItemJugadorsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JugadorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JugadorsAdapter.JugadorsViewHolder, position: Int) {
        with(holder) {
            with(jugadorsList[position]) {
                binding.nomJugador.text = this.nom
            }
        }
    }

    override fun getItemCount(): Int {
        return JugadorsProvider.jugadorsList.size
    }
}
