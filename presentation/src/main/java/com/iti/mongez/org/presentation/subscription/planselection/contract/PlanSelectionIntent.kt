package com.iti.mongez.org.presentation.subscription.planselection.contract

import com.iti.mongez.org.domain.subscription.model.PaymentOutcome
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan

/**
 * User actions & system triggers for PlanSelectionScreen - the "Intent Processor" input
 * per docs/ArchitectureAgents.md. The ViewModel is the only thing that consumes these.
 */
sealed interface PlanSelectionIntent {
    data object LoadPlans : PlanSelectionIntent
    data class SelectPlan(val plan: SubscriptionPlan) : PlanSelectionIntent
    data class FullNameChanged(val value: String) : PlanSelectionIntent
    data class EmailChanged(val value: String) : PlanSelectionIntent
    data class PhoneNumberChanged(val value: String) : PlanSelectionIntent
    data object ConfirmPurchase : PlanSelectionIntent

    /** Reported once the native Paymob checkout UI (launched via an Effect) finishes. */
    data class PaymentSdkFinished(val outcome: PaymentOutcome) : PlanSelectionIntent

    data object DismissError : PlanSelectionIntent
}
