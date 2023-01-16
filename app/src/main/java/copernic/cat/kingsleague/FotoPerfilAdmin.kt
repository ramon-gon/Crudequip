
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import copernic.cat.kingsleague.Utils
import copernic.cat.kingsleague.databinding.FragmentFotoPerfilAdminBinding
import copernic.cat.kingsleague.databinding.FragmentFotoPerfilBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FotoPerfilUsuari.newInstance] factory method to
 * create an instance of this fragment.
 */
class FotoPerfilAdmin: Fragment() {
    private var photoSelectedUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private val utils = Utils()
    private var storage = FirebaseStorage.getInstance()
    private var storageRef = storage.getReference().child("image/imatges").child(".jpeg")

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentFotoPerfilAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            auth = Firebase.auth
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFotoPerfilAdminBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //metode per carregar l'imatge si ya esta creada
        binding.imgButtonBuscar.setOnClickListener {
            lifecycleScope.launch {
                carregarImatge()
            }
        }
        //metode per afegir l'imatge
        binding.imgButtonBuscar.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    afegirImatge()
                }
            }
        }
    }

    private val guardarImgCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoSelectedUri = result.data?.data //Assignem l'URI de la imatge
            }
        }

    private suspend fun afegirImatge() {
        lifecycleScope.launch {
            //Obrim la galeria per seleccionar la imatge  //Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            guardarImgCamera.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
            var correo = utils.getCorreoUserActural()
            storageRef = storage.reference.child("image/imatges").child("$correo.jpeg")
            //Afegim la imatge seleccionada a storage
            photoSelectedUri?.let { uri -> //Hem seleccionat una imatge...
                //Afegim (pujem) la imatge que hem seleccionat mitjançant el mètode putFile de la classe FirebasStorage, passant-li com a
                //paràmetre l'URI de la imatge.
                storageRef.putFile(uri).await()
                val storageRef =
                    FirebaseStorage.getInstance().reference.child("image/imatges/$correo.jpeg")
                val localfile = File.createTempFile("tempImage", "jpeg")
                storageRef.getFile(localfile).await()
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imgFotoDePerfil.setImageBitmap(bitmap)
            }
        }
    }

    suspend fun carregarImatge() {
        lifecycleScope.launch {
            var correo = utils.getCorreoUserActural()
            val storageRef =
                FirebaseStorage.getInstance().reference.child("image/imatges/$correo.jpeg")
            val localfile = File.createTempFile("tempImage", "jpeg")
            val task = storageRef.getFile(localfile)
            try {
                Tasks.await(task)
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imgFotoDePerfil.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // maneja el error aquí
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FotoPerfilUsuari.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FotoPerfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}