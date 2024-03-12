package tk.infotech.fastlightjp2kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import tk.infotech.fastlightjp2kt.Utils.DataSource
import tk.infotech.fastlightjp2kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val mainView = mainBinding.root
        setContentView(mainView)

        val recyclerView : RecyclerView = mainBinding.imageRecyclerView

        val imageData = DataSource.generateImageListData()

        val imageListAdapter = ImageListAdapter(this, imageData)
        recyclerView.adapter = imageListAdapter


    }

}