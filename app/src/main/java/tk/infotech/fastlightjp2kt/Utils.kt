package tk.infotech.fastlightjp2kt

import android.content.Context
import android.content.Intent


fun sendImageData(context: Context, imageName: String){
    val dataIntent = Intent(context, ImageDetailActivity::class.java)
    dataIntent.putExtra("imageName", imageName)

    context.startActivity(dataIntent)
}
