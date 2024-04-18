package com.example.flixster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvMovies = findViewById(R.id.rvMovies)
        val movieAdapter = MovieAdapter(this, movies) {
            movie -> launchDetailsActivity(movie as Movie)
        }
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try{
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    val curSize = movieAdapter.getItemCount()
                    val newItems = Movie.fromJsonArray(movieJsonArray)
                    movies.addAll(newItems)
                    movieAdapter.notifyItemRangeInserted(curSize, newItems.size)
                    Log.i(TAG, "Movies list $movies")
                }
                catch (e: JSONException){
                    Log.e(TAG, "Encountered exception $e")
                }
            }

        })

    }

    private fun launchDetailsActivity(contact: Movie){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIE_EXTRA", contact.posterImageURL)
        val profileView = findViewById<ImageView>(R.id.ivPoster)
        if (profileView != null){
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                profileView,
                "moviePoster"
            )
            startActivity(intent, options.toBundle())
        } else {
            Log.w(TAG, "Movie image not found for transition animation")
            startActivity(intent)
        }
    }
}