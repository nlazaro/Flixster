package com.example.flixster

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


private const val TAG = "MovieAdapter"
const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(
    private val context: Context,
    private val movies: List<Movie>,
    param: (Any) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val ivBackdrop = itemView.findViewById<ImageView>(R.id.ivBackdrop)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(movie: Movie){
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            Log.d("MovieAdapter", "Poster image URL: ${movie.posterImageURL}")
            ivPoster?.let{
                Glide.with(context)
                    .load(movie.posterImageURL)
                    .error(R.color.default_placeholder)
                    .into(ivPoster)
            }
            Log.d("MovieAdapter", "Backdrop image URL: ${movie.backdropImageURL}")
            ivBackdrop?.let{
                Glide.with(context)
                    .load(movie.backdropImageURL)
                    .error(R.color.default_placeholder)
                    .into(ivBackdrop)
            }
        }

        override fun onClick(v: View?) {
            // Get notified of movie that was clicked
            val movie = movies[adapterPosition]
            // Use the intent system to navigate to the new activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("MOVIE_EXTRA", movie)
            context.startActivity(intent)

        }
    }
}
