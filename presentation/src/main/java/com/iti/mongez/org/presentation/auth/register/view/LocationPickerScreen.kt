package com.iti.mongez.org.presentation.auth.register.view

import android.Manifest
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.utils.PermissionUtils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    initialLat: Double,
    initialLng: Double,
    onLocationSelected: (lat: Double, lng: Double, address: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Cairo as default if no location
    val defaultLocation = LatLng(30.0444, 31.2357)
    val startLocation = if (initialLat != 0.0) LatLng(initialLat, initialLng) else defaultLocation
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 15f)
    }

    var currentAddress by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    
    // We use a flow to debounce the camera move events
    val cameraMoveFlow = remember { MutableStateFlow<LatLng?>(null) }
    
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    LaunchedEffect(Unit) {
        cameraMoveFlow
            .debounce(800L)
            .collectLatest { latLng ->
                if (latLng != null) {
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val addressText = address.getAddressLine(0) ?: ""
                            currentAddress = addressText
                            searchQuery = addressText
                        } else {
                            currentAddress = "Unknown Location"
                            searchQuery = "Unknown Location"
                        }
                    } catch (e: Exception) {
                        currentAddress = "Unknown Location"
                    }
                }
            }
    }

    // Trigger flow when camera stops moving
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            cameraMoveFlow.value = cameraPositionState.position.target
        }
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            try {
                fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                }
            } catch (e: SecurityException) {
                // Ignore
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        )

        // Center marker
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search, // Just using search icon as a placeholder for a pin
                contentDescription = "Center pin",
                tint = Theme.colorScheme.brand.primary,
                modifier = Modifier.size(36.dp).offset(y = (-18).dp) // offset to point exactly to center
            )
        }

        // Search Bar and Back Button
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(Theme.spacing.md)) {
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Theme.colorScheme.surface.background)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                
                Spacer(modifier = Modifier.width(Theme.spacing.sm))
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        // Optional: Debounce this too if you want live search
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.colorScheme.surface.background, RoundedCornerShape(Theme.radius.md)),
                    placeholder = { Text("Search location...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.isNotBlank()) {
                                scope.launch {
                                    try {
                                        @Suppress("DEPRECATION")
                                        val results = geocoder.getFromLocationName(searchQuery, 1)
                                        if (!results.isNullOrEmpty()) {
                                            val loc = results[0]
                                            val latLng = LatLng(loc.latitude, loc.longitude)
                                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                        }
                                    } catch (e: Exception) {
                                        // Ignore or show toast
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search Action")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Theme.colorScheme.surface.background,
                        unfocusedContainerColor = Theme.colorScheme.surface.background,
                        disabledContainerColor = Theme.colorScheme.surface.background,
                        focusedIndicatorColor = Theme.colorScheme.brand.primary,
                        unfocusedIndicatorColor = Theme.colorScheme.border.primary
                    ),
                    singleLine = true
                )
            }
        }

        // FABs
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Theme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            FloatingActionButton(
                onClick = {
                    val permission = Manifest.permission.ACCESS_FINE_LOCATION
                    if (PermissionUtils.isPermissionGranted(context, permission)) {
                        try {
                            fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { location ->
                                location?.let {
                                    val latLng = LatLng(it.latitude, it.longitude)
                                    scope.launch {
                                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                    }
                                }
                            }
                        } catch (e: SecurityException) {
                            // Ignored
                        }
                    } else {
                        requestPermissionLauncher.launch(permission)
                    }
                },
                containerColor = Theme.colorScheme.surface.background,
                contentColor = Theme.colorScheme.brand.primary
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }

            FloatingActionButton(
                onClick = {
                    val target = cameraPositionState.position.target
                    onLocationSelected(target.latitude, target.longitude, currentAddress)
                },
                containerColor = Theme.colorScheme.brand.primary,
                contentColor = Theme.colorScheme.surface.background
            ) {
                Icon(Icons.Default.Check, contentDescription = "Confirm Location")
            }
        }
    }
}
