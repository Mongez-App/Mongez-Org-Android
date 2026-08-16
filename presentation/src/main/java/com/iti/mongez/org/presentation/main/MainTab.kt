package com.iti.mongez.org.presentation.main

import androidx.annotation.DrawableRes
import com.iti.mongez.org.designsystem.R

enum class MainTab(
    val label: String,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
) {
    Dashboard(
        label = "Home",
        unselectedIcon = R.drawable.ic_home_unfilled,
        selectedIcon = R.drawable.ic_home_filled
    ),
    Teams(
        label = "Teams",
        unselectedIcon = R.drawable.ic_teams_unfilled,
        selectedIcon = R.drawable.ic_teams_filled
    ),
    Profile(
        label = "Profile",
        unselectedIcon = R.drawable.ic_profile_unfilled,
        selectedIcon = R.drawable.ic_profile_filled
    );

    companion object {
        fun fromIndex(index: Int): MainTab {
            return entries.getOrElse(index) { Dashboard }
        }
    }
}
