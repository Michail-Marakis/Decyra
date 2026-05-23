package com.example.decyra.frontend.modeSelection

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import android.speech.tts.TextToSpeech
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import java.util.Locale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlin.math.sin
import android.graphics.Bitmap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import com.example.decyra.R

// --- PREMIUM COLOR PALETTE ---
val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidSecondary = Color(0xFF818CF8) // Προστέθηκε για τα nodes
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun ModeSelectionScreen(
    userId: String?,
    userFullName: String?,
    userEmail: String?,
    userPhone: String?,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onModeSelected: (String) -> Unit,
    onForumClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showAIAssistant by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    Box(modifier = Modifier.fillMaxSize()) {
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
                    contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp)
                ) {
                    // --- TOP BAR ---
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DECYRA",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 28.sp,
                                    letterSpacing = 8.sp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(InkDeep, OrchidPrimary)
                                    )
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
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

                    item { Spacer(Modifier.height(32.dp)) }

                    item {
                        HeroGlassCard(
                            name = userFullName?.split(" ")?.firstOrNull() ?: "User",
                            onAuraClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showAIAssistant = true
                            }
                        )
                    }

                    item { Spacer(Modifier.height(48.dp)) }

                    // --- MISSIONS SECTION ---
                    item {
                        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                            AnimatedShimmerTitle(text = "CHOOSE YOUR MISSION")

                            Spacer(Modifier.height(24.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                ModeCard(
                                    title = "Erasmus+",
                                    subtitle = "Global Academic Mobility",
                                    icon = Icons.Default.Public
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onModeSelected("erasmus")
                                }
                                ModeCard(
                                    title = "Master's Degree",
                                    subtitle = "Higher Education Research",
                                    icon = Icons.Default.School
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onModeSelected("master")
                                }
                                ModeCard(
                                    title = "Career Path",
                                    subtitle = "Professional Placement",
                                    icon = Icons.Default.AutoGraph
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onModeSelected("career")
                                }
                            }
                        }
                    }

                    item { Spacer(Modifier.height(40.dp)) }

                    item {
                        ExtremeForumButtonUnified(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onForumClick()
                        })
                    }
                }
            }
        }

        // --- AI ASSISTANT OVERLAY ---
        if (showAIAssistant) {
            AIAssistantOverlay(onDismiss = { showAIAssistant = false })
        }
    }
}

@Composable
fun AIAssistantOverlay(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var isVoiceEnabled by remember { mutableStateOf(true) }
    var isPaused by remember { mutableStateOf(false) }
    var isTtsReady by remember { mutableStateOf(false) } // Νέο state για ετοιμότητα

    val tips = listOf(
        "Tip: Use the Erasmus plus mission to explore global opportunities!",
        "Tip: You can access the Community forum to chat with peers.",
        "Tip: Long-press your avatar to quickly access account settings.",
        "Tip: Check your Notes to keep track of important research."
    )

    var currentTipIndex by remember { mutableIntStateOf(0) }

    // --- TEXT TO SPEECH SETUP ---
    val tts = remember {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.ENGLISH
                isTtsReady = true // Ενημέρωση ότι είναι έτοιμο
            }
        }
        textToSpeech
    }

    // Διαχείριση εναλλαγής tips και ομιλίας
    LaunchedEffect(currentTipIndex, isVoiceEnabled, isPaused, isTtsReady) {
        if (!isPaused && isTtsReady) { // Προσθήκη ελέγχου isTtsReady
            if (isVoiceEnabled) {
                // Μικρή καθυστέρηση για να προλάβει το UI animation να ξεκινήσει
                kotlinx.coroutines.delay(300)
                tts?.speak(tips[currentTipIndex], TextToSpeech.QUEUE_FLUSH, null, "tip_id")
            }
            kotlinx.coroutines.delay(5000)
            currentTipIndex = (currentTipIndex + 1) % tips.size
        }
    }

    // Cleanup TTS όταν κλείνει το Overlay
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InkBlack.copy(alpha = 0.75f))
            .clickable {
                tts?.stop()
                onDismiss()
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(340.dp)
                .wrapContentHeight()
                .padding(16.dp)
                .clickable(enabled = false) { },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = InkDeep.copy(alpha = 0.98f)),
            elevation = CardDefaults.cardElevation(30.dp),
            border = BorderStroke(1.dp, OrchidPrimary.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- TOP ROW ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        isVoiceEnabled = !isVoiceEnabled
                        if (!isVoiceEnabled) tts?.stop()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }) {
                        Icon(
                            imageVector = if (isVoiceEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = null,
                            tint = if (isVoiceEnabled) OrchidPrimary else Color.Gray
                        )
                    }

                    Surface(
                        color = if (isPaused) Color.White.copy(0.2f) else OrchidPrimary.copy(0.1f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = if (isPaused) "PAUSED" else "LIVE TIPS",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPaused) Color.White else OrchidPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = {
                        tts?.stop()
                        onDismiss()
                    }) {
                        Icon(Icons.Default.Close, null, tint = Color.White.copy(0.5f))
                    }
                }

                Spacer(Modifier.height(20.dp))

                NeuralPrismAura(isSpeaking = isVoiceEnabled && !isPaused && isTtsReady, onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                })

                Spacer(Modifier.height(24.dp))

                // --- INTERACTIVE TIP BOX ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isPaused = true
                                    tts?.stop()
                                    tryAwaitRelease()
                                    isPaused = false
                                }
                            )
                        }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        androidx.compose.animation.AnimatedContent(
                            targetState = tips[currentTipIndex],
                            transitionSpec = {
                                (slideInVertically { it } + fadeIn()).togetherWith(slideOutVertically { -it } + fadeOut())
                            }, label = ""
                        ) { tipText ->
                            Text(
                                text = tipText,
                                color = PureWhite,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 22.sp
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        if (!isPaused) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(0.4f).height(2.dp).clip(CircleShape),
                                color = OrchidPrimary,
                                trackColor = Color.White.copy(0.1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // --- VOICE ANIMATION & DYNAMIC HINT ---
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isVoiceEnabled && !isPaused && isTtsReady) {
                        VoiceVisualizer()

                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Tap and hold the tip to pause AI",
                            color = OrchidPrimary.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else if (isPaused) {
                        Text(
                            "Released to resume",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    } else if (!isVoiceEnabled) {
                        Text(
                            "Voice is muted",
                            color = Color.White.copy(alpha = 0.2f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    } else {
                        //Εμφάνιση κατά το loading του TTS
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = OrchidPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceVisualizer() {
    Row(
        modifier = Modifier.height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "voice")
        repeat(8) { index ->
            val height by infiniteTransition.animateFloat(
                initialValue = 10f,
                targetValue = 40f,
                animationSpec = infiniteRepeatable(
                    animation = tween(300 + (index * 80), easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "bar"
            )
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(height.dp)
                    .background(
                        brush = Brush.verticalGradient(listOf(OrchidPrimary, OrchidSecondary)),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun HeroGlassCard(name: String, onAuraClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(InkDeep, HeroIndigoEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(x = 160.dp, y = (-40).dp)
                .background(OrchidPrimary.copy(alpha = 0.25f), CircleShape)
                .blur(45.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Στην αρχική οθόνη isSpeaking = false
            Box(modifier = Modifier.clickable { onAuraClick() }) {
                NeuralPrismAura(isSpeaking = false, onClick = onAuraClick)
            }
            Spacer(Modifier.width(24.dp))
            Column {
                Text("Welcome back,", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(0.6f))
                Text(name, style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black, color = Color.White))
            }
        }
    }
}

@Composable
fun ModeCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = "scale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .scale(scale)
            .padding(vertical = 4.dp)
            .shadow(
                elevation = if (isPressed) 6.dp else 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = OrchidPrimary.copy(alpha = 0.5f), //Έντονη ροζ σκιά
                ambientColor = OrchidPrimary
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            OrchidLight,
                            PureWhite
                        ),

                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(OrchidPrimary.copy(alpha = 0.4f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(SoftPinkGlow.copy(alpha = 0.6f), Color.Transparent),
                        center = Offset(0f, size.height),
                        radius = size.width * 0.7f
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Icon Container
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(OrchidPrimary.copy(alpha = 0.15f))
                        .border(1.dp, OrchidPrimary.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = OrchidPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.width(18.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = InkDeep,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = InkDeep.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = OrchidPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun ExtremeForumButtonUnified(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.94f else 1f, label = "scale")

    val infiniteTransition = rememberInfiniteTransition(label = "neon")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(if (isPressed) 1200 else 4000, easing = LinearEasing)), label = "rot"
    )

    Box(
        modifier = Modifier.fillMaxWidth().height(72.dp).scale(scale)
            .shadow(if (isPressed) 10.dp else 25.dp, RoundedCornerShape(28.dp), spotColor = OrchidPrimary)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(colors = listOf(InkDeep, HeroIndigoEnd)))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Canvas(modifier = Modifier.fillMaxSize().rotate(rotation)) {
            drawRoundRect(
                brush = Brush.sweepGradient(0.0f to OrchidPrimary, 0.5f to Color.Transparent, 1.0f to OrchidPrimary, center = center),
                style = Stroke(width = 3.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
            )
        }
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(Icons.Default.Diversity3, null, tint = OrchidPrimary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text("ACCESS COMMUNITY", fontWeight = FontWeight.Black, letterSpacing = 2.sp, color = Color.White)
        }
    }
}

@Composable
fun AnimatedShimmerTitle(text: String) {
    val shimmerColors = listOf(InkBlack, InkBlack, OrchidPrimary, InkBlack, InkBlack)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)), label = "s"
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
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Reverse), label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))), size.height * 0.1f),
                radius = 1100f
            )
        )
    }
}

@Composable
fun NeuralPrismAura(isSpeaking: Boolean = false, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_final")

    // 1. ΣΥΝΕΧΗΣ ΠΕΡΙΣΤΡΟΦΗ (Spinning)
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)), label = "outer"
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)), label = "inner"
    )

    // 2. RADAR EFFECT (The "Click Me" hint)
    val radarScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "radar_scale"
    )
    val radarAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "radar_alpha"
    )

    // 3. PULSE (Breathing)
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.15f else 1.08f,
        animationSpec = infiniteRepeatable(tween(1000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(90.dp)
            .scale(pulse)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 55.dp, color = OrchidPrimary),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // --- LAYER 1: THE RADAR ---
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (!isSpeaking) {
                drawCircle(
                    color = OrchidPrimary.copy(alpha = radarAlpha),
                    radius = (size.width / 2) * radarScale,
                    style = Stroke(width = 2f)
                )
            }
        }

        // --- LAYER 2: THE ORIGINAL PRISM SHAPE ---
        Box(
            modifier = Modifier
                .size(85.dp)
                .rotate(outerRotation)
                .drawBehind {
                    drawCircle(
                        brush = Brush.sweepGradient(listOf(OrchidPrimary, Color.Transparent, OrchidPrimary)),
                        style = Stroke(width = 5f, cap = StrokeCap.Round)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(listOf(OrchidPrimary.copy(0.15f), Color.Transparent)),
                        radius = size.width / 1.3f
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // --- LAYER 3: THE ORIGINAL LAYERS ICON ---
            Icon(
                imageVector = Icons.Default.Layers,
                contentDescription = null,
                tint = OrchidPrimary,
                modifier = Modifier
                    .size(32.dp)
                    .rotate(innerRotation - outerRotation) // Αντίθετη περιστροφή για το εφέ σου
            )
        }
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    profileBitmap: Bitmap?,
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
                            Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.baseline_face_24)
                                .error(R.drawable.baseline_face_24)
                                .circleCrop()
                                .into(this)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            profileBitmap != null -> {
                Image(
                    bitmap = profileBitmap.asImageBitmap(),
                    contentDescription = null,
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
fun ProfileMenuDropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAccountClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAccountClickAction: () -> Unit = onAccountClick
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.width(220.dp).background(PureWhite).border(1.dp, SoftPinkGlow, RoundedCornerShape(16.dp))
    ) {
        DropdownMenuItem(text = { Text("My Account") }, leadingIcon = { Icon(Icons.Outlined.AccountCircle, null, tint = OrchidPrimary) }, onClick = onAccountClickAction)
        DropdownMenuItem(text = { Text("Messages") }, leadingIcon = { Icon(Icons.Outlined.ChatBubbleOutline, null, tint = OrchidPrimary) }, onClick = onChatClick)
        DropdownMenuItem(text = { Text("Conferences") }, leadingIcon = { Icon(Icons.Outlined.VideoCall, null, tint = OrchidPrimary) }, onClick = onConferenceClick)
        DropdownMenuItem(text = { Text("Notes") }, leadingIcon = { Icon(Icons.Outlined.Description, null, tint = OrchidPrimary) }, onClick = onNotesClick)
        Divider(modifier = Modifier.padding(vertical = 4.dp), color = SoftPinkGlow)
        DropdownMenuItem(text = { Text("Logout", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold) }, leadingIcon = { Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444)) }, onClick = onLogoutClick)
    }
}