package com.example.nikestore.data.repo

import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.source.ProductDataSource
import com.example.nikestore.data.repo.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    val remoteDataSource: ProductDataSource,
    val localDataSource: ProductLocalDataSource
) : ProductRepository {

    override fun getProducts(): Single<List<Product>> = remoteDataSource.getProducts()

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }
}