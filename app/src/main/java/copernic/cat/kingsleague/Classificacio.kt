package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.FragmentClassificacioAdminBinding
import copernic.cat.kingsleague.databinding.FragmentClassificacioBinding
import copernic.cat.kingsleague.databinding.FragmentJugadorsBinding
import copernic.cat.kingsleague.rvClassificacio.Adapter.ClassificacioAdapter
import copernic.cat.kingsleague.rvClassificacio.ClassificacioProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.C

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Classificacio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Classificacio : Fragment() {
    private var _binding: FragmentClassificacioBinding? = null
    private val binding get() = _binding!!
    private var bd = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClassificacioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
        binding.btnTornarClassificacio.setOnClickListener {
            findNavController().navigate(R.id.action_classificacio2_to_menuUsuari)
        }
    }

    private fun initRecyclerView(view: View) {
        ClassificacioProvider.classificacioList.clear()

        if (ClassificacioProvider.classificacioList.isEmpty()) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { // Li  diem que executi amb el fil d'entrada i sortida, IO
                    rellenarCircularsProvider()
                }
            }
        } else {
            binding.rvClassificacio.layoutManager = LinearLayoutManager(context)
            binding.rvClassificacio.adapter =
                ClassificacioAdapter(ClassificacioProvider.classificacioList.toList())
        }


    }

    private fun rellenarCircularsProvider() {


        lifecycleScope.launch {
            var resultatConsulta=bd.collection("Equips").orderBy("Puntuacio", Query.Direction.DESCENDING).get().await()
            // Fem la cerca a la base de dades
            if(!resultatConsulta.isEmpty) {// Si troba resultat
                for (document in resultatConsulta) {//  Creem un bucle perque mostri tots els atributs de l'objecte
                    var count=0
                    val wallItem = copernic.cat.kingsleague.rvClassificacio.Classificacio(// objecte
                        nomEquip = document["Nom"].toString(),
                        puntuacio = document["Puntuacio"].toString(), // atribut
                    )
                    if (ClassificacioProvider.classificacioList.isEmpty()) {// Si el provider esta buit
                        ClassificacioProvider.classificacioList.add(wallItem) // Afegeix un item
                    } else {
                        for (i in ClassificacioProvider.classificacioList) { // Fem un bucle fins que no hi hagin mes items
                            if (wallItem.nomEquip == i.nomEquip) { // si l'atribut del item es igual a l'atribut del bucle
                                count++// incrementem  el contador
                            }
                        }
                        if (count == 0) { // si el contador es  0 vol dir que ja no hi ha mes items
                            ClassificacioProvider.classificacioList.add(wallItem) // afegim els items restants
                        }
                    }
                }
                binding.rvClassificacio.layoutManager = LinearLayoutManager(context)
                binding.rvClassificacio.adapter =
                    ClassificacioAdapter(ClassificacioProvider.classificacioList.toList())

            }

        }
    }
}
