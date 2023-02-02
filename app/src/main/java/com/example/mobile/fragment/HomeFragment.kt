package com.example.mobile.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobile.R
import com.example.mobile.adapter.HomeAdapter
import com.example.mobile.api.model.Noticia
import com.example.mobile.api.model.RespostaAPI
import com.example.peoplefinder.API.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //Apresentar ao utilizador que a app esta a buscar as noticias
        val progressDialog = ProgressDialog(view.context)
        progressDialog.setMessage("A procurar...")
        progressDialog.show()

        //get Noticias
        getNoticias("", "us", "entertainment") {
            if (it != null) {
                //configure List
                val noticia : List<Noticia> = it.articles
                val recyclerView: RecyclerView = view.findViewById(R.id.reciclerViewHome)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = HomeAdapter(noticia, this)
                val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                recyclerView.layoutManager = layoutManager
            }
        }
        progressDialog.dismiss()


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    //Buscar as noticias
    private fun getNoticias(q: String, country: String, category: String, onResult: (RespostaAPI?) -> Unit){
        val call = RetrofitInitializer().noticiaService().getNoticias(q, "2b41d00aef9043cc84784b6fab8c7f0d", country, category)
        call.enqueue(
            object : Callback<RespostaAPI> {
                override fun onFailure(call: Call<RespostaAPI>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null)
                }

                override fun onResponse(call: Call<RespostaAPI>, response: Response<RespostaAPI>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }

    public fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
                .commit()
        }
    }
}