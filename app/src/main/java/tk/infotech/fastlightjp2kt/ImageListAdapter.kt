package tk.infotech.fastlightjp2kt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ImageListAdapter(
    private val context: Context,
    private val imageList: List<Image>
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindImageHolder(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private val imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
        private val imageName : TextView = itemView.findViewById(R.id.image_name)
        private val imageProps : TextView = itemView.findViewById(R.id.image_properties)

        fun bindImageHolder(image: Image) {
            imageThumbnail.setImageResource(image.imageId)
            imageName.text = image.name
            imageProps.text = image.properties
        }

        override fun onClick(v: View?) {
            val correspondingImage = imageList
                .find { image ->
                    imageName.text.contentEquals(image.name)
                }
            if (correspondingImage != null) {
                sendImageData(context, correspondingImage.name)
            } else {
                Toast.makeText(context, "Please tap on an item...", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

}