package com.example.phasmatic.ui.Forum.new_review

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.phasmatic.ui.modeSelection.ProfileAvatar
import com.example.phasmatic.ui.modeSelection.ProfileMenuDropdown
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun NewReviewScreen(
    selectedType: String,
    countries: List<String>,
    universities: List<String>,
    selectedCountry: String?,
    selectedUniversity: String?,
    ratingText: String,
    reviewText: String,
    isSaving: Boolean,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onTypeSelected: (String) -> Unit,
    onCountrySelected: (String?) -> Unit,
    onUniversitySelected: (String?) -> Unit,
    onRatingTextChange: (String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSaveClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showCountryDropdown by remember { mutableStateOf(false) }
    var showUniversityDropdown by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PureWhite
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedMeshBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = InkDeep
                        )
                    }

                    Box {
                        ProfileAvatar(
                            imageUrl = profileImageUrl,
                            profileBitmap = profileBitmap,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                menuExpanded = true
                            }
                        )

                        ProfileMenuDropdown(
                            expanded = menuExpanded,
                            onDismiss = { menuExpanded = false },
                            onChatClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onChatClick()
                            },
                            onConferenceClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onConferenceClick()
                            },
                            onNotesClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNotesClick()
                            },
                            onLogoutClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLogoutClick()
                            },
                            onAccountClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onProfileClick()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "NEW REVIEW",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        letterSpacing = 6.sp,
                        brush = Brush.linearGradient(
                            colors = listOf(InkDeep, OrchidPrimary)
                        )
                    )
                )

                Spacer(Modifier.height(28.dp))

                AnimatedShimmerTitle("SELECT TYPE")
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TypeFilterChip(
                        label = "Erasmus",
                        isSelected = selectedType == "Erasmus",
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onTypeSelected("Erasmus")
                        }
                    )
                    TypeFilterChip(
                        label = "Master",
                        isSelected = selectedType == "Master",
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onTypeSelected("Master")
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))

                AnimatedShimmerTitle("LOCATION & UNIVERSITY")
                Spacer(Modifier.height(16.dp))

                PremiumDropdown(
                    label = "Country",
                    value = selectedCountry ?: "Select Country",
                    expanded = showCountryDropdown,
                    onExpandChange = { showCountryDropdown = it },
                    onDismiss = { showCountryDropdown = false },
                    items = listOf("Select Country") + countries,
                    onItemClick = { country ->
                        if (country == "Select Country") {
                            onCountrySelected(null)
                        } else {
                            onCountrySelected(country)
                        }
                        showCountryDropdown = false
                    }
                )

                Spacer(Modifier.height(12.dp))

                PremiumDropdown(
                    label = "University",
                    value = selectedUniversity ?: "Select University",
                    expanded = showUniversityDropdown,
                    enabled = !universities.isEmpty(),
                    onExpandChange = { showUniversityDropdown = it },
                    onDismiss = { showUniversityDropdown = false },
                    items = listOf("Select University") + universities,
                    onItemClick = { uni ->
                        if (uni == "Select University") {
                            onUniversitySelected(null)
                        } else {
                            onUniversitySelected(uni)
                        }
                        showUniversityDropdown = false
                    }
                )

                Spacer(Modifier.height(24.dp))

                AnimatedShimmerTitle("RATING (1-5)")
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = ratingText,
                    onValueChange = onRatingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter rating (1-5)") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = OrchidPrimary
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrchidPrimary,
                        unfocusedBorderColor = Color(0xFFE9D5F5),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(Modifier.height(24.dp))

                AnimatedShimmerTitle("YOUR REVIEW")
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = reviewText,
                    onValueChange = onReviewTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    placeholder = { Text("Share your experience...") },
                    trailingIcon = {
                        IconButton(onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onVoiceClick()
                        }) {
                            Icon(
                                Icons.Default.Mic,
                                contentDescription = "Voice input",
                                tint = OrchidPrimary
                            )
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrchidPrimary,
                        unfocusedBorderColor = Color(0xFFE9D5F5),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    maxLines = 8
                )

                Spacer(Modifier.height(32.dp))

                PremiumPrimaryButton(
                    text = if (isSaving) "SAVING..." else "POST REVIEW",
                    enabled = !isSaving,
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onSaveClick()
                    }
                )

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun TypeFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "chipScale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier.scale(scale),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) OrchidPrimary else OrchidLight,
        border = BorderStroke(1.dp, if (isSelected) OrchidPrimary else Color(0xFFE9D5F5))
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) Color.White else InkDeep,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumDropdown(
    label: String,
    value: String,
    expanded: Boolean,
    enabled: Boolean = true,
    onExpandChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) onExpandChange(it) }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = OrchidPrimary
                )
            },
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrchidPrimary,
                unfocusedBorderColor = Color(0xFFE9D5F5),
                focusedLabelColor = OrchidPrimary,
                disabledBorderColor = Color(0xFFF5F5F5),
                disabledTextColor = Color.Gray,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier.background(PureWhite)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun PremiumPrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "buttonScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = InkDeep,
            disabledContainerColor = InkDeep.copy(alpha = 0.45f)
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.6.sp
        )
    }
}

@Composable
fun AnimatedShimmerTitle(text: String) {
    val shimmerColors = listOf(InkBlack, InkBlack, OrchidPrimary, InkBlack, InkBlack)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing)
        ),
        label = "shimmerMove"
    )

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(translateAnim - 500f, translateAnim - 500f),
                end = Offset(translateAnim, translateAnim)
            )
        )
    )
}

@Composable
fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(
                    size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))),
                    size.height * 0.12f
                ),
                radius = 1100f
            )
        )
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    bitmap: Bitmap?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(2.dp, OrchidPrimary, CircleShape)
            .clickable { onClick() }
    ) {
        when {
            !imageUrl.isNullOrEmpty() -> {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Glide.with(context).load(imageUrl).circleCrop().into(this)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            bitmap != null -> {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            setImageBitmap(bitmap)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = OrchidPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}
