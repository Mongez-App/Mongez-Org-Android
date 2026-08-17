package com.iti.mongez.org.domain.subscription.model

/**
 * A purchasable subscription plan as advertised to the user (monthly, yearly, ...).
 * Pure Kotlin domain model - no Android / Paymob SDK types leak in here.
 */
data class SubscriptionPlan(
    val id: String,
    val title: String,
    val description: String,
    val priceEgp: Double,
    val currency: String,
    val billingCycle: BillingCycle,
    val isMostPopular: Boolean = false,
)

private const val DAY_MILLIS = 24L * 60 * 60 * 1000

enum class BillingCycle(val cycleDurationMillis: Long) {
    MONTHLY(cycleDurationMillis = 30L * DAY_MILLIS),
    QUARTERLY(cycleDurationMillis = 90L * DAY_MILLIS),
    YEARLY(cycleDurationMillis = 365L * DAY_MILLIS),
}

/** Epoch millis at which a subscription started on [startEpochMillis] would expire. */
fun BillingCycle.expiryFrom(startEpochMillis: Long): Long = startEpochMillis + cycleDurationMillis

/**
 * Default plan catalog. There is no remote "plans" endpoint in this project, so the
 * catalog is defined locally. Prices below are PLACEHOLDERS - adjust to your real pricing.
 */
object SubscriptionPlanCatalog {
    val plans: List<SubscriptionPlan> = listOf(
        SubscriptionPlan(
            id = "plan_monthly",
            title = "Monthly",
            description = "Full access to all teams, courses and Magic Box scheduling.",
            priceEgp = 199.0,
            currency = "EGP",
            billingCycle = BillingCycle.MONTHLY,
        ),
        SubscriptionPlan(
            id = "plan_yearly",
            title = "Yearly",
            description = "Best value - two months free compared to paying monthly.",
            priceEgp = 1899.0,
            currency = "EGP",
            billingCycle = BillingCycle.YEARLY,
            isMostPopular = true,
        ),
    )
}
