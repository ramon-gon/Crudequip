package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.FragmentCrearEquipsBinding
import copernic.cat.kingsleague.databinding.FragmentMenuAdminBinding
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
 * Use the [CrearEquips.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearEquips : Fragment() {
    private var _binding: FragmentCrearEquipsBinding? = null
    private val binding get() = _binding!!
    private var bd= FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var jugadors: ArrayList<Jugador>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCrearEquipsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        jugadors = arrayListOf()


        binding.crearEquip.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Unconfined) {//llegir dades de la base de dades


                    var equip = llegirDades() //Departament introduït per l'usuari

                    //Si hem introduit un codi (és l'identifiacdor del departament, per tant ha de ser obligatori).
                    //En el nostre cas, els altres camps no cal que tinguin contingut
                    if (equip.nom.isNotEmpty()) {

                        //Afegim el departament mitjançant eñ mètode afegirDepartament que hem creat nosaltres
                        AfegirEquip(equip)


                        findNavController().navigate(R.id.action_crearEquips_to_menuEquips)


                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Cal introduïr un nom a l'equip")
                        builder.setPositiveButton("Aceptar", null)
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
        binding.btnTornarCrearEquips.setOnClickListener() {
            findNavController().navigate(R.id.action_crearEquips_to_menuEquips)
        }
    }

    fun llegirDades(): Equip {
        //Guardem les dades introduïdes per l'usuari
        var nom = binding.editNomEquip.text.toString()

        //Afegim els Tutors introduïts per l'usuari l'atribut treballadors
        jugadors.add(Jugador("",""))

        return Equip(nom, nom, 0, jugadors)
    }

    fun AfegirEquip(equip: Equip) {
        var nom = binding.editNomEquip.text.toString()

        var existe =
            bd.collection("Equips").document(nom)

        existe.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("L'equip no s'ha afegit perquè ja esta creat")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    //Afegim una subcolecció igual que afegim una col.lecció però penjant de la col.lecció on està inclosa.
                    bd.collection("Equips").document(equip.nom).set(
                        hashMapOf(
                            "Nom" to equip.nom,
                            "Puntuacio" to equip.puntuacio
                        )
                    )
                        .addOnSuccessListener {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage("L'equip s'ha afegit correctament")
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }
                        .addOnFailureListener { //No s'ha afegit el departament...
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setMessage("L'equip no s'ha afegit")
                            builder.setPositiveButton("Aceptar", null)
                            val dialog = builder.create()
                            dialog.show()
                        }
                }
            }
    }
}