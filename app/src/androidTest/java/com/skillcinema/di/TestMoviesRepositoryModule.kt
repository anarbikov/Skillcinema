package com.skillcinema.di

import android.content.Context
import com.skillcinema.data.Api
import com.skillcinema.data.RepositoryImpl
import com.skillcinema.domain.RepositoryInterface
import com.skillcinema.room.CollectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SingletonComponent::class]
)
class FakeRepositoryModule {

    @Provides
    @Singleton
    fun provideRepositoryImpl(
        api: Api,
        @ApplicationContext
        context: Context,
        collectionDao: CollectionDao
    ): RepositoryInterface = RepositoryImpl(api,context,collectionDao)

}