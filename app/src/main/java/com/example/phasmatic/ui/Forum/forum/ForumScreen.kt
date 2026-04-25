package com.example.phasmatic.ui.Forum.forum

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.phasmatic.data.model.ForumReview
import com.example.phasmatic.ui.modeSelection.OrchidPrimary
import com.example.phasmatic.ui.modeSelection.ProfileAvatar
import com.example.phasmatic.ui.modeSelection.ProfileMenuDropdown
import com.example.phasmatic.ui.modeSelection.PureWhite
import com.example.phasmatic.ui.modeSelection.SoftPinkGlow
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)
val StarGold = Color(0xFFFBBF24)

@Composable
fun ForumScreen(
    reviews: List<ForumReview>,
    countries: List<String>,
    universities: List<String>,
    selectedType: String?,
    selectedCountry: String?,
    selectedUniversity: String?,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    currentUserId: String?,
    onBackClick: () -> Unit,
    onTypeSelected: (String) -> Unit,
    onCountrySelected: (String?) -> Unit,
    onUniversitySelected: (String?) -> Unit,
    onAddReviewClick: () -> Unit,
    onReviewClick: (ForumReview) -> Unit,
    onLikeToggle: (ForumReview, Boolean) -> Unit,
    isReviewLiked: (ForumReview) -> Boolean,
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
        containerColor = PureWhite,
        floatingActionButton = {
            PremiumFAB(onClick = {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                onAddReviewClick()
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedMeshBackground()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = InkDeep
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "FORUM",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 28.sp,
                                    letterSpacing = 6.sp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(InkDeep, OrchidPrimary)
                                    )
                                )
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
                }

                item { Spacer(Modifier.height(24.dp)) }

                item {
                    AnimatedShimmerTitle("FILTERS")
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TypeFilterChip(
                            label = "Erasmus",
                            isSelected = selectedType == "erasmus",
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onTypeSelected("erasmus")
                            }
                        )
                        TypeFilterChip(
                            label = "Master",
                            isSelected = selectedType == "master",
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onTypeSelected("master")
                            }
                        )
                    }
                }

                item { Spacer(Modifier.height(14.dp)) }

                item {
                    Column {
                        PremiumDropdown(
                            label = "Country",
                            value = selectedCountry ?: "All Countries",
                            expanded = showCountryDropdown,
                            onExpandChange = { showCountryDropdown = it },
                            onDismiss = { showCountryDropdown = false },
                            items = listOf("All Countries") + countries,
                            onItemClick = { country ->
                                if (country == "All Countries") {
                                    onCountrySelected(null)
                                } else {
                                    onCountrySelected(country)
                                }
                                showCountryDropdown = false
                            }
                        )

                        Spacer(Modifier.height(10.dp))

                        PremiumDropdown(
                            label = "University",
                            value = selectedUniversity ?: "All Universities",
                            expanded = showUniversityDropdown,
                            enabled = !universities.isEmpty(),
                            onExpandChange = { showUniversityDropdown = it },
                            onDismiss = { showUniversityDropdown = false },
                            items = listOf("All Universities") + universities,
                            onItemClick = { uni ->
                                if (uni == "All Universities") {
                                    onUniversitySelected(null)
                                } else {
                                    onUniversitySelected(uni)
                                }
                                showUniversityDropdown = false
                            }
                        )
                    }
                }

                item { Spacer(Modifier.height(28.dp)) }

                item {
                    AnimatedShimmerTitle("REVIEWS (${reviews.size})")
                    Spacer(Modifier.height(18.dp))
                }

                if (reviews.isEmpty()) {
                    item {
                        EmptyReviewsState()
                    }
                } else {
                    items(reviews, key = { it.id ?: it.hashCode() }) { review ->
                        ReviewCard(
                            review = review,
                            isLiked = isReviewLiked(review),
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onReviewClick(review)
                            },
                            onLikeClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onLikeToggle(review, isReviewLiked(review))
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
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
fun ReviewCard(
    review: ForumReview,
    isLiked: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "cardScale"
    )

    val title = when {
        review.type?.equals("erasmus", ignoreCase = true) == true -> "Erasmus · ${review.university}"
        review.type?.equals("master", ignoreCase = true) == true -> "Master · ${review.university}"
        else -> review.university ?: "Unknown"
    }

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1E8F7)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = InkDeep
            )

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "by ${review.user_name ?: "Anonymous"}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.weight(1f))
                RatingStars(rating = review.rating)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = review.text ?: "",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color(0xFFEF4444) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = review.likes.toString(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = review.comments.toString(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Visibility,
                        contentDescription = "Views",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = review.views.toString(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun RatingStars(rating: Float) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = StarGold,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun EmptyReviewsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.Forum,
            contentDescription = null,
            tint = OrchidPrimary.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "No reviews found",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = InkDeep
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Try adjusting your filters",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PremiumFAB(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fabScale"
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .scale(scale),
        containerColor = OrchidPrimary,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add review",
            modifier = Modifier.size(28.dp)
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