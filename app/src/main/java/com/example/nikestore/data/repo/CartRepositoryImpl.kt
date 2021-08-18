package com.example.nikestore.data.repo

import com.example.nikestore.data.AddToCartResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.repo.source.CartDataSource
import com.example.nikestore.data.repo.source.CartRemoteDataSource
import io.reactivex.Single

class CartRepositoryImpl(val remoteDataSource: CartDataSource) : CartRepository {

    override fun addTOCart(productId: Int): Single<AddToCartResponse> =
        remoteDataSource.addTOCart(productId)

    override fun get(): Single<CartResponse> {
        TODO("Not yet implemented")
    }

    override fun remove(cartItemId: Int): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> {
        TODO("Not yet implemented")
    }

    override fun getCartItemsCount(): Single<CartItemCount> {
        TODO("Not yet implemented")
    }
}