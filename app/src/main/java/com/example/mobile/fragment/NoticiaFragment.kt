package com.example.mobile.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.mobile.HomeActivity
import com.example.mobile.R
import com.example.mobile.api.model.Noticia
import com.example.mobile.api.model.NoticiaLerMaisTarde
import com.example.mobile.api.model.Origem
import com.example.mobile.api.model.Utilizador
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoticiaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoticiaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var backFragment: Fragment = Fragment()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val alertDialog : AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_noticia, container, false)
        val title = arguments?.getString("title")
        val source = arguments?.getString("source")
        val conteudo = arguments?.getString("conteudo")
        val image = arguments?.getString("image")
        val author = arguments?.getString("author")
        val description = arguments?.getString("description")
        val url = arguments?.getString("url")
        val urlToImage = arguments?.getString("urlToImage")
        val publishedAt = arguments?.getString("publishedAt")
        val origem = arguments?.getSerializable("Origem") as Origem

        view.findViewById<TextView>(R.id.tituloNot).text = title
        view.findViewById<TextView>(R.id.sourceNot).text = source
        view.findViewById<TextView>(R.id.conteudoNot).text = conteudo

        Picasso.get().load(image).into(view.findViewById<ImageView>(R.id.imgNot))

        //Voltar ao fragment anterior assim que o utilizador clicar em voltar
        val goBck = view.findViewById<TextView>(R.id.goBack)
        goBck.setOnClickListener{
            goBack()
        }

        //Quando o utilizador clicar em ler mais tarde adicionar a noticia para ler mais tarde na base de dados
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        var noticiaParaGuadar : Noticia? = null
        if (author != null && title != null && description != null && url != null && urlToImage != null && publishedAt != null && conteudo != null){
            noticiaParaGuadar = Noticia(origem, author, title, description, url, urlToImage, publishedAt, conteudo)
        }

        val lerMaisTarde = view.findViewById<TextView>(R.id.lerMaisTarde)
        lerMaisTarde.setOnClickListener{
            if (noticiaParaGuadar != null) {
                adicionarNoticiaLerMaisTarde(noticiaParaGuadar)
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoticiaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoticiaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun goBack(){
        parentFragmentManager.beginTransaction().apply {
            this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            replace(R.id.frame_layout, backFragment)
                .commit()
        }
    }

    public fun setBackFragment(fragment: Fragment){
        backFragment = fragment
    }

    public fun adicionarNoticiaLerMaisTarde(noticia: Noticia){
        val databaseRef = database.reference.child(firebaseAuth.currentUser!!.uid).child(noticia.title)
        val noticiaLerMaisTarde : NoticiaLerMaisTarde = NoticiaLerMaisTarde(noticia)
        val alertDialog = activity?.let { AlertDialog.Builder(it) }

        databaseRef.setValue(noticiaLerMaisTarde).addOnCompleteListener {
            if(it.isSuccessful) {
                if (alertDialog != null) {
                    alertDialog.setMessage("Noticia adicionada para ler mais tarde")
                    alertDialog.setNeutralButton("OK", null)
                    alertDialog.show()
                }
            }else {
                if (alertDialog != null) {
                    alertDialog.setMessage(it.exception.toString())
                    alertDialog.setNeutralButton("OK", null)
                    alertDialog.show()
                }
            }
        }
    }
}