package com.iti.mongez.org.presentation.subscription.planselection.uiState

import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan

/** The "State Manager" single source of truth for PlanSelectionScreen. */
data class PlanSelectionUiState(
    val isLoadingPlans: Boolean = true,
    val plans: List<SubscriptionPlan> = emptyList(),
    val selectedPlanId: String? = null,
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isProcessingPayment: Boolean = false,
    val errorMessage: String? = null,
) {
    val selectedPlan: SubscriptionPlan?
        get() = plans.firstOrNull { it.id == selectedPlanId }

    val isEmailValid: Boolean
        get() = EMAIL_REGEX.matches(email)

    val canConfirmPurchase: Boolean
        get() = selectedPlan != null &&
            fullName.isNotBlank() &&
            isEmailValid &&
            phoneNumber.isNotBlank() &&
            !isProcessingPayment

    private companion object {
        // Deliberately simple/pure-Kotlin (no android.util.Patterns) so this state
        // class stays testable in plain JVM unit tests without Robolectric.
        val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}
