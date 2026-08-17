package com.iti.mongez.org.domain.subscription.usecase

import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlanCatalog
import javax.inject.Inject

/**
 * Returns the plans a user can pick from. Backed by a local catalog today; kept as a
 * use case (rather than reading [SubscriptionPlanCatalog] directly from the ViewModel)
 * so swapping to a remote catalog later doesn't touch the presentation layer.
 */
class GetSubscriptionPlansUseCase @Inject constructor() {
    operator fun invoke(): List<SubscriptionPlan> = SubscriptionPlanCatalog.plans
}
