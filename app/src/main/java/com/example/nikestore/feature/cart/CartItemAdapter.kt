package com.example.nikestore.feature.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.CartItem
import com.example.nikestore.data.PurchaseDetail
import com.example.nikestore.services.imageloader.ImageLoadingService
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_purchase_details.view.*


const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAIL = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallbacks: CartItemViewCallbacks
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var purchaseDetail: PurchaseDetail? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART_ITEM)
            CartItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
            )
        else
            PurchaseDetailViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_purchase_details, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder)
            holder.bindCartItem(cartItems[position])
        else if (holder is PurchaseDetailViewHolder) {
            purchaseDetail?.let {
                holder.bind(it.totalPrice, it.shipping_cost, it.payable_price)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size + 1

    override fun getItemViewType(position: Int): Int {

        return if (position == cartItems.size)
            VIEW_TYPE_PURCHASE_DETAIL
        else
            VIEW_TYPE_CART_ITEM
    }

    inner class CartItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindCartItem(cartItem: CartItem) {

            containerView.productTitleTv.text = cartItem.product.title
            containerView.cartItemCountTv.text = cartItem.count.toString()
            containerView.previousPriceTv.text =
                formatPrice(cartItem.product.price + cartItem.product.discount)
            containerView.priceTv.text = formatPrice(cartItem.product.price)
            imageLoadingService.load(containerView.productIv, cartItem.product.image)

            containerView.removeFromCartBtn.setOnClickListener {
                cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
            }


            containerView.changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarVisible) View.VISIBLE else View.GONE

            containerView.cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarVisible) View.INVISIBLE else View.VISIBLE

            containerView.increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarVisible = true
                containerView.changeCountProgressBar.visibility = View.VISIBLE
                containerView.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartItem)
            }

            containerView.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1)
                    cartItem.changeCountProgressBarVisible = true
                containerView.changeCountProgressBar.visibility = View.VISIBLE
                containerView.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartItem)

            }

            containerView.productIv.setOnClickListener {
                cartItemViewCallbacks.onProductImageClick(cartItem)
            }

            if (cartItem.changeCountProgressBarVisible) {
                containerView.changeCountProgressBar.visibility = View.VISIBLE
                containerView.increaseBtn.visibility = View.GONE
            }


        }
    }

    class PurchaseDetailViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {


        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {

            containerView.totalPriceTv.text = formatPrice(totalPrice)
            containerView.shippingCostTv.text = formatPrice(shippingCost)
            containerView.payablePriceTv.text = formatPrice(payablePrice)

        }

    }

    interface CartItemViewCallbacks {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }


    fun removeCartItem(cartItem: CartItem) {

        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun changeCount(cartItem: CartItem) {

        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarVisible = false
            notifyItemChanged(index)
        }
    }

}