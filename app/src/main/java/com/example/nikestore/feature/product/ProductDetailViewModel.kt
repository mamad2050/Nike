package com.example.nikestore.feature.product

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.*
import com.example.nikestore.data.Comment
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.CartRepository
import com.example.nikestore.data.repo.CommentRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(
    bundle: Bundle,
    commentRepository: CommentRepository,
    val cartRepository: CartRepository
) :
    NikeViewModel() {

    val productLiveData = MutableLiveData<Product>()
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)

        commentRepository.getAll(productLiveData.value!!.id)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {

                    commentsLiveData.value = t
                }

            })
    }

    fun onAddToCartBtn(): Completable =
        cartRepository.addTOCart(productLiveData.value!!.id)
            .ignoreElement()

}