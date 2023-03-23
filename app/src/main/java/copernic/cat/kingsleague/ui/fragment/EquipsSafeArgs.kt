package copernic.cat.kingsleague.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentEquipsSafeArgsBinding
import copernic.cat.kingsleague.databinding.FragmentJugadorsAdminBinding
import copernic.cat.kingsleague.rvJugadors.Adapter.JugadorsAdapter
import copernic.cat.kingsleague.rvJugadors.JugadorsProvider
import copernic.cat.kingsleague.ui.Utils
import copernic.cat.kingsleague.ui.fragment.administrador.ClassificacioAdminDirections
import copernic.cat.kingsleague.ui.fragment.usuari.ClassificacioDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EquipsSafeArgs.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipsSafeArgs : Fragment() {
    private var _binding: FragmentEquipsSafeArgsBinding? = null
    private val binding get() = _binding!!
    val bd = FirebaseFirestore.getInstance()
    private val utils = Utils()
    private val args: EquipsSafeArgsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquipsSafeArgsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

        val nomEquip= args.nomequip
        binding.TitolEquip.text=nomEquip

           val buscar= bd.collection("Equips").document(nomEquip)
        buscar.get().addOnSuccessListener { documentSnapshot ->
            val puntuacio=documentSnapshot.getLong("Puntuacio").toString()
            binding.puntuacionumero.text=puntuacio
        }

        binding.btnTornarEquipsSafeArgs.setOnClickListener {
            var ola = bd.collection("Usuaris").document(utils.getCorreoUserActural())
            ola.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.getBoolean("admin") == true) {


                    view.findNavController()
                        .navigate(R.id.action_equipsSafeArgs_to_classificacioAdmin)

                } else {
                    view.findNavController().navigate(R.id.action_equipsSafeArgs2_to_classificacio2)

                }
            }
        }
    }
    private fun initRecyclerView(view: View) {

        if (JugadorsProvider.jugadorsList.isEmpty()) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { // Li  diem que executi amb el fil d'entrada i sortida, IO
                    rellenarCircularsProvider()
                }
            }
        } else {
            JugadorsProvider.jugadorsList.clear()
            if (JugadorsProvider.jugadorsList.isEmpty()) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) { // Li  diem que executi amb el fil d'entrada i sortida, IO
                        rellenarCircularsProvider()
                    }
                }
            }
            binding.rvJugadors.layoutManager = LinearLayoutManager(context)
            binding.rvJugadors.adapter =
                JugadorsAdapter(JugadorsProvider.jugadorsList.toList())
        }


    }

    private fun rellenarCircularsProvider() {
        val nomEquip= args.nomequip

        lifecycleScope.launch {
            var resultatConsulta=
                bd.collection("Equips").document(nomEquip).collection("Jugadors").get().await()
            // Fem la cerca a la base de dades
            if(!resultatConsulta.isEmpty) {// Si troba resultat
                for (document in resultatConsulta) {//  Creem un bucle perque mostri tots els atributs de l'objecte
                    var count=0
                    val wallItem = copernic.cat.kingsleague.rvJugadors.Jugadors(// objecte
                        nom = document["Nom"].toString(),
                    )
                    if (JugadorsProvider.jugadorsList.isEmpty()) {// Si el provider esta buit
                        JugadorsProvider.jugadorsList.add(wallItem) // Afegeix un item
                    } else {
                        for (i in JugadorsProvider.jugadorsList) { // Fem un bucle fins que no hi hagin mes items
                            if (wallItem.nom == i.nom) { // si l'atribut del item es igual a l'atribut del bucle
                                count++// incrementem  el contador
                            }
                        }
                        if (count == 0) { // si el contador es  0 vol dir que ja no hi ha mes items
                            JugadorsProvider.jugadorsList.add(wallItem) // afegim els items restants
                        }
                    }
                }
                binding.rvJugadors.layoutManager = LinearLayoutManager(context)
                binding.rvJugadors.adapter =
                    JugadorsAdapter(JugadorsProvider.jugadorsList.toList())

            }

        }
    }
}
