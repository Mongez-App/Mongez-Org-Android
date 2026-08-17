package com.iti.mongez.org.domain.subscription.repository

/**
 * Resolves "who is signed in right now" for subscription scoping, without the domain
 * layer needing to know *how* (Firebase, a session token, ...). Implemented in the data
 * layer.
 */
interface CurrentUserIdProvider {
    fun currentUserId(): String?
}
