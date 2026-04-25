package com.example.phasmatic.ui.conference.general_conference

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.phasmatic.data.model.User
import com.example.phasmatic.ui.modeSelection.OrchidPrimary
import com.example.phasmatic.ui.modeSelection.ProfileAvatar
import com.example.phasmatic.ui.modeSelection.ProfileMenuDropdown
import com.example.phasmatic.ui.modeSelection.PureWhite
import com.example.phasmatic.ui.modeSelection.SoftPinkGlow
import kotlin.math.sin

private val InkBlack = Color(0xFF000000)
private val InkDeep = Color(0xFF1E1B4B)
private val HeroIndigoEnd = Color(0xFF312E81)
private val OrchidPrimary = Color(0xFFD946EF)
private val OrchidLight = Color(0xFFFDF4FF)
private val SoftPinkGlow = Color(0xFFFFE4FF)
private val PureWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF16A34A)

@Composable
fun GeneralConferenceScreen(
    userFullName: String?,
    searchQuery: String,
    users: List<User>,
    selectedUserIds: List<String>,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    showJoinDialog: Boolean,
    joinCode: String,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onToggleUser: (User) -> Unit,
    onConfirmClick: () -> Unit,
    onJoinClick: () -> Unit,
    onJoinCodeChange: (String) -> Unit,
    onDismissJoinDialog: () -> Unit,
    onConfirmJoin: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val anchorView = LocalView.current
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        AnimatedMeshBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onBackClick()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, SoftPinkGlow, CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = InkDeep)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "DECYRA",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 26.sp,
                            letterSpacing = 7.sp,
                            brush = Brush.linearGradient(listOf(InkDeep, OrchidPrimary))
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

            Spacer(Modifier.height(24.dp))

            Spacer(Modifier.height(24.dp))

            SearchField(
                value = searchQuery,
                onValueChange = onSearchChange
            )

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    modifier = Modifier.weight(1f),
                    text = "SCHEDULE",
                    icon = Icons.Default.CalendarMonth,
                    onClick = onConfirmClick
                )
                SecondaryActionButton(
                    modifier = Modifier.weight(1f),
                    text = "JOIN ROOM",
                    icon = Icons.Outlined.VideoCall,
                    onClick = onJoinClick
                )
            }

            Spacer(Modifier.height(20.dp))

            AnimatedShimmerTitle(text = "SELECT PARTICIPANTS")
            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 30.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(users, key = { it.id ?: it.fullName.orEmpty() }) { user ->
                    SelectableUserCard(
                        user = user,
                        selected = selectedUserIds.contains(user.id),
                        onToggle = { onToggleUser(user) }
                    )
                }
            }
        }
    }

    if (showJoinDialog) {
        AlertDialog(
            onDismissRequest = onDismissJoinDialog,
            title = {
                Text("Enter Room Code", fontWeight = FontWeight.Bold, color = InkDeep)
            },
            text = {
                OutlinedTextField(
                    value = joinCode,
                    onValueChange = onJoinCodeChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Code...") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrchidPrimary,
                        unfocusedBorderColor = SoftPinkGlow,
                        cursorColor = OrchidPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirmJoin) {
                    Text("Join", color = OrchidPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissJoinDialog) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        placeholder = { Text("Search participants") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = OrchidPrimary)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrchidPrimary,
            unfocusedBorderColor = SoftPinkGlow,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = OrchidPrimary
        )
    )
}

@Composable
private fun SelectableUserCard(
    user: User,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = 300f),
        label = "conferenceUserScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onToggle
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) OrchidLight else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (selected) OrchidPrimary.copy(alpha = 0.35f) else Color(0xFFF1F5F9)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(url = user.profileImageUrl)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName ?: "User",
                    color = if (user.isGray) Color.Gray else InkDeep,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (selected) "Selected for conference" else "Tap to include in this room",
                    color = if (selected) OrchidPrimary else Color.Gray,
                    fontSize = 12.sp
                )
            }
            Checkbox(
                checked = selected,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f, label = "actionScale")

    Button(
        onClick = onClick,
        modifier = modifier
            .height(58.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = InkDeep)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun SecondaryActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(22.dp),
        color = OrchidLight,
        border = BorderStroke(1.dp, OrchidPrimary.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = OrchidPrimary)
            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                color = InkDeep,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun UserAvatar(url: String?) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(2.dp, OrchidPrimary.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!url.isNullOrEmpty()) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context)
                            .load(url)
                            .placeholder(com.example.phasmatic.R.drawable.baseline_face_24)
                            .error(com.example.phasmatic.R.drawable.baseline_face_24)
                            .into(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(Icons.Default.Person, contentDescription = null, tint = OrchidPrimary)
        }
    }
}

@Composable
private fun ProfileAnchorAvatar(
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
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            bitmap != null -> androidx.compose.foundation.Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            !imageUrl.isNullOrEmpty() -> AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context).load(imageUrl).circleCrop().into(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            else -> Icon(Icons.Default.Person, contentDescription = null, tint = OrchidPrimary)
        }
    }
}

@Composable
private fun AnimatedShimmerTitle(text: String) {
    val shimmerColors = listOf(InkBlack, InkBlack, OrchidPrimary, InkBlack, InkBlack)
    val transition = rememberInfiniteTransition(label = "conferenceShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "conferenceShimmerAnim"
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
private fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "conferenceMesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "conferenceWave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(
                    size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))),
                    size.height * 0.1f
                ),
                radius = 1100f
            )
        )
    }
}

@Composable
private fun NeuralConferenceAura() {
    val infiniteTransition = rememberInfiniteTransition(label = "conferenceAura")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "conferenceAuraRotation"
    )

    Box(
        modifier = Modifier
            .size(84.dp)
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(OrchidPrimary, Color.Transparent, OrchidPrimary)
                ),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(OrchidPrimary.copy(alpha = 0.28f), Color.Transparent)
                ),
                radius = size.minDimension / 1.5f
            )
        }
        Icon(Icons.Default.Groups, contentDescription = null, tint = OrchidPrimary, modifier = Modifier.size(30.dp))
    }
}