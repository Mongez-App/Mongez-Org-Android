package com.iti.mongez.org.presentation.auth.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme

@Composable
fun DynamicListField(
    label: String,
    values: List<String>,
    onValuesChange: (List<String>) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.animateContentSize()) {
        Text(
            text = label,
            style = Theme.typography.label.large,
            color = Theme.colorScheme.text.primary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.sm))
        
        values.forEachIndexed { index, value ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm)
            ) {
                AppTextField(
                    value = value,
                    onValueChange = { newValue ->
                        val newValues = values.toMutableList()
                        newValues[index] = newValue
                        onValuesChange(newValues)
                    },
                    label = "",
                    placeholder = placeholder,
                    modifier = Modifier.weight(1f)
                )
                
                if (values.size > 1) {
                    IconButton(
                        onClick = {
                            val newValues = values.toMutableList()
                            newValues.removeAt(index)
                            onValuesChange(newValues)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove field",
                            tint = Theme.colorScheme.text.secondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Theme.spacing.sm))
        }
        
        IconButton(
            onClick = {
                val newValues = values.toMutableList()
                newValues.add("")
                onValuesChange(newValues)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add field",
                tint = Theme.colorScheme.brand.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DynamicListFieldPreview() {
    MongezTheme {
        DynamicListField(
            label = "Target Audience",
            values = listOf("Students", ""),
            onValuesChange = {},
            placeholder = "Enter target audience"
        )
    }
}
