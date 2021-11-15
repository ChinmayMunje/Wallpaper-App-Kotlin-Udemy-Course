package com.gtappdevelopers.wallpaperapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), CategoryClickInterface {
    lateinit var wallpapersRV: RecyclerView
    lateinit var categoryRV: RecyclerView
    lateinit var categoryRVAdapter: CategoryRVAdapter
    lateinit var wallpaperRVAdapter: WallpaperRVAdapter
    lateinit var loadingPB: ProgressBar
    lateinit var searchEdt: EditText
    lateinit var searchIV: ImageView
    lateinit var wallpaperList: List<String>
    lateinit var categoryList: List<CategoryRVModal>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        wallpapersRV = findViewById(R.id.idRVWallpapers)
        categoryRV = findViewById(R.id.idRVCategories)
        loadingPB = findViewById(R.id.idPBLoading)
        searchEdt = findViewById(R.id.idEdtSearch)
        searchIV = findViewById(R.id.idIVSearch)
        categoryList = ArrayList<CategoryRVModal>()

        searchIV.setOnClickListener {
            if (searchEdt.text.toString().isNotEmpty()) {
                getWallpapersByCategory(searchEdt.text.toString())
            }
        }
        getCategories()
        getAllWallpapers()
    }

    private fun getCategories() {
        categoryList = categoryList + CategoryRVModal(
            "Nature",
            "https://images.pexels.com/photos/2387873/pexels-photo-2387873.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )

        categoryList = categoryList + CategoryRVModal(
            "Architecture",
            "https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )
        categoryList = categoryList + CategoryRVModal(
            "Arts",
            "https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )
        categoryList = categoryList + CategoryRVModal(
            "Music",
            "https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )


        categoryList = categoryList + CategoryRVModal(
            "Abstract",
            "https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )

        categoryList = categoryList + CategoryRVModal(
            "Cars",
            "https://images.pexels.com/photos/3802510/pexels-photo-3802510.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )

        categoryList = categoryList + CategoryRVModal(
            "Flowers",
            "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        )

        categoryRVAdapter = CategoryRVAdapter(categoryList, this, this)
        categoryRV.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        categoryRV.adapter = categoryRVAdapter
        categoryRVAdapter.notifyDataSetChanged()
    }

    private fun getAllWallpapers() {
        wallpaperList = ArrayList<String>()
        val apirUrl = "https://api.pexels.com/v1/curated?per_page=30&page=1"
        val queue = Volley.newRequestQueue(this)

        val request = object : JsonObjectRequest(Request.Method.GET, apirUrl, null,
            Response.Listener<JSONObject> { response ->
                loadingPB.visibility = View.GONE
                try {
                    val photoArray = response.getJSONArray("photos")
                    if (photoArray.length().equals(0)) {
                        Toast.makeText(this, "No Wallpapers found", Toast.LENGTH_LONG).show()
                    }

                    for (i in 0 until photoArray.length()) {
                        val photoObj = photoArray.getJSONObject(i)
                        val imgUrl: String = photoObj.getJSONObject("src").getString("portrait")
                        wallpaperList = wallpaperList + imgUrl
                    }
                    wallpaperRVAdapter = WallpaperRVAdapter(wallpaperList, this)
                    wallpapersRV.layoutManager = GridLayoutManager(this, 2)
                    wallpapersRV.adapter = wallpaperRVAdapter
                    wallpaperRVAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@HomeActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] =
                    "Enter your API key"
                return headers
            }
        }

        queue.add(request)
    }

    private fun getWallpapersByCategory(category: String) {
        wallpaperList = ArrayList<String>()
        loadingPB.visibility = View.VISIBLE
        val apiurl = "https://api.pexels.com/v1/search?query="+category+"&per_page=30&page=1"
        val queue = Volley.newRequestQueue(this)

        val request = object : JsonObjectRequest(Request.Method.GET, apiurl, null,
            Response.Listener<JSONObject> { response ->
                loadingPB.visibility = View.GONE
                try {
                    val photoArray = response.getJSONArray("photos")
                    if (photoArray.length().equals(0)) {
                        Toast.makeText(this, "No Wallpapers found", Toast.LENGTH_LONG).show()
                    }

                    for (i in 0 until photoArray.length()) {
                        val photoObj = photoArray.getJSONObject(i)
                        val imgUrl: String = photoObj.getJSONObject("src").getString("portrait")
                        wallpaperList = wallpaperList + imgUrl
                    }
                    wallpaperRVAdapter = WallpaperRVAdapter(wallpaperList, this)
                    wallpapersRV.layoutManager = GridLayoutManager(this, 2)
                    wallpapersRV.adapter = wallpaperRVAdapter
                    wallpaperRVAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@HomeActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] =
                    "Enter your API Key"
                return headers
            }
        }
        queue.add(request)

    }

    override fun onCategoryClick(position: Int) {
        val category: String = categoryList.get(position).categoryName
        getWallpapersByCategory(category)

    }
}