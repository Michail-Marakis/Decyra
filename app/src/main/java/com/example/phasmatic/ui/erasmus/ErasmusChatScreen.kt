package com.example.phasmatic.ui.erasmus

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phasmatic.ui.modeSelection.ProfileAvatar
import com.example.phasmatic.ui.modeSelection.ProfileMenuDropdown
import com.example.phasmatic.ui.Forum.forum.AnimatedMeshBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

// --- PREMIUM COLORS ---
private val OrchidPrimary = Color(0xFFD946EF)
private val OrchidSecondary = Color(0xFFA855F7)
private val OrchidGradient = Brush.linearGradient(listOf(OrchidPrimary, OrchidSecondary))
private val AssistantBubbleBg = Color(0xFFFDF4FF)
private val PureWhite = Color(0xFFFFFFFF)
private val InkDeep = Color(0xFF0F172A)
private val SoftGray = Color(0xFFF1F5F9)
private val HintGray = Color(0xFF94A3B8)

@Composable
fun ErasmusChatScreen(
    userFullName: String?,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    inputText: String,
    messages: List<String>,
    isSending: Boolean,
    onInputChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSaveClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        if (inputText.isNotBlank() && messages.isEmpty()) {
            onSendClick()
        }
    }

    LaunchedEffect(messages.size, isSending) {
        if (messages.isNotEmpty() || isSending) {
            listState.animateScrollToItem(if (isSending) messages.size else messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PureWhite,
        topBar = {
            ErasmusTopBar(
                title = "Erasmus Mentor",
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = onBackClick,
                onAvatarClick = { menuExpanded = true }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedMeshBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
                ) {
                    itemsIndexed(messages) { index, message ->
                        val isAssistant = message.startsWith("Assistant:")
                        val cleanText = if (isAssistant) message.substringAfter("Assistant:\n") else message.substringAfter("You: ")

                        ChatBubble(
                            message = cleanText,
                            isUser = !isAssistant,
                            isLatestAssistant = index == messages.size - 1 && isAssistant,
                            onCharTyped = {
                                scope.launch { listState.scrollToItem(messages.size - 1) }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (isSending) {
                        item {
                            ChatBubble(
                                message = "",
                                isUser = false,
                                isLatestAssistant = true,
                                onCharTyped = {}
                            )
                        }
                    }
                }

                ErasmusInputArea(
                    inputText = inputText,
                    onInputChange = onInputChange,
                    onSendClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSendClick()
                    },
                    onVoiceClick = onVoiceClick,
                    onSaveClick = onSaveClick
                )
            }

            Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 16.dp, top = 8.dp)) {
                ProfileMenuDropdown(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onAccountClick = onProfileClick,
                    onChatClick = onChatClick,
                    onConferenceClick = onConferenceClick,
                    onNotesClick = onNotesClick,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

/**
 * Η Neural Aura του Mentor.
 * Το εσωτερικό εικονίδιο γυρνάει πάντα όταν είναι idle.
 */
@Composable
fun NeuralPrismAura(isSpeaking: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition(label = "prism")

    // Εξωτερική περιστροφή (δακτύλιος)
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)), label = "outerRot"
    )

    // ΕΣΩΤΕΡΙΚΗ ΠΕΡΙΣΤΡΟΦΗ (Για το εικονίδιο - γυρνάει πάντα)
    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)), label = "innerRot"
    )

    // Περιστροφή των κόμβων (Μόνο όταν μιλάει)
    val nodeRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)), label = "nodes"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = if (isSpeaking) 1.15f else 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer {
                scaleX = pulse
                scaleY = pulse
            }
            .rotate(outerRotation) // Ο δακτύλιος γυρνάει με το Box
            .drawBehind {
                drawCircle(
                    brush = Brush.sweepGradient(listOf(OrchidPrimary, Color.Transparent, OrchidPrimary)),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
                drawCircle(
                    brush = Brush.radialGradient(listOf(OrchidPrimary.copy(0.15f), Color.Transparent)),
                    radius = size.width / 1.3f
                )

                if (isSpeaking) {
                    val radius = size.width / 2.8f
                    val angle1 = Math.toRadians(nodeRotation.toDouble())
                    val angle2 = angle1 + Math.PI

                    drawCircle(color = OrchidPrimary, radius = 5f,
                        center = androidx.compose.ui.geometry.Offset(
                            center.x + (radius * cos(angle1)).toFloat(),
                            center.y + (radius * sin(angle1)).toFloat()
                        )
                    )
                    drawCircle(color = OrchidSecondary, radius = 5f,
                        center = androidx.compose.ui.geometry.Offset(
                            center.x + (radius * cos(angle2)).toFloat(),
                            center.y + (radius * sin(angle2)).toFloat()
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Layers,
            contentDescription = null,
            tint = OrchidPrimary,
            modifier = Modifier
                .size(22.dp)
                .rotate(innerRotation - outerRotation) // Ανεξάρτητη περιστροφή του "μέσα"
        )
    }
}

@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean,
    isLatestAssistant: Boolean,
    onCharTyped: () -> Unit
) {
    var skipAnimation by remember { mutableStateOf(!isLatestAssistant) }
    val haptic = LocalHapticFeedback.current

    val alignment = if (isUser) Alignment.End else Alignment.Start
    val textColor = if (isUser) Color.White else InkDeep
    val shape = if (isUser)
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    else
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                NeuralPrismAura(isSpeaking = isLatestAssistant && !skipAnimation)
                Spacer(Modifier.width(10.dp))
            }

            Surface(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .clip(shape)
                    .clickable {
                        if (isLatestAssistant && !skipAnimation && message.isNotEmpty()) {
                            skipAnimation = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    },
                color = Color.Transparent,
                shadowElevation = 2.dp
            ) {
                Box(modifier = Modifier.background(if (isUser) OrchidGradient else Brush.linearGradient(listOf(AssistantBubbleBg, AssistantBubbleBg))).padding(14.dp)) {
                    Column {
                        if (isLatestAssistant && !skipAnimation) {
                            if (message.isEmpty()) {
                                ThinkingShimmerText()
                            } else {
                                TypewriterText(text = message, color = textColor, onCharTyped = onCharTyped, onFinish = {
                                    skipAnimation = true
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                })
                                Spacer(modifier = Modifier.height(10.dp))
                                FastForwardBadge()
                            }
                        } else {
                            Text(text = message, color = textColor, fontSize = 15.sp, lineHeight = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThinkingShimmerText() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse), label = "alpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Analyzing", color = OrchidPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black, modifier = Modifier.graphicsLayer { this.alpha = alpha })
        repeat(3) { index ->
            val dotAlpha by infiniteTransition.animateFloat(
                initialValue = 0f, targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(400, delayMillis = index * 150), RepeatMode.Reverse), label = "dot"
            )
            Text(".", color = OrchidPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black, modifier = Modifier.graphicsLayer { this.alpha = dotAlpha })
        }
    }
}

@Composable
fun FastForwardBadge() {
    Row(
        modifier = Modifier.background(OrchidPrimary.copy(alpha = 0.15f), RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.FastForward, null, tint = OrchidPrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("FAST FORWARD", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = OrchidPrimary, letterSpacing = 0.5.sp)
    }
}

@Composable
fun TypewriterText(text: String, color: Color, onCharTyped: () -> Unit, onFinish: () -> Unit) {
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        displayedText = ""
        text.forEach { char ->
            displayedText += char
            onCharTyped()
            delay(20)
        }
        onFinish()
    }
    Text(text = displayedText, color = color, fontSize = 15.sp, lineHeight = 22.sp)
}

@Composable
fun ErasmusTopBar(title: String, profileImageUrl: String?, profileBitmap: Bitmap?, onBackClick: () -> Unit, onAvatarClick: () -> Unit) {
    Surface(color = PureWhite, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, null, tint = InkDeep) }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Black, color = InkDeep)) { append("Erasmus ") }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light, color = OrchidPrimary)) { append("Mentor") }
                    },
                    fontSize = 22.sp
                )
                Surface(
                    color = OrchidPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("AI", color = OrchidPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
            ProfileAvatar(imageUrl = profileImageUrl, profileBitmap = profileBitmap, onClick = onAvatarClick)
        }
    }
}

@Composable
fun ErasmusInputArea(inputText: String, onInputChange: (String) -> Unit, onSendClick: () -> Unit, onVoiceClick: () -> Unit, onSaveClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = PureWhite, shadowElevation = 15.dp) {
        Row(
            modifier = Modifier.padding(12.dp).navigationBarsPadding().imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSaveClick) { Icon(Icons.Outlined.Save, null, tint = HintGray) }
            IconButton(onClick = onVoiceClick) { Icon(Icons.Default.Mic, null, tint = OrchidPrimary) }

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                placeholder = { Text("How can I help you?", color = HintGray) },
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = InkDeep, unfocusedTextColor = InkDeep,
                    focusedContainerColor = SoftGray, unfocusedContainerColor = SoftGray,
                    focusedBorderColor = OrchidPrimary, unfocusedBorderColor = Color.Transparent
                )
            )

            FloatingActionButton(onClick = onSendClick, containerColor = OrchidPrimary, contentColor = PureWhite, shape = CircleShape, modifier = Modifier.size(52.dp)) {
                Icon(Icons.Default.Send, null)
            }
        }
    }
}