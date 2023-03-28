package copernic.cat.kingsleague.ui.fragment.administrador

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentEliminarEquipsBinding
import copernic.cat.kingsleague.models.Equip
import copernic.cat.kingsleague.models.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EliminarEquips : Fragment() {
    private var _binding: FragmentEliminarEquipsBinding? = null
    private val binding get() = _binding!!
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var jugadors: ArrayList<Jugador>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEliminarEquipsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        jugadors = arrayListOf()


        binding.eliminarEquip.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {//llegir dades de la base de dades


                    var equip = llegirDades() //Departament introduït per l'usuari


                    //Si hem introduit un codi (és l'identifiacdor del departament, per tant ha de ser obligatori).
                    //En el nostre cas, els altres camps no cal que tinguin contingut
                    if (equip.nom.isNotEmpty()) {

                        //Afegim el departament mitjançant eñ mètode afegirDepartament que hem creat nosaltres
                        EliminarrEquip(equip)


                        findNavController().navigate(R.id.action_eliminarEquips_to_menuEquips)


                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(getString(R.string.introduir_nom_equip))
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
        binding.btnTornarEliminarEquips.setOnClickListener() {
            findNavController().navigate(R.id.action_eliminarEquips_to_menuEquips)
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

    fun llegirDades(): Equip {
        //Guardem les dades introduïdes per l'usuari
        var nom = binding.spinner?.selectedItem.toString()

        //Afegim els Tutors introduïts per l'usuari l'atribut treballadors
        jugadors.add(Jugador("", ""))

        return Equip(nom , nom, 0, jugadors)
    }

    fun EliminarrEquip(equip: Equip) {
        var nom = binding.spinner?.selectedItem.toString()


        var existe =
            bd.collection("Equips").document(nom)

        existe.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    bd.collection("Equips").document(nom).collection("Jugadors").get()
                        .addOnSuccessListener { documents ->
                            for (documentc in documents) {
                                documentc.reference.delete()
                            }
                        }
                    bd.collection("Equips").document(nom).delete()
                        .addOnSuccessListener {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage(getString(R.string.equip_eliminat_alert))
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(R.string.equip_no_existeix_alert)
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }
}