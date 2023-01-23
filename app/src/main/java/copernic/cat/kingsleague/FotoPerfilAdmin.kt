package copernic.cat.kingsleague

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
import androidx.navigation.fragment.findNavController
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
 * Use the [FotoPerfilAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FotoPerfilAdmin: Fragment() {
    private var photoSelectedUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private val utils = Utils()
    private var storage = FirebaseStorage.getInstance()
    private var storageRef = storage.getReference().child("image/imatges").child(".jpeg")

    private var _binding: FragmentFotoPerfilAdminBinding? = null
    private val binding get() = _binding!!

    /**
     * Método que se llama cuando se crea la actividad. Se encarga de inicializar la autenticación de Firebase.
     * @param savedInstanceState guarda el estado de la aplicación en caso de cambio de configuración
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            auth = Firebase.auth
        }
    }

    /**
     * Crea y devuelve la vista del fragmento de autorizaciones de administrador.
     * @param inflater El inflador de layout utilizado para inflar la vista.
     * @param container El contenedor padre de la vista.
     * @param savedInstanceState El estado previamente guardado del fragmento.
     * @return La vista del fragmento de autoritzacionsUsuari.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFotoPerfilAdminBinding.inflate(inflater)
        return binding.root
    }

    /**
     * La función onViewCreated se ejecuta después de onCreateView, y se utiliza para inicializar elementos de la interfaz de usuario.
     * En este caso, se está utilizando el método launch para iniciar una tarea en segundo plano para cargar la
     * imagen del perfil del usuario si ya existe. Además, se establecen los listeners en los botones imgButtonBuscar
     * y button4, para abrir un selector de imágenes y guardar la imagen seleccionada respectivamente.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgButtonBuscar.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    obrirImatge()
                }
            }
            //metode per afegir l'imatge
        }
        binding.btnGuardar.setOnClickListener{
            findNavController().navigate(R.id.action_fotoPerfil2_to_configuracioAdmin)
        }
        binding.btnCancelarfotoPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_fotoPerfil2_to_configuracioAdmin)
        }
        //metode per carregar l'imatge si ya esta creada
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                carregarImatge()
            }

        }

    }

    /**
     * Esta función es un registro para el resultado de la actividad que se encarga de abrir la cámara del dispositivo
     * para poder seleccionar una imagen. Si el resultado de la actividad es exitoso, se asigna el URI de la imagen seleccionada
     * y se llama al método "afegirImatge" para subir la imagen al storage de Firebase.
     */
    private val guardarImgCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoSelectedUri = result.data?.data //Assignem l'URI de la imatge
                //metode per afegir l'imatge
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        afegirImatge()
                    }
                }
            }
        }

    /**
     * Esta función se encarga de añadir una imagen seleccionada al storage de Firebase, asignando a la imagen un
     * nombre con el correo del usuario actual. Utiliza el método putFile de FirebaseStorage para subir la imagen
     * seleccionada y asignarle el nombre con el correo del usuario actual. Además, se encarga de cargar la imagen
     * en el ImageView correspondiente.
     */
    private suspend fun afegirImatge() {
        lifecycleScope.launch {
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
    /**
     * Método que se encarga de cargar la imagen del perfil del usuario en caso de ya estar creada.
     * Se utiliza el método getFile de la clase FirebaseStorage para obtener la imagen almacenada en el servidor.
     * Se utiliza un archivo temporal para almacenar la imagen obtenida y se convierte en un bitmap para poder mostrarla en la interfaz.
     * En caso de error se maneja dentro del catch.
     */
    suspend fun carregarImatge() {
        var correo = utils.getCorreoUserActural()
        val storageRef =
            FirebaseStorage.getInstance().reference.child("image/imatges/$correo.jpeg")
        val localfile = File.createTempFile("tempImage", "jpeg")
        val task = storageRef.getFile(localfile).await()
        try {
            (task)
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imgFotoDePerfil.setImageBitmap(bitmap)
        } catch (e: Exception) {
            // maneja el error aquí
        }
    }

    /**
     * Función que permite abrir la galería de imágenes del dispositivo para seleccionar una imagen.
     * Utiliza el método launch de la clase guardarImgCamera para abrir la galería y seleccionar una imagen.
     */
    fun obrirImatge() {
        //Obrim la galeria per seleccionar la imatge  //Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        guardarImgCamera.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }
}