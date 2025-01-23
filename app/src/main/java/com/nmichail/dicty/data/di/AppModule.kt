package com.nmichail.dicty.data.di

import com.nmichail.dicty.data.remote.DictionaryApi
import com.nmichail.dicty.data.repository.DictionaryRepositoryImpl
import com.nmichail.dicty.domain.repository.DictionaryRepository
import com.nmichail.dicty.domain.usecases.GetWordDefinitionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(api: DictionaryApi): DictionaryRepository {
        return DictionaryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetWordDefinitionUseCase(repository: DictionaryRepository): GetWordDefinitionUseCase {
        return GetWordDefinitionUseCase(repository)
    }
}