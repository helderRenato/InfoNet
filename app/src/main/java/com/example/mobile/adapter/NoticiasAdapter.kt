package com.example.mobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.api.model.Noticia
import com.example.mobile.fragment.HomeFragment
import com.squareup.picasso.Picasso

class HomeAdapter(private val noticias: List<Noticia>?, private val context: HomeFragment):
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias?.get(position)
        holder?.let {
            if (noticia != null) {
                it.bindView(noticia)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = context.layoutInflater.inflate(R.layout.noticia, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noticias?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(noticia: Noticia) {
            val image = itemView.findViewById<ImageView>(R.id.noticiaImg)
            val titulo = itemView.findViewById<TextView>(R.id.noticiaTit)
            val data = itemView.findViewById<TextView>(R.id.noticiaDt)

            Picasso.get().load(noticia.urlToImage).into(image)
            titulo.text = noticia.title
            data.text = noticia.publishedAt
        }


    }

}