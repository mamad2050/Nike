package com.example.nikestore.feature.product

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Comment
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.CommentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(bundle: Bundle, private val commentRepository: CommentRepository) :
    NikeViewModel() {

    val productLiveData = MutableLiveData<Product>()
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {

        productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)

        commentRepository.getAll(productLiveData.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {

                    commentsLiveData.value = t
                }

            })
    }
}