package com.iti.mongez.org.data.di

import com.iti.mongez.org.data.repository.AuthRepositoryImpl
import com.iti.mongez.org.data.repository.CoursesRepositoryImpl
import com.iti.mongez.org.data.repository.ProfileRepositoryImpl
import com.iti.mongez.org.data.repository.TeamDetailsRepositoryImpl
import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import com.iti.mongez.org.domain.profile.repository.ProfileRepository
import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCoursesRepository(
        coursesRepositoryImpl: CoursesRepositoryImpl
    ): CoursesRepository

    @Binds
    @Singleton
    abstract fun bindTeamsRepository(
        teamsRepositoryImpl: TeamDetailsRepositoryImpl
    ): TeamDetailsRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}
