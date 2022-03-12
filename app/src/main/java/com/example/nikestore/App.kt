package com.example.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikestore.data.db.AppDataBase
import com.example.nikestore.data.repo.*
import com.example.nikestore.data.repo.order.OrderRemoteDataSource
import com.example.nikestore.data.repo.order.OrderRepository
import com.example.nikestore.data.repo.order.OrderRepositoryImpl
import com.example.nikestore.data.repo.source.*
import com.example.nikestore.feature.auth.AuthViewModel
import com.example.nikestore.feature.cart.CartViewModel
import com.example.nikestore.feature.checkout.CheckOutViewModel
import com.example.nikestore.feature.common.ProductListAdapter
import com.example.nikestore.feature.favorite.FavoriteProductsViewModel
import com.example.nikestore.feature.list.ProductListViewModel
import com.example.nikestore.feature.home.HomeViewModel
import com.example.nikestore.feature.main.MainViewModel

import com.example.nikestore.feature.product.CommentListViewModel
import com.example.nikestore.feature.product.ProductDetailViewModel
import com.example.nikestore.feature.profile.ProfileViewModel
import com.example.nikestore.feature.shipping.ShippingViewModel
import com.example.nikestore.services.imageloader.FrescoImageLoadingService
import com.example.nikestore.services.imageloader.ImageLoadingService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        Fresco.initialize(this)

        val myModules = module {


            single<ImageLoadingService> { FrescoImageLoadingService() }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }



            single{
                Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build()
            }

            single { UserLocalDataSource(get()) }

            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDataBase>().productDao()
                )
            }
            factory<BannerRepository> {
                BannerRepositoryImpl(
                    BannerRemoteDataSource(get())
                )
            }
            factory { (viewType: Int) -> ProductListAdapter(viewType, get()) }

            factory<CommentRepository> {
                CommentRepositoryImpl(
                    CommentRemoteDataSource(get())
                )
            }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }

            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductsViewModel(get()) }
            viewModel { (orderId: Int) -> CheckOutViewModel(orderId, get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()

    }
}