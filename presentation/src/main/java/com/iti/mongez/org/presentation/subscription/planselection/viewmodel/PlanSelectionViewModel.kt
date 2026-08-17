package com.iti.mongez.org.presentation.subscription.planselection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.subscription.model.BillingData
import com.iti.mongez.org.domain.subscription.model.PaymentOutcome
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.domain.subscription.usecase.CreatePaymentIntentionUseCase
import com.iti.mongez.org.domain.subscription.usecase.GetSubscriptionPlansUseCase
import com.iti.mongez.org.domain.subscription.usecase.VerifyAndActivateSubscriptionUseCase
import com.iti.mongez.org.presentation.subscription.planselection.contract.PlanSelectionIntent
import com.iti.mongez.org.presentation.subscription.planselection.uiState.PlanSelectionEffect
import com.iti.mongez.org.presentation.subscription.planselection.uiState.PlanSelectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The MVI loop for PlanSelectionScreen: [state] is the single source of truth (State
 * Manager), [onIntent] is the single entry point for every user action / system trigger
 * (Intent Processor), and [effect] carries one-off events like navigation or SDK launch
 * requests (Effect Dispatcher) - see docs/ArchitectureAgents.md.
 */
@HiltViewModel
class PlanSelectionViewModel @Inject constructor(
    private val getSubscriptionPlansUseCase: GetSubscriptionPlansUseCase,
    private val createPaymentIntentionUseCase: CreatePaymentIntentionUseCase,
    private val verifyAndActivateSubscriptionUseCase: VerifyAndActivateSubscriptionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PlanSelectionUiState())
    val state: StateFlow<PlanSelectionUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PlanSelectionEffect>()
    val effect: SharedFlow<PlanSelectionEffect> = _effect.asSharedFlow()

    init {
        onIntent(PlanSelectionIntent.LoadPlans)
    }

    fun onIntent(intent: PlanSelectionIntent) {
        when (intent) {
            PlanSelectionIntent.LoadPlans -> loadPlans()
            is PlanSelectionIntent.SelectPlan -> selectPlan(intent.plan)
            is PlanSelectionIntent.FullNameChanged -> _state.update { it.copy(fullName = intent.value) }
            is PlanSelectionIntent.EmailChanged -> _state.update { it.copy(email = intent.value) }
            is PlanSelectionIntent.PhoneNumberChanged -> _state.update { it.copy(phoneNumber = intent.value) }
            PlanSelectionIntent.ConfirmPurchase -> confirmPurchase()
            is PlanSelectionIntent.PaymentSdkFinished -> handlePaymentSdkResult(intent.outcome)
            PlanSelectionIntent.DismissError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadPlans() {
        val plans = getSubscriptionPlansUseCase()
        _state.update {
            it.copy(
                isLoadingPlans = false,
                plans = plans,
                selectedPlanId = it.selectedPlanId ?: plans.firstOrNull { plan -> plan.isMostPopular }?.id ?: plans.firstOrNull()?.id,
            )
        }
    }

    private fun selectPlan(plan: SubscriptionPlan) {
        _state.update { it.copy(selectedPlanId = plan.id) }
    }

    private fun confirmPurchase() {
        val current = _state.value
        val plan = current.selectedPlan ?: return
        if (!current.canConfirmPurchase) {
            _state.update { it.copy(errorMessage = "Please fill in your name, a valid email and phone number.") }
            return
        }

        _state.update { it.copy(isProcessingPayment = true, errorMessage = null) }

        viewModelScope.launch {
            val billingData = BillingData(
                firstName = current.fullName.substringBefore(" "),
                lastName = current.fullName.substringAfter(" ", missingDelimiterValue = current.fullName),
                email = current.email,
                phoneNumber = current.phoneNumber,
            )

            when (val result = createPaymentIntentionUseCase(plan, billingData)) {
                is Result.Success -> {
                    _effect.emit(PlanSelectionEffect.LaunchPaymobCheckout(result.data))
                }
                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isProcessingPayment = false,
                            errorMessage = result.exception.message ?: "Couldn't start the checkout. Please try again.",
                        )
                    }
                }
                Result.Loading -> Unit
            }
        }
    }

    private fun handlePaymentSdkResult(outcome: PaymentOutcome) {
        when (outcome) {
            is PaymentOutcome.Completed -> activateSubscription(outcome)
            is PaymentOutcome.Failed -> _state.update {
                it.copy(isProcessingPayment = false, errorMessage = outcome.message)
            }
            PaymentOutcome.Cancelled -> _state.update {
                it.copy(isProcessingPayment = false)
            }
        }
    }

    private fun activateSubscription(outcome: PaymentOutcome.Completed) {
        val plan = _state.value.selectedPlan ?: return

        viewModelScope.launch {
            when (val result = verifyAndActivateSubscriptionUseCase(plan, outcome.rawFields)) {
                is Result.Success -> {
                    _state.update { it.copy(isProcessingPayment = false) }
                    _effect.emit(PlanSelectionEffect.ShowSnackbar("You're subscribed to the ${plan.title} plan!"))
                    _effect.emit(PlanSelectionEffect.NavigateToMain)
                }
                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isProcessingPayment = false,
                            errorMessage = result.exception.message
                                ?: "We couldn't verify your payment. If you were charged, contact support.",
                        )
                    }
                }
                Result.Loading -> Unit
            }
        }
    }
}
