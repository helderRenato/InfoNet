package com.example.mobile.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.api.model.Noticia
import com.example.mobile.fragment.HomeFragment
import com.example.mobile.fragment.NoticiaFragment
import com.squareup.picasso.Picasso

class HomeAdapter(private val noticias: List<Noticia>?, private val context: HomeFragment):
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias?.get(position)
        holder?.let {
            if (noticia != null) {
                it.bindView(noticia, context)
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
        fun bindView(noticia: Noticia, context: HomeFragment) {
            val image = itemView.findViewById<ImageView>(R.id.noticiaImg)
            val titulo = itemView.findViewById<TextView>(R.id.noticiaTit)
            val data = itemView.findViewById<TextView>(R.id.noticiaDt)
            val origem = itemView.findViewById<TextView>(R.id.noticiaOrig)

            Picasso.get().load(noticia.urlToImage).into(image)
            titulo.text = noticia.title
            data.text = noticia.publishedAt
            origem.text = noticia.source.name

            //cardview on click redirect to read the new
            val cardView = itemView.findViewById<CardView>(R.id.cardViewHome)
            cardView.setOnClickListener{
                //redirecionar para outro fragment para com os dados para ler a noticia
                val bundle = Bundle()
                val titulo = noticia.title
                val source = noticia.source
                val conteudo = noticia.content
                val image = noticia.urlToImage

                bundle.putString("title", titulo)
                bundle.putString("source", source.name)
                bundle.putString("conteudo", conteudo)
                bundle.putString("image", image)

                val noticiaFragment = NoticiaFragment()
                noticiaFragment.arguments = bundle

                context.replaceFragment(noticiaFragment)

            }
        }


    }

}