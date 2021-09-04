package com.example.nikestore.feature.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.feature.product.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.view_card_empty_state.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteProductsActivity : NikeActivity(),
    FavoriteProductsAdapter.FavoriteProductEventListener {
    val viewModel: FavoriteProductsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        helpBtn.setOnClickListener {
            showSnackBar(getString(R.string.favorites_help_message))
        }

        viewModel.productsLiveData.observe(this) {

            if (it.isNotEmpty()) {
                favoriteProductsRv.layoutManager = LinearLayoutManager(this)
                favoriteProductsRv.adapter =
                    FavoriteProductsAdapter(
                        it as MutableList<Product>, this, get()
                    )
            } else
                showEmptyState(R.layout.view_default_empty_state)

        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorites(product)
    }


}