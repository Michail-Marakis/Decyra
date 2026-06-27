package com.example.decyra.frontend.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun LoginScreen(
    email: String,
    password: String,
    infoMessage: String?,
    faceLoginEnabled: Boolean,
    isLoading: Boolean,
    showCameraPreview: Boolean,
    isFaceCentered: Boolean = false,
    guidanceMessage: String = "Align your face inside the frame",

    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onFaceLoginClick: () -> Unit,
    cameraPreview: @Composable () -> Unit,
    onCaptureClick: () -> Unit,
    onBackFromCamera: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        AnimatedMeshBackground()

        if (showCameraPreview) {
            CameraCaptureOverlay(
                cameraPreview = cameraPreview,
                onCaptureClick = onCaptureClick,
                onBackClick = onBackFromCamera,
                isFaceCentered = isFaceCentered,
                guidanceMessage = guidanceMessage
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBrand()
                Spacer(Modifier.height(28.dp))
                Spacer(Modifier.height(28.dp))
                LoginFormCard(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    infoMessage = infoMessage,
                    isLoading = isLoading,
                    faceLoginEnabled = faceLoginEnabled,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    onLoginClick = onLoginClick,
                    onForgotPasswordClick = onForgotPasswordClick,
                    onFaceLoginClick = onFaceLoginClick
                )
                Spacer(Modifier.height(18.dp))
                SecondaryRegisterRow(onRegisterClick = onRegisterClick)
            }
        }
    }
}

@Composable
fun TopBrand() {
    Text(
        text = "DECYRA",
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Black,
            fontSize = 28.sp,
            letterSpacing = 8.sp,
            brush = Brush.linearGradient(
                colors = listOf(InkDeep, OrchidPrimary)
            )
        )
    )
}

@Composable
fun LoginFormCard(
    email: String,
    password: String,
    passwordVisible: Boolean,
    infoMessage: String?,
    isLoading: Boolean,
    faceLoginEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onFaceLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(1.dp, Color(0xFFF1E8F7), RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        AnimatedShimmerTitle("SECURE LOGIN")
        Spacer(Modifier.height(18.dp))

        PremiumTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email address",
            leadingIcon = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            leadingIcon = Icons.Outlined.Lock,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailing = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = OrchidPrimary
                    )
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(
                    "Forgot password?",
                    color = InkDeep,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        AnimatedVisibility(visible = !infoMessage.isNullOrBlank()) {
            Text(
                text = infoMessage.orEmpty(),
                color = if (infoMessage?.contains("Incorrect", true) == true ||
                    infoMessage?.contains("error", true) == true) Color(0xFFDC2626) else OrchidPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        PremiumPrimaryButton(
            text = if (isLoading) "SIGNING IN..." else "LOGIN",
            onClick = onLoginClick,
            enabled = !isLoading
        )

        Spacer(Modifier.height(12.dp))

        PremiumSecondaryButton(
            text = if (faceLoginEnabled) "FACE LOGIN" else "FACE LOGIN LOCKED",
            icon = Icons.Default.Face,
            enabled = faceLoginEnabled,
            onClick = onFaceLoginClick
        )
    }
}

@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        singleLine = true,
        label = { Text(label) },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = null, tint = OrchidPrimary)
        },
        trailingIcon = trailing,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrchidPrimary,
            unfocusedBorderColor = Color(0xFFE9D5F5),
            focusedLabelColor = OrchidPrimary,
            cursorColor = OrchidPrimary,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@Composable
fun PremiumPrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "primaryScale"
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
fun PremiumSecondaryButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "secondaryScale"
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.4.dp, OrchidPrimary.copy(alpha = 0.35f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = OrchidLight,
            contentColor = InkDeep,
            disabledContainerColor = OrchidLight.copy(alpha = 0.5f),
            disabledContentColor = Color.Gray
        )
    ) {
        Icon(icon, contentDescription = null, tint = OrchidPrimary)
        Spacer(Modifier.width(10.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SecondaryRegisterRow(onRegisterClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "New here?",
            color = Color.Gray
        )
        TextButton(onClick = onRegisterClick) {
            Text(
                "Create account",
                color = OrchidPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CameraCaptureOverlay(
    cameraPreview: @Composable () -> Unit,
    onCaptureClick: () -> Unit,
    onBackClick: () -> Unit,
    isFaceCentered: Boolean,
    guidanceMessage: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "modern_scan")


    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)), label = "rotation"
    )


    val pulseScale by animateFloatAsState(
        targetValue = if (isFaceCentered) 1.08f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        cameraPreview()

        //THE SCANNER RING
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
        ) {
            val centerX = size.width / 2
            val centerY = size.height * 0.42f
            val radius = size.width * 0.38f

            val accentColor = if (isFaceCentered) OrchidPrimary else Color.White.copy(alpha = 0.5f)
            val strokeWidth = if (isFaceCentered) 10f else 6f

            rotate(rotation, pivot = Offset(centerX, centerY)) {
                for (i in 0..3) {
                    drawArc(
                        color = accentColor,
                        startAngle = i * 90f + 20f,
                        sweepAngle = 50f,
                        useCenter = false,
                        topLeft = Offset(centerX - radius, centerY - radius),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            if (isFaceCentered) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(OrchidPrimary.copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(centerX, centerY),
                        radius = radius * 1.3f
                    )
                )
            }
        }

        //TOP CONTROLS
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(Icons.Default.Close, null, tint = Color.White)
        }

        //GUIDANCE & SHUTTER
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = guidanceMessage.uppercase(),
                color = if (isFaceCentered) OrchidPrimary else Color.White,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                )
            )

            Spacer(Modifier.height(40.dp))

            //Premium Shutter Button με εικονίδιο κάμερας
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(pulseScale) //Το κουμπί μεγαλώνει όταν κλειδώνει το πρόσωπο
                    .border(2.dp, Color.White, CircleShape)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(if (isFaceCentered) OrchidPrimary else Color.White.copy(alpha = 0.2f))
                    .clickable(enabled = isFaceCentered) { onCaptureClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    //Αντικατάσταση Fingerprint με PhotoCamera
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Capture",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
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