package com.example.phasmatic.ui.Chat.chat

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.phasmatic.data.model.Message
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
val UserBubble = Color(0xFF312E81)
val OtherBubble = Color(0xFFFFFFFF)

@Composable
fun ChatScreen(
    currentUid: String?,
    otherName: String,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    messages: List<Message>,
    messageText: String,
    onMessageChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSendClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = PureWhite,
        bottomBar = {
            MessageInputBar(
                value = messageText,
                onValueChange = onMessageChange,
                onVoiceClick = onVoiceClick,
                onSendClick = onSendClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedMeshBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                ChatTopBar(
                    title = otherName,
                    imageUrl = profileImageUrl,
                    bitmap = profileBitmap,
                    onBackClick = onBackClick,
                    onProfileClick = onProfileClick,
                    onChatClick = onChatClick,
                    onConferenceClick = onConferenceClick,
                    onNotesClick = onNotesClick,
                    onLogoutClick = onLogoutClick
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages, key = { it.id ?: it.hashCode() }) { message ->
                        val isMine = currentUid != null && currentUid == message.sender_id
                        MessageBubble(
                            message = message.messageText ?: "",
                            time = message.timeMessage ?: "",
                            isMine = isMine
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatTopBar(
    title: String,
    imageUrl: String?,
    bitmap: Bitmap?,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    val haptic = LocalHapticFeedback.current
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
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
            Spacer(Modifier.size(6.dp))
            Column {
                Text(
                    text = title,
                    color = InkDeep,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )
                Text(
                    text = "Live conversation",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Box {
            ProfileAvatar(
                imageUrl = imageUrl,
                profileBitmap = bitmap,
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

@Composable
fun MessageBubble(
    message: String,
    time: String,
    isMine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 22.dp,
                bottomStart = if (isMine) 22.dp else 6.dp,
                bottomEnd = if (isMine) 6.dp else 22.dp
            ),
            color = if (isMine) UserBubble else OtherBubble,
            border = if (isMine) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1E8F7)),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(0.78f)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message,
                    color = if (isMine) Color.White else InkDeep,
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = time,
                    color = if (isMine) Color.White.copy(alpha = 0.65f) else Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun MessageInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrchidPrimary,
                unfocusedBorderColor = Color(0xFFE9D5F5),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(Modifier.size(10.dp))

        MiniActionButton(
            icon = Icons.Default.Mic,
            onClick = onVoiceClick
        )

        Spacer(Modifier.size(8.dp))

        MiniActionButton(
            icon = Icons.Default.Send,
            onClick = onSendClick,
            filled = true
        )
    }
}

@Composable
fun MiniActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    filled: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(stiffness = 220f),
        label = "miniBtnScale"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(if (filled) OrchidPrimary else OrchidLight)
            .border(
                width = if (filled) 0.dp else 1.dp,
                color = if (filled) Color.Transparent else Color(0xFFE9D5F5),
                shape = CircleShape
            )
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (filled) Color.White else OrchidPrimary
        )
    }
}

@Composable
fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing)
        ),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.10f), Color.Transparent),
                center = Offset(
                    size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))),
                    size.height * 0.10f
                ),
                radius = 1000f
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

