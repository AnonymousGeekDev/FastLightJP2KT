package tk.infotech.fastlightjp2kt.Utils

import tk.infotech.fastlightjp2kt.Image
import tk.infotech.fastlightjp2kt.R

object DataSource {

    //List of trivial/recurrent operations
    fun generateImageListData() = listOf(
        Image(
            R.drawable.ic_launcher_background,
            "Balloon",
            "Unknown yet"
        ),
        Image(R.drawable.ic_launcher_background, "Boat", "Unknown yet"),
        Image(R.drawable.ic_launcher_background, "Lena", "Colored version of image"),
        Image(R.drawable.ic_launcher_background, "Lena-grey", "Gray version of image")
    )

    val imageNameList = listOf("balloon.jp2", "boat.jp2", "lena.jp2", "lena-grey.jp2")

}