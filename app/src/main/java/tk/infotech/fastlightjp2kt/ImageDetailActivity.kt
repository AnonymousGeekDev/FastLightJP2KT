package tk.infotech.fastlightjp2kt

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gemalto.jp2.JP2Decoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tk.infotech.fastlightjp2kt.Utils.DataSource
import tk.infotech.fastlightjp2kt.databinding.ActivityDetailImageBinding
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import kotlin.system.measureTimeMillis


class ImageDetailActivity : AppCompatActivity(){

    private val TAG : String = "ImageDetailActivity"
    private lateinit var detailImageBinding: ActivityDetailImageBinding

    /*
     This variable represents the scope that works with the main thread.
      La variable ci-dessous represente le contexte dans lequel les coroutines qui
        interagissent avec les elements graphiques(visuels) s'executent.
     */
    private val uiScope : CoroutineScope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailImageBinding = ActivityDetailImageBinding.inflate(layoutInflater)
        val rootView = detailImageBinding.root
        setContentView(rootView)

        val imgView = detailImageBinding.image

        val imageName = intent.getStringExtra("imageName")
        val imageFileName = DataSource
            .imageNameList
            .find { name ->
                name.contentEquals(imageName, ignoreCase = true)
            }
        if (imageFileName != null) {
            imgView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    imgView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    val outerTime = measureTimeMillis {
                        uiScope.launch {
                            val processedImage = processImage(imgView, imageFileName)

                            imgView.setImageBitmap(processedImage)
                        }
                    }

                    println("Execution time : " + outerTime.toDouble() + "ms")
                }
            })
        }
        else {
         Toast
             .makeText(applicationContext, "The image could not be found.", Toast.LENGTH_LONG)
             .show()
        }

    }


    // TODO: Optimize the coroutine implementation below.
    // TODO : Add tests for measuring coroutine performance.

    // Initial draft of coroutine implementation

      suspend fun processImage(
          view: ImageView,
          imageName: String
      ) : Bitmap? = withContext(Dispatchers.Default) {

          var executionTime: Long = 0
          val start = System.currentTimeMillis()

        Log.d(TAG, String.format("View resolution: %d x %d", view.width, view.height))
        var ret: Bitmap? = null
        var `in`: InputStream? = null

        try {

                `in` = assets.open(imageName)
                val decoder = JP2Decoder(`in`)
                val header = decoder.readHeader()
                println("Number of resolutions: " + header.numResolutions)
                println("Number of quality layers: " + header.numQualityLayers)
                var skipResolutions = 1
                var imgWidth = header.width
                var imgHeight = header.height
                Log.d(TAG, String.format("JP2 resolution: %d x %d", imgWidth, imgHeight))
                while (skipResolutions < header.numResolutions) {
                    imgWidth = imgWidth shr 1
                    imgHeight = imgHeight shr 1
                    if (imgWidth < view.width || imgHeight < view.height) break else skipResolutions++
                }
                //we break the loop when skipResolutions goes over the correct value
                skipResolutions--
                Log.d(TAG, String.format("Skipping %d resolutions", skipResolutions))
                if (skipResolutions > 0) decoder.setSkipResolutions(skipResolutions)
                ret = decoder.decode()

            executionTime = System.currentTimeMillis() - start
            Log.d(TAG, String.format("Decoded at resolution: %d x %d", ret?.width, ret?.height))

            println("Execution time : " + executionTime + "ms")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            close(`in`)
        }

          withContext(Dispatchers.Main){
              detailImageBinding.decodingTimeView.append("$executionTime ms")
          }

         ret

    }

    private fun close(stream : Closeable?) {
        try {
            stream?.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}
