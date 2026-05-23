package com.example.decyra.frontend.Chat.users_to_chat

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.decyra.backend.domain.Conversation
import com.example.decyra.frontend.modeSelection.ProfileAvatar
import com.example.decyra.frontend.modeSelection.ProfileMenuDropdown
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun UsersScreen(
    conversations: List<Conversation>,
    currentUid: String?,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    resolveOtherUserName: (String) -> String,
    resolveOtherUserImage: (String) -> String?,
    onBackClick: () -> Unit,
    onConversationClick: (Conversation) -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var menuExpanded by remember { mutableStateOf(false) }

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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
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
                            Spacer(Modifier.size(8.dp))
                            Text(
                                text = "MESSAGES",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 26.sp,
                                    letterSpacing = 5.sp,
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

                item { Spacer(Modifier.height(26.dp)) }

//                item {
//                    AnimatedShimmerTitle("YOUR CONVERSATIONS")
//                    Spacer(Modifier.height(18.dp))
//                }

                if (conversations.isEmpty()) {
                    item {
                        EmptyConversationsState()
                    }
                } else {
                    items(
                        items = conversations,
                        key = { c ->
                            val left = c.leftUser_id ?: "left"
                            val right = c.rightUser_id ?: "right"
                            left + "_" + right + "_" + (c.timeLastMessage ?: "0")
                        }
                    ) { conversation ->
                        val otherUid = if (currentUid == conversation.leftUser_id) {
                            conversation.rightUser_id
                        } else {
                            conversation.leftUser_id
                        }

                        ConversationCard(
                            conversation = conversation,
                            otherUserName = if (!otherUid.isNullOrBlank()) resolveOtherUserName(otherUid) else "User",
                            otherUserImage = if (!otherUid.isNullOrBlank()) resolveOtherUserImage(otherUid) else null,
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onConversationClick(conversation)
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
fun ConversationCard(
    conversation: Conversation,
    otherUserName: String,
    otherUserImage: String?,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = 220f),
        label = "conversationScale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1E8F7)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkAvatar(
                imageUrl = otherUserImage,
                modifier = Modifier.size(56.dp)
            )

            Spacer(Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = otherUserName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = InkDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = OrchidPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = conversation.lastMessage ?: "No messages yet",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.size(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = conversation.timeLastMessage ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun EmptyConversationsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.ChatBubbleOutline,
            contentDescription = null,
            tint = OrchidPrimary.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No conversations yet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = InkDeep
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Your chats will appear here",
            fontSize = 14.sp,
            color = Color.Gray
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
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
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

@Composable
fun NetworkAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(2.dp, OrchidPrimary.copy(alpha = 0.45f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context).load(imageUrl).circleCrop().into(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = OrchidPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
    }
}
