package com.iti.mongez.org.presentation.subscription.planselection.uiState

import com.iti.mongez.org.domain.subscription.model.PaymentCheckoutSession

/** One-off, non-persisted events - the "Effect Dispatcher" per docs/ArchitectureAgents.md. */
sealed interface PlanSelectionEffect {
    data class LaunchPaymobCheckout(val session: PaymentCheckoutSession) : PlanSelectionEffect
    data object NavigateToMain : PlanSelectionEffect
    data class ShowSnackbar(val message: String) : PlanSelectionEffect
}
