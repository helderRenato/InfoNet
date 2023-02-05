package com.example.mobile.adapter


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.api.model.Noticia
import com.example.mobile.fragment.HomeFragment
import com.example.mobile.fragment.NoticiaFragment
import com.example.mobile.fragment.SearchFragment
import com.squareup.picasso.Picasso

class SearchAdapter(private val noticias: List<Noticia>?, private val context: SearchFragment):
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

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
        fun bindView(noticia: Noticia, context: SearchFragment) {
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
                val author = noticia.author
                val description = noticia.description
                val url = noticia.url
                val urlToImage = noticia.urlToImage
                val publishedAt = noticia.publishedAt

                bundle.putString("title", titulo)
                bundle.putString("source", source.name)
                bundle.putSerializable("Origem", source)
                bundle.putString("conteudo", conteudo)
                bundle.putString("image", image)
                bundle.putString("author", author)
                bundle.putString("description", description)
                bundle.putString("url", url)
                bundle.putString("urlToImage", urlToImage)
                bundle.putString("publishedAt", publishedAt)

                val noticiaFragment = NoticiaFragment()
                noticiaFragment.arguments = bundle

                context.replaceFragment(noticiaFragment, context)

            }
        }


    }

}