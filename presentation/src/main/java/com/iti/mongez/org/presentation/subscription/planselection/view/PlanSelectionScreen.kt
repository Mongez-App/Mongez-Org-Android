package com.iti.mongez.org.presentation.subscription.planselection.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.card.AppCard
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.domain.subscription.model.BillingCycle
import com.iti.mongez.org.domain.subscription.model.SubscriptionPlan
import com.iti.mongez.org.presentation.subscription.planselection.contract.PlanSelectionIntent
import com.iti.mongez.org.presentation.subscription.planselection.uiState.PlanSelectionUiState

/**
 * Dumb MVI view: renders [state] and forwards user actions via [onIntent] only.
 * ViewModel hoisting and effect handling (launching the Paymob SDK, navigation, snackbars)
 * live in AppNavHost, matching this project's other screens (e.g. LoginScreen).
 */
@Composable
fun PlanSelectionScreen(
    state: PlanSelectionUiState,
    onIntent: (PlanSelectionIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .safeDrawingPadding(),
        contentPadding = PaddingValues(Theme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg),
    ) {
        item {
            Text(
                text = "Choose your plan",
                style = Theme.typography.headline.small,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Unlock every team, course and Magic Box schedule.",
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.secondary,
                modifier = Modifier.padding(top = Theme.spacing.xxs),
            )
        }

        items(state.plans, key = { it.id }) { plan ->
            PlanCard(
                plan = plan,
                isSelected = plan.id == state.selectedPlanId,
                onClick = { onIntent(PlanSelectionIntent.SelectPlan(plan)) },
            )
        }

        item {
            BillingForm(state = state, onIntent = onIntent)
        }

        state.errorMessage?.let { message ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Theme.radius.md))
                        .background(Theme.colorScheme.state.errorContainer)
                        .padding(start = Theme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = message,
                        style = Theme.typography.body.small,
                        color = Theme.colorScheme.state.error,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { onIntent(PlanSelectionIntent.DismissError) }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Dismiss",
                            tint = Theme.colorScheme.state.error,
                        )
                    }
                }
            }
        }

        item {
            val plan = state.selectedPlan
            val priceLabel = plan?.let { "Pay ${it.priceEgp.formatEgp()} ${it.currency}" } ?: "Continue"
            AppButton(
                text = priceLabel,
                onClick = { onIntent(PlanSelectionIntent.ConfirmPurchase) },
                enabled = state.canConfirmPurchase,
                isLoading = state.isProcessingPayment,
            )
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AppCard(
        onClick = onClick,
        modifier = if (isSelected) {
            Modifier.border(2.dp, Theme.colorScheme.brand.primary, RoundedCornerShape(Theme.radius.xl))
        } else {
            Modifier
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SelectionIndicator(isSelected = isSelected)
            Spacer(modifier = Modifier.width(Theme.spacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.title,
                        style = Theme.typography.title.medium,
                        fontWeight = FontWeight.SemiBold,
                        color = Theme.colorScheme.text.primary,
                    )
                    if (plan.isMostPopular) {
                        Box(
                            modifier = Modifier
                                .padding(start = Theme.spacing.sm)
                                .clip(RoundedCornerShape(Theme.radius.full))
                                .background(Theme.colorScheme.brand.primary)
                                .padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.xxs),
                        ) {
                            Text(
                                text = "Best value",
                                style = Theme.typography.label.small,
                                color = Theme.colorScheme.brand.onPrimary,
                            )
                        }
                    }
                }
                Text(
                    text = plan.description,
                    style = Theme.typography.body.small,
                    color = Theme.colorScheme.text.secondary,
                    modifier = Modifier.padding(top = Theme.spacing.xxs),
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${plan.priceEgp.formatEgp()} ${plan.currency}",
                    style = Theme.typography.title.medium,
                    fontWeight = FontWeight.SemiBold,
                    color = Theme.colorScheme.brand.primary,
                )
                Text(
                    text = "/ ${plan.billingCycle.label()}",
                    style = Theme.typography.label.small,
                    color = Theme.colorScheme.text.secondary,
                )
            }
        }
    }
}

@Composable
private fun SelectionIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(if (isSelected) Theme.colorScheme.brand.primary else Theme.colorScheme.surface.background)
            .border(
                width = 1.5.dp,
                color = if (isSelected) Theme.colorScheme.brand.primary else Theme.colorScheme.border.primary,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = Theme.colorScheme.brand.onPrimary,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun BillingForm(
    state: PlanSelectionUiState,
    onIntent: (PlanSelectionIntent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing.sm)) {
        Text(
            text = "Billing details",
            style = Theme.typography.title.small,
            fontWeight = FontWeight.SemiBold,
            color = Theme.colorScheme.text.primary,
        )
        AppTextField(
            value = state.fullName,
            onValueChange = { onIntent(PlanSelectionIntent.FullNameChanged(it)) },
            label = "Full name",
            placeholder = "Your full name",
        )
        AppTextField(
            value = state.email,
            onValueChange = { onIntent(PlanSelectionIntent.EmailChanged(it)) },
            label = "Email",
            placeholder = "youremail@example.com",
            isError = state.email.isNotBlank() && !state.isEmailValid,
            errorMessage = "Please enter a valid email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        AppTextField(
            value = state.phoneNumber,
            onValueChange = { onIntent(PlanSelectionIntent.PhoneNumberChanged(it)) },
            label = "Phone number",
            placeholder = "+20 1xx xxx xxxx",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
    }
}

private fun BillingCycle.label(): String = when (this) {
    BillingCycle.MONTHLY -> "month"
    BillingCycle.QUARTERLY -> "quarter"
    BillingCycle.YEARLY -> "year"
}

private fun Double.formatEgp(): String =
    if (this % 1.0 == 0.0) this.toInt().toString() else "%.2f".format(this)

@Preview(showBackground = true)
@Composable
private fun PlanSelectionScreenPreview() {
    MongezTheme {
        PlanSelectionScreen(
            state = PlanSelectionUiState(
                isLoadingPlans = false,
                plans = listOf(
                    SubscriptionPlan(
                        id = "plan_monthly",
                        title = "Monthly",
                        description = "Full access, billed every month.",
                        priceEgp = 199.0,
                        currency = "EGP",
                        billingCycle = BillingCycle.MONTHLY,
                    ),
                    SubscriptionPlan(
                        id = "plan_yearly",
                        title = "Yearly",
                        description = "Two months free vs. paying monthly.",
                        priceEgp = 1899.0,
                        currency = "EGP",
                        billingCycle = BillingCycle.YEARLY,
                        isMostPopular = true,
                    ),
                ),
                selectedPlanId = "plan_yearly",
                fullName = "Abdullah Mohamed",
                email = "abdullah@example.com",
                phoneNumber = "+201234567890",
            ),
            onIntent = {},
        )
    }
}
