package copernic.cat.kingsleague.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentModificarClassificacioBinding
import copernic.cat.kingsleague.models.Equip
import copernic.cat.kingsleague.models.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModificarClassificacio.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModificarClassificacio : Fragment() {
    private var _binding: FragmentModificarClassificacioBinding? = null
    private val binding get() = _binding!!
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var jugadors: ArrayList<Jugador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentModificarClassificacioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        jugadors = arrayListOf()

        binding.ModificarClassificacio.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {//llegir dades de la base de dades

                    try {


                    var equips = llegirDades() //Departament introduït per l'usuari
                    //Si hem introduit un codi (és l'identifiacdor del departament, per tant ha de ser obligatori).
                    //En el nostre cas, els altres camps no cal que tinguin contingut
                        if (equips.nom.isNotEmpty() && equips.id.isNotEmpty()) {

                            //Afegim el departament mitjançant eñ mètode afegirDepartament que hem creat nosaltres
                            AfegirPuntuacio(equips)


                        } else {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage(R.string.introduir_nom_equip_alert)
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }

                    } catch (e: Exception){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(getString(R.string.introduir_puntuacio_alert))
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    }

                }
            }
        }

        binding.btnTornarModificarClassificacio.setOnClickListener {
            findNavController().navigate(R.id.action_modificarClassificacio_to_menuEquips)
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
        var puntuacio= binding.editPuntuacio.text.toString()
        //Afegim els Tutors introduïts per l'usuari l'atribut treballadors
        jugadors.add(Jugador("",""))

        return Equip(puntuacio, nom, puntuacio.toInt(), jugadors)
    }

    fun AfegirPuntuacio(equips:Equip) {
        var nomEquip = binding.spinner?.selectedItem.toString()
        var puntuacio = binding.editPuntuacio.text.toString()
        //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.
        var existe =
            bd.collection("Equips").document(nomEquip)

        existe.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    bd.collection("Equips")//Col.lecció
                        .document(nomEquip).update(

                            "Puntuacio", equips.puntuacio,
                        ) //Subcol.lecció

                        .addOnSuccessListener {
                            findNavController().navigate(R.id.action_modificarClassificacio_to_menuEquips)
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage(getString(R.string.la_puntuacio_sha_modificat_alert))
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }


                                }
                else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(R.string.equip_no_existeix_alert)
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                }

                        }


                }

            }
