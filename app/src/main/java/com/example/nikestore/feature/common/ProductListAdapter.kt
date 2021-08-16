package com.example.nikestore.feature.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.common.implementSpringAnimationTrait
import com.example.nikestore.data.Product
import com.example.nikestore.services.http.ImageLoadingService
import com.example.nikestore.view.NikeImageView
import java.lang.IllegalStateException

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductListAdapter.Holder>() {

    var productOnClickListener: OnProductClickListener? = null

    var products = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val layoutResId = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            else -> throw IllegalStateException("viewType not valid")
        }
        return Holder(
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        )

    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindProduct(products[position])


    override fun getItemCount() = products.size


    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productIv = itemView.findViewById<NikeImageView>(R.id.productIv)
        private val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        private val currentPriceTv = itemView.findViewById<TextView>(R.id.currentPriceTv)
        private val previousPriceTv = itemView.findViewById<TextView>(R.id.previousPriceTv)

        fun bindProduct(product: Product) {

            imageLoadingService.load(productIv, product.image)
            titleTv.text = product.title
            currentPriceTv.text = formatPrice(product.price)
            previousPriceTv.text = formatPrice(product.previous_price)
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productOnClickListener?.onProductClick(product)
            }

        }
    }

    interface OnProductClickListener {

        fun onProductClick(product: Product)

    }

}