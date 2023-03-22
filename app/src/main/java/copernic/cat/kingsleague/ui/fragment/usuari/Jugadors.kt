package copernic.cat.kingsleague.ui.fragment.usuari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentJugadorsAdminBinding
import copernic.cat.kingsleague.databinding.FragmentJugadorsBinding
import copernic.cat.kingsleague.rvClassificacio.Adapter.ClassificacioAdapter
import copernic.cat.kingsleague.rvClassificacio.ClassificacioProvider
import copernic.cat.kingsleague.rvJugadors.Adapter.JugadorsAdapter
import copernic.cat.kingsleague.rvJugadors.JugadorsProvider
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
 * Use the [Jugadors.newInstance] factory method to
 * create an instance of this fragment.
 */
class Jugadors : Fragment() {
    private var _binding: FragmentJugadorsBinding? = null
    private val binding get() = _binding!!
    val bd = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJugadorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTornarJugadors.setOnClickListener {
            findNavController().navigate(R.id.action_jugadors_to_menuUsuari)
        }
        binding.btnVeureJugadors.setOnClickListener {
            try {

                var nomEquip = binding.spinner?.selectedItem.toString()

                var existe =
                    bd.collection("Equips").document(nomEquip)

                existe.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            initRecyclerView(view)


                        } else {
                            initRecyclerView(view)
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage(R.string.equip_no_existeix_alert)
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }
                    }

            } catch (e: Exception) {

                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.introduir_nom_equip_alert)
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()

            }
        }

        bd.collection("Equips")
            .get()
            .addOnSuccessListener { documents ->
                val nombres = ArrayList<String>()
                nombres.add("-")
                for (document in documents) {
                    val nombre = document.getString("Nom")
                    nombres.add(nombre!!)
                }

                // 3. Crear un ArrayAdapter en la actividad o fragmento correspondiente
                val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, nombres) }

                // 4. Asignar el ArrayAdapter al spinner y establecer el comportamiento correspondiente
                val spinner= binding.spinner
                spinner?.adapter = adapter

                spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val nombreSeleccionado = parent.getItemAtPosition(position).toString()
                        // Aquí se puede establecer el comportamiento correspondiente al seleccionar un elemento del spinner
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // No se seleccionó ningún elemento del spinner
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al obtener los datos de la base de datos
            }}



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
        var nomEquip = binding.spinner?.selectedItem.toString()


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
