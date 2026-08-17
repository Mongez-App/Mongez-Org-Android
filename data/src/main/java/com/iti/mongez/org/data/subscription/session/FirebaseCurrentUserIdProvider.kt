package com.iti.mongez.org.data.subscription.session

import com.google.firebase.auth.FirebaseAuth
import com.iti.mongez.org.domain.subscription.repository.CurrentUserIdProvider
import javax.inject.Inject

/**
 * The project's existing auth flow (see data/remote/datasource/FirebaseAuthDataSource.kt)
 * is Firebase-based, so subscription state is scoped by `FirebaseAuth.currentUser.uid` -
 * the same identity the rest of the app already treats as "the signed-in user". If your
 * session/user id source differs, swap this implementation only; nothing else in the
 * subscription feature needs to change.
 */
class FirebaseCurrentUserIdProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : CurrentUserIdProvider {
    override fun currentUserId(): String? = firebaseAuth.currentUser?.uid
}
