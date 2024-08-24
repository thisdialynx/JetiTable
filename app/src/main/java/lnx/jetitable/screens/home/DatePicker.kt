package lnx.jetitable.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.getCurrentDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended(context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {}
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    Text(
        text = datePickerState.selectedDateMillis?.let { millis ->
            val formatter = remember { DatePickerDefaults.dateFormatter() }
            val locale = Locale.getDefault()
            formatter.formatDate(millis, locale)
        } ?: getCurrentDate(),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable {
            showDialog = true
            Toast.makeText(context, R.string.in_development, Toast.LENGTH_SHORT).show()
        }
    )
}