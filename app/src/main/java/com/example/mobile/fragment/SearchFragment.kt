package com.example.mobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobile.R
import com.example.mobile.adapter.HomeAdapter
import com.example.mobile.adapter.SearchAdapter
import com.example.mobile.api.model.Noticia
import com.example.mobile.api.model.RespostaAPI
import com.example.peoplefinder.API.retrofit.RetrofitInitializer
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        //Fazer a pesquisa para o filtro de business já que é o filtro padrão da app
        callGetNoticias("business", view)

        // Filtrar as noticias de acordo com o filtro selecionado nas Tabs
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout);

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab){
                when(tab.position){
                    0 -> {
                        callGetNoticias("business", view)
                    }
                    1 -> {
                        callGetNoticias("entertainment", view)
                    }
                    2 -> {
                        callGetNoticias("general", view)
                    }
                    3 -> {
                        callGetNoticias("health", view)
                    }
                    4 -> {
                        callGetNoticias("science", view)
                    }
                    5 -> {
                        callGetNoticias("sports", view)
                    }
                    6 -> {
                        callGetNoticias("technology", view)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab){

            }
            override fun onTabReselected(tab: TabLayout.Tab){

            }
        })

        //Saber o que esta está escrito no search view
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })


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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
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

    private fun callGetNoticias(filtro: String, view: View){

        getNoticias("", "us", filtro) {
            if (it != null) {
                //configure List
                val noticia : List<Noticia> = it.articles
                val recyclerView: RecyclerView = view.findViewById(R.id.reciclerViewSearch)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = SearchAdapter(noticia, this)
                val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                recyclerView.layoutManager = layoutManager
            }
        }
    }

    public fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
                .commit()
        }
    }

}