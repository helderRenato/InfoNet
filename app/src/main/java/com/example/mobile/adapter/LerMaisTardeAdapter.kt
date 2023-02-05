package com.example.mobile.adapter


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobile.HomeActivity
import com.example.mobile.R
import com.example.mobile.api.model.Noticia
import com.example.mobile.api.model.Origem
import com.example.mobile.fragment.LerMaisTardeFragment
import com.example.mobile.fragment.NoticiaFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class LerMaisTardeAdapter(private val noticias: List<Noticia>?, private val context: LerMaisTardeFragment):
    RecyclerView.Adapter<LerMaisTardeAdapter.ViewHolder>() {

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
        fun bindView(noticia: Noticia, context: LerMaisTardeFragment) {


            val image = itemView.findViewById<ImageView>(R.id.noticiaImg)
            val titulo = itemView.findViewById<TextView>(R.id.noticiaTit)
            val data = itemView.findViewById<TextView>(R.id.noticiaDt)
            val origem = itemView.findViewById<TextView>(R.id.noticiaOrig)
            val removerLerMaisTarde = itemView.findViewById<TextView>(R.id.removerLerMaisTarde)
            //Fazer com que a text view seja visivel para remover a noticia
            removerLerMaisTarde.visibility = View.VISIBLE

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

            var firebaseAuth: FirebaseAuth
            var database: FirebaseDatabase
            firebaseAuth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance()
            //Remover a noticia do firebase assim que clicado para remover
            removerLerMaisTarde.setOnClickListener {
                val databaseRef = database.getReference(firebaseAuth.currentUser!!.uid)

                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot){
                        //buscar cada noticia uma por uma
                        for(productSnapshot in dataSnapshot.children){
                            //Ver o valor dentro da key ou seja title
                            if(productSnapshot.key != null){
                                //Adicionar as noticias encontradas a lista
                                databaseRef.child(noticia.title).addValueEventListener(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.value != null){
                                            val noticia1 = snapshot.value as HashMap<*, *>
                                            for (not in noticia1) {
                                                //Construir um novo objeto de noticia
                                                val objetoNoticia = not.value as HashMap<*, *>
                                                val title = objetoNoticia.get("title").toString()
                                                if (title.equals(noticia.title)) {
                                                    databaseRef.child(title).removeValue()
                                                }
                                            }
                                        }


                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError){

                    }
                })
            }
        }
    }
}