package com.example.mobile.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobile.R
import com.example.mobile.adapter.LerMaisTardeAdapter
import com.example.mobile.api.model.Noticia
import com.example.mobile.api.model.Origem
import com.example.mobile.api.model.RespostaAPI
import com.example.peoplefinder.API.retrofit.RetrofitInitializer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LerMaisTardeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class LerMaisTardeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val alertDialog : AlertDialog.Builder? = null
    private var progressDialog: ProgressDialog? = null
    private var isDataLoaded = false
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
        val alertDialog = activity?.let { AlertDialog.Builder(it) }
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(activity)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ler_mais_tarde, container, false)
        //Apresentar ao utilizador que a app esta a buscar as noticias
        progressDialog!!.setMessage("A procurar...")
        progressDialog!!.show()

        getNoticiasFirebase(view, this)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LerMaisTardeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LerMaisTardeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    //buscar as noticias que foram guardadas para ler mais tarde pelo uui do utilizador
    private fun getNoticiasFirebase(view: View, context: LerMaisTardeFragment){
        var listaNoticia = mutableListOf<Noticia>()//Lista mutavel para ser possivel adicionar valores
        val databaseRef = database.getReference(firebaseAuth.currentUser!!.uid)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                //buscar cada noticia uma por uma
                for(productSnapshot in dataSnapshot.children){
                    //Ver o valor dentro da key ou seja title
                    if(productSnapshot.key != null){
                        //Adicionar as noticias encontradas a lista
                        databaseRef.child(productSnapshot.key.toString()).addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.value == null){
                                    //atualizar o fragment
                                }else {
                                    val noticia = snapshot.value as HashMap<*, *>
                                    for (not in noticia) {
                                        //Construir um novo objeto de noticia
                                        val objetoNoticia = not.value as HashMap<*, *>


                                        val author = objetoNoticia.get("author").toString()
                                        val content = objetoNoticia.get("content").toString()
                                        val description =
                                            objetoNoticia.get("description").toString()
                                        val publishedAt =
                                            objetoNoticia.get("publishedAt").toString()
                                        val title = objetoNoticia.get("title").toString()
                                        val url = objetoNoticia.get("url").toString()
                                        val urlToImage = objetoNoticia.get("urlToImage").toString()

                                        //Criar uma variavel que herda da clesse origem
                                        val source = objetoNoticia.get("source") as HashMap<*, *>
                                        val id = objetoNoticia.get("source").toString()
                                        val name = objetoNoticia.get("name").toString()
                                        val origem: Origem = Origem(id, name)

                                        //Criar a noticia
                                        val noticia: Noticia = Noticia(
                                            origem,
                                            author,
                                            title,
                                            description,
                                            url,
                                            urlToImage,
                                            publishedAt,
                                            content
                                        )
                                        listaNoticia.add(noticia)
                                        progressDialog!!.dismiss()
                                    }
                                    adicionarNoFragment(view, listaNoticia, context)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    }
                }
                progressDialog!!.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError){

            }

        })


    }


    //Buscar as noticias
    private fun getNoticias(q: String, country: String, category: String, onResult: (RespostaAPI?) -> Unit){
        val call = RetrofitInitializer().noticiaService().getNoticias(q, "2b41d00aef9043cc84784b6fab8c7f0d", country, category)
        call.enqueue(
            object : Callback<RespostaAPI> {
                override fun onFailure(call: Call<RespostaAPI>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null)
                    progressDialog!!.dismiss()
                }

                override fun onResponse(call: Call<RespostaAPI>, response: Response<RespostaAPI>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                    progressDialog!!.dismiss()
                }
            }
        )
    }

    private fun adicionarNoFragment(view: View, noticia: List<Noticia>, context: LerMaisTardeFragment){
        val recyclerView: RecyclerView = view.findViewById(R.id.reciclerViewLerMaisTarde)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = LerMaisTardeAdapter(noticia, context)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    public fun replaceFragment(fragment: NoticiaFragment, fragment1: Fragment){
        fragment.setBackFragment(fragment1)
        parentFragmentManager.beginTransaction().apply {
            this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frame_layout, fragment)
                .commit()
        }
    }
}