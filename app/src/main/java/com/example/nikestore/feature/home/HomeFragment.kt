package com.example.nikestore.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.data.SORT_POPULAR
import com.example.nikestore.feature.common.ProductListAdapter
import com.example.nikestore.feature.common.VIEW_TYPE_ROUND
import com.example.nikestore.feature.list.ProductListActivity
import com.example.nikestore.feature.main.BannerSliderAdapter
import com.example.nikestore.feature.product.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener {

    private val homeViewModel: HomeViewModel by viewModel()
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    private val popularsProductListAdapter: ProductListAdapter by inject {
        parametersOf(
            VIEW_TYPE_ROUND
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        latestProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        latestProductsRv.adapter = productListAdapter
        productListAdapter.productEventListener = this

        homeViewModel.productsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productListAdapter.products = it as ArrayList<Product>

        }

        popularsProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularsProductsRv.adapter = popularsProductListAdapter
        popularsProductListAdapter.productEventListener = this

        homeViewModel.popularsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            popularsProductListAdapter.products = it as ArrayList<Product>
        }


        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            bannerSliderViewPager.adapter = bannerSliderAdapter

            val viewPagerHeight = (((bannerSliderViewPager.measuredWidth - convertDpToPixel
                (32f, requireContext())) * 173) / 328).toInt()

            val layoutParams = bannerSliderViewPager.layoutParams
            layoutParams.height = viewPagerHeight
            bannerSliderViewPager.layoutParams = layoutParams
            sliderIndicator.setViewPager2(bannerSliderViewPager)

        }

        viewLatestProductsBtn.setOnClickListener {
            goToProductListActivity(SORT_LATEST)
        }

        viewPopularProductBtn.setOnClickListener {
            goToProductListActivity(SORT_POPULAR)
        }


    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }


    private fun goToProductListActivity(sort: Int) {
        startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, sort)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        homeViewModel.addProductToFavorites(product)
    }

}

