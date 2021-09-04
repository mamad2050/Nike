package com.example.nikestore.feature.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.feature.common.ProductListAdapter
import com.example.nikestore.feature.common.VIEW_TYPE_LARGE
import com.example.nikestore.feature.common.VIEW_TYPE_SMALL
import com.example.nikestore.feature.product.ProductDetailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlinx.android.synthetic.main.activity_product_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ProductListActivity : NikeActivity() , ProductListAdapter.ProductEventListener {

    val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }

    val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        val gridLayoutManager = GridLayoutManager(this, 2)
        productsRv.layoutManager = gridLayoutManager
        productsRv.adapter = productListAdapter

        viewModel.productsLiveData.observe(this) {
            productListAdapter.products = it as ArrayList<Product>
        }


        productListAdapter.productEventListener = this

        viewTypeChangerBtn.setOnClickListener {
            if (productListAdapter.viewType == VIEW_TYPE_SMALL) {
                viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large)
                productListAdapter.viewType = VIEW_TYPE_LARGE
                gridLayoutManager.spanCount = 1

            } else {
                viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
                productListAdapter.viewType = VIEW_TYPE_SMALL
                gridLayoutManager.spanCount = 2
            }
            productListAdapter.notifyDataSetChanged()
        }


        viewModel.selectedSortTitleLiveData.observe(this){
            selectedSortTitleTv.text = getString(it)
        }

        sortBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(
                    R.array.sortArray, viewModel.sort
                ) { dialog, index ->
                    viewModel.onSelectedSortChangedByUser(index)
                    dialog.dismiss()
                }.setTitle(getString(R.string.sort))
            dialog.show()
        }

        toolbarView.onBackBtnClickListener = View.OnClickListener {
            finish()
        }

    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        TODO("Not yet implemented")
    }
}