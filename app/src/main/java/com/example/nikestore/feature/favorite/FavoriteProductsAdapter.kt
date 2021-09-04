package com.example.nikestore.feature.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Product
import com.example.nikestore.services.imageloader.ImageLoadingService
import com.example.nikestore.view.NikeImageView

class FavoriteProductsAdapter(
    private val products: MutableList<Product>,
    private val favoriteProductEventListener: FavoriteProductEventListener,
    private val imageLoadingService: ImageLoadingService
) :

    RecyclerView.Adapter<FavoriteProductsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsAdapter.Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteProductsAdapter.Holder, position: Int) =
        holder.bindProduct(
            products[position]
        )

    override fun getItemCount(): Int = products.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        private val productIv = itemView.findViewById<NikeImageView>(R.id.nikeImageView)

        fun bindProduct(product: Product) {

            titleTv.text = product.title
            imageLoadingService.load(productIv, product.image)

            itemView.setOnClickListener {
                favoriteProductEventListener.onClick(product)
            }
            itemView.setOnLongClickListener {
                products.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductEventListener.onLongClick(product)
                return@setOnLongClickListener false
            }
        }
    }


    interface FavoriteProductEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product)
    }
}