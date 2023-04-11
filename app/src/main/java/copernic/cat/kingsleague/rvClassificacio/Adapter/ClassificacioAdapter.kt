package copernic.cat.kingsleague.rvClassificacio.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import copernic.cat.kingsleague.databinding.ItemClassiificacioBinding
import copernic.cat.kingsleague.rvClassificacio.Classificacio
import copernic.cat.kingsleague.rvClassificacio.ClassificacioProvider
import copernic.cat.kingsleague.ui.Utils
import copernic.cat.kingsleague.ui.activity.Menu
import copernic.cat.kingsleague.ui.activity.MenuA
import copernic.cat.kingsleague.ui.fragment.administrador.ClassificacioAdminDirections
import copernic.cat.kingsleague.ui.fragment.usuari.ClassificacioDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClassificacioAdapter (private val classificacioList: List<Classificacio>): RecyclerView.Adapter<ClassificacioAdapter.ClassificacioViewHolder>() {

    var context: Context? = null
    private var bd = FirebaseFirestore.getInstance()
    private val utils = Utils()

    inner class ClassificacioViewHolder(val binding: ItemClassiificacioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassificacioViewHolder {

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
                binding.PuntsEquip.text = this.puntuacio
            }
            holder.itemView.setOnClickListener { view ->

                navigation(view, classificacioList[position].nomEquip)
            }
        }
    }

    override fun getItemCount(): Int {
        return ClassificacioProvider.classificacioList.size
    }

    fun navigation(view: View, equip: String) {

        var ola = bd.collection("Usuaris").document(utils.getCorreoUserActural())
        ola.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getBoolean("admin") == true) {


                val action =
                    ClassificacioAdminDirections.actionClassificacioAdminToEquipsSafeArgs(equip)
                view.findNavController().navigate(action)

            } else {
                val action = ClassificacioDirections.actionClassificacio2ToEquipsSafeArgs2(equip)
                view.findNavController().navigate(action)

            }
        }
    }
}
