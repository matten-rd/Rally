package com.example.compose.rally.di

import android.app.Application
import androidx.room.Room
import com.example.compose.rally.data.RallyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: RallyDatabase.Callback
    ) = Room.databaseBuilder(app, RallyDatabase::class.java, "rally.db")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()


    @Provides
    fun provideAccountDao(db: RallyDatabase) = db.accountDao()

    @Provides
    fun provideBillDao(db: RallyDatabase) = db.billDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
