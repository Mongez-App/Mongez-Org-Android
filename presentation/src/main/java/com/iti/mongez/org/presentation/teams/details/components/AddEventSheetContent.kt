package com.iti.mongez.org.presentation.teams.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.button.AppButtonVariant
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventSheetContent(
    isLoading: Boolean,
    onAddEvent: (courseId: String, type: String, date: String) -> Unit
) {
    var selectedCourse by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var eventDateUi by remember { mutableStateOf("") }
    var eventDateIso by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Theme.spacing.xl)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)
    ) {
        Text(
            text = "Add Event",
            style = Theme.typography.title.large,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        // Event Course Dropdown (Mocked)
        AppTextField(
            value = selectedCourse,
            onValueChange = {},
            label = "Event Course",
            placeholder = "Choose a course",
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )

        // Event Type Dropdown (Mocked)
        AppTextField(
            value = selectedType,
            onValueChange = {},
            label = "Event Type",
            placeholder = "Choose a event type",
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )

        // Event Date Picker
        AppTextField(
            value = eventDateUi,
            onValueChange = {},
            label = "Event Date",
            placeholder = "DD/MM/YYYY",
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppButton(
            text = "Add course", // As per screenshot, though "Add event" makes more sense
            onClick = {
                onAddEvent(selectedCourse, selectedType, eventDateIso)
            },
            variant = AppButtonVariant.Primary,
            isLoading = isLoading
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            eventDateUi = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            eventDateIso = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
