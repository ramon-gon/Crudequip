package copernic.cat.kingsleague.rvClassificacio.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.inflate
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import copernic.cat.kingsleague.databinding.ActivityMainBinding.inflate
import copernic.cat.kingsleague.databinding.ItemClassiificacioBinding
import copernic.cat.kingsleague.rvClassificacio.Classificacio
import copernic.cat.kingsleague.rvClassificacio.ClassificacioProvider

class ClassificacioAdapter (private val classificacioList: List<Classificacio>): RecyclerView.Adapter<ClassificacioAdapter.ClassificacioViewHolder>() {

    var context: Context? = null

    inner class ClassificacioViewHolder(val binding: ItemClassiificacioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassificacioViewHolder{

        val binding = ItemClassiificacioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassificacioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassificacioViewHolder, position: Int) {
        with(holder) {
            with(classificacioList[position]) {
                binding.nomEquip.text = this.nomEquip
                binding.PuntsEquip.text  = this.puntuacio
            }
        }
        }

    override fun getItemCount(): Int {
            return ClassificacioProvider.classificacioList.size
        }
    }
