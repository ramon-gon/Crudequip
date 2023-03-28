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
import copernic.cat.kingsleague.databinding.FragmentCrearJugadorBinding
import copernic.cat.kingsleague.models.Equip
import copernic.cat.kingsleague.models.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CrearJugador : Fragment() {
    private var _binding: FragmentCrearJugadorBinding? = null
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

        _binding = FragmentCrearJugadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        jugadors = arrayListOf()

        binding.crearJugador.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {//llegir dades de la base de dades


                    var equips = llegirDades() //Departament introduït per l'usuari

                    //Si hem introduit un codi (és l'identifiacdor del departament, per tant ha de ser obligatori).
                    //En el nostre cas, els altres camps no cal que tinguin contingut
                    if (equips.nom.isNotEmpty() && equips.id.isNotEmpty()) {

                        //Afegim el departament mitjançant eñ mètode afegirDepartament que hem creat nosaltres
                        AfegirJugador(equips)

                    } else if (equips.nom.isNotEmpty()) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(getString(R.string.cal_introduir_nom_al_jugador_alert))
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(getString(R.string.cal_introduir_nom_al_equip_alert))
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }

        binding.btnTornarCrearJugador.setOnClickListener {
            findNavController().navigate(R.id.action_crearJugador_to_menuJugadors)
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
        var nomJ = binding.editjugador.text.toString()
        //Afegim els Tutors introduïts per l'usuari l'atribut treballadors
        jugadors.add(Jugador("", nomJ))

        return Equip(nomJ, nom,0, jugadors)
    }

    fun AfegirJugador(equip: Equip) {
        var nomEquip = binding.spinner?.selectedItem.toString()
        var nomJ = binding.editjugador.text.toString()
        //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.
        var existe =
            bd.collection("Equips").document(nomEquip).collection("Jugadors").document(nomJ)

        existe.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("El jugador no s'ha afegit perquè ja esta creat")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.
                    var existee =
                        bd.collection("Equips").document(nomEquip)

                    existee.get()
                        .addOnSuccessListener { documente ->
                            if (documente.exists()) {
                        bd.collection("Equips").document(nomEquip).collection("Jugadors")
                            .document(nomJ).set(
                                hashMapOf(
                                    "Nom" to equip.jugadors.get(0).nom
                                )
                            )

                            .addOnSuccessListener {
                                findNavController().navigate(R.id.action_crearJugador_to_menuJugadors)
                                val builder = AlertDialog.Builder(requireContext())
                                builder.setMessage(getString(R.string.crear_jugador_alert))
                                builder.setPositiveButton("Aceptar", null)
                                val dialog = builder.create()
                                dialog.show()
                            }
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(R.string.lequip_no_existeix_alert)
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    }

                }


            }

    }
}
}