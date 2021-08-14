package com.example.nikestore

import android.app.Application
import android.os.Bundle
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.*
import com.example.nikestore.data.repo.source.*
import com.example.nikestore.feature.main.MainViewModel
import com.example.nikestore.feature.main.ProductListAdapter
import com.example.nikestore.feature.product.ProductDetailViewModel
import com.example.nikestore.services.http.ApiService
import com.example.nikestore.services.http.FrescoImageLoadingService
import com.example.nikestore.services.http.ImageLoadingService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val myModules = module {

            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }

            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    ProductLocalDataSource()
                )
            }
            factory<BannerRepository> {
                BannerRepositoryImpl(
                    BannerRemoteDataSource(get())
                )
            }


            factory { ProductListAdapter(get()) }


            factory<CommentRepository> {
                CommentRepositoryImpl(
                    CommentRemoteDataSource(get())
                )
            }

            viewModel { MainViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
    }
}