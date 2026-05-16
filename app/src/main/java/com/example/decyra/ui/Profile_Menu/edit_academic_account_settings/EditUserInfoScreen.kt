package com.example.decyra.ui.Profile_Menu.edit_academic_account_settings

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserInfoScreen(
    universities: List<String>,
    selectedUniversity: String,
    selectedYear: String,
    academicLevel: String,
    languages: String,
    gpa: String,
    field: String,
    budget: String,
    selectedAdvisorType: String?,
    isSaving: Boolean,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onUniversitySelected: (String) -> Unit,
    onYearSelected: (String) -> Unit,
    onAcademicLevelChange: (String) -> Unit,
    onLanguagesChange: (String) -> Unit,
    onGpaChange: (String) -> Unit,
    onFieldChange: (String) -> Unit,
    onBudgetChange: (String) -> Unit,
    onAdvisorSelected: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val years = listOf("1", "2", "3", "4")

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
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            EditUserInfoTopBar(onBackClick)
            Spacer(Modifier.height(24.dp))
            EditUserInfoHero()
            Spacer(Modifier.height(24.dp))

            AcademicFormCard(
                universities = universities,
                selectedUniversity = selectedUniversity,
                selectedYear = selectedYear,
                academicLevel = academicLevel,
                languages = languages,
                gpa = gpa,
                field = field,
                budget = budget,
                onUniversitySelected = onUniversitySelected,
                onYearSelected = onYearSelected,
                onAcademicLevelChange = onAcademicLevelChange,
                onLanguagesChange = onLanguagesChange,
                onGpaChange = onGpaChange,
                onFieldChange = onFieldChange,
                onBudgetChange = onBudgetChange,
                years = years
            )

            Spacer(Modifier.height(18.dp))

            AdvisorSelectionCard(
                selectedAdvisorType = selectedAdvisorType,
                onAdvisorSelected = onAdvisorSelected
            )

            Spacer(Modifier.height(20.dp))

            PremiumPrimaryButton(
                text = if (isSaving) "SAVING..." else "SAVE ACADEMIC INFO",
                onClick = onSaveClick,
                enabled = !isSaving && !isLoading
            )
        }
    }
}

@Composable
fun EditUserInfoTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SoftPinkGlow)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = InkDeep)
        }

        Text(
            text = "DECYRA",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                letterSpacing = 8.sp,
                brush = Brush.linearGradient(colors = listOf(InkDeep, OrchidPrimary))
            )
        )

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun EditUserInfoHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
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
                .size(130.dp)
                .offset(x = 180.dp, y = (-40).dp)
                .background(OrchidPrimary.copy(alpha = 0.23f), CircleShape)
                .blur(42.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(22.dp))
            Column {
                Text(
                    "Refine your",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Academic Path",
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicFormCard(
    universities: List<String>,
    selectedUniversity: String,
    selectedYear: String,
    academicLevel: String,
    languages: String,
    gpa: String,
    field: String,
    budget: String,
    years: List<String>,
    onUniversitySelected: (String) -> Unit,
    onYearSelected: (String) -> Unit,
    onAcademicLevelChange: (String) -> Unit,
    onLanguagesChange: (String) -> Unit,
    onGpaChange: (String) -> Unit,
    onFieldChange: (String) -> Unit,
    onBudgetChange: (String) -> Unit
) {
    var uniExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFF1E8F7), RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        AnimatedShimmerTitle("ACADEMIC DETAILS")
        Spacer(Modifier.height(18.dp))

        ExposedDropdownMenuBox(
            expanded = uniExpanded,
            onExpandedChange = { uniExpanded = !uniExpanded }
        ) {
            OutlinedTextField(
                value = selectedUniversity,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                label = { Text("University") },
                leadingIcon = {
                    Icon(Icons.Outlined.AccountBalance, contentDescription = null, tint = OrchidPrimary)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = uniExpanded)
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrchidPrimary,
                    unfocusedBorderColor = Color(0xFFE9D5F5),
                    focusedLabelColor = OrchidPrimary,
                    cursorColor = OrchidPrimary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = uniExpanded,
                onDismissRequest = { uniExpanded = false }
            ) {
                universities.forEach { university ->
                    DropdownMenuItem(
                        text = { Text(university) },
                        onClick = {
                            onUniversitySelected(university)
                            uniExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = academicLevel,
            onValueChange = onAcademicLevelChange,
            label = "Academic level",
            leadingIcon = Icons.Outlined.School,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = languages,
            onValueChange = onLanguagesChange,
            label = "Languages",
            leadingIcon = Icons.Outlined.Language,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = gpa,
            onValueChange = onGpaChange,
            label = "GPA",
            leadingIcon = Icons.Outlined.AutoAwesome,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = field,
            onValueChange = onFieldChange,
            label = "Field",
            leadingIcon = Icons.Outlined.Workspaces,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        PremiumTextField(
            value = budget,
            onValueChange = onBudgetChange,
            label = "Budget per year",
            leadingIcon = Icons.Outlined.Payments,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        ExposedDropdownMenuBox(
            expanded = yearExpanded,
            onExpandedChange = { yearExpanded = !yearExpanded }
        ) {
            OutlinedTextField(
                value = selectedYear,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                label = { Text("Year of studies") },
                leadingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = OrchidPrimary)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded)
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrchidPrimary,
                    unfocusedBorderColor = Color(0xFFE9D5F5),
                    focusedLabelColor = OrchidPrimary,
                    cursorColor = OrchidPrimary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = yearExpanded,
                onDismissRequest = { yearExpanded = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year) },
                        onClick = {
                            onYearSelected(year)
                            yearExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdvisorSelectionCard(
    selectedAdvisorType: String?,
    onAdvisorSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFF1E8F7), RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        AnimatedShimmerTitle("CHOOSE ADVISOR")
        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdvisorOption(
                title = "Male",
                emoji = "👨",
                selected = selectedAdvisorType == "male",
                modifier = Modifier.weight(1f),
                onClick = { onAdvisorSelected("male") }
            )
            AdvisorOption(
                title = "Female",
                emoji = "👩",
                selected = selectedAdvisorType == "female",
                modifier = Modifier.weight(1f),
                onClick = { onAdvisorSelected("female") }
            )
            AdvisorOption(
                title = "Robot",
                emoji = "🤖",
                selected = selectedAdvisorType == "robot",
                modifier = Modifier.weight(1f),
                onClick = { onAdvisorSelected("robot") }
            )
        }
    }
}

@Composable
fun AdvisorOption(
    title: String,
    emoji: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (selected) OrchidPrimary else Color(0xFFE8E8F0)
    val bgColor = if (selected) OrchidLight else Color.White

    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 30.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                color = InkDeep,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        label = { Text(label) },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = null, tint = OrchidPrimary)
        },
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrchidPrimary,
            unfocusedBorderColor = Color(0xFFE9D5F5),
            focusedLabelColor = OrchidPrimary,
            cursorColor = OrchidPrimary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun PremiumPrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "saveScale"
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
        Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.4.sp
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
        label = "s"
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
                    size.height * 0.1f
                ),
                radius = 1100f
            )
        )
    }
}