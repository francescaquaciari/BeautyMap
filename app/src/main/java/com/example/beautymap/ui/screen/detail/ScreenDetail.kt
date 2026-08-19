package com.example.beautymap.ui.screen.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.theme.Pink40
import com.example.beautymap.ui.theme.Purple40
import com.example.beautymap.ui.theme.PurpleGrey40
import androidx.core.net.toUri

@Composable
fun ScreenDetail(
    user: User
) {
    val context = LocalContext.current
    val initialLetter = user.name.trim().take(1).uppercase().ifEmpty { "B" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- HERO CARD PRINCIPALE ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circolare con iniziale e gradiente
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                brush = Brush.linearGradient(colors = listOf(Purple40, Pink40)),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initialLetter,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C)
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Tag "Aperto" / "Estetica"
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF3E5F5)
                        ) {
                            Text(
                                text = "Estetica",
                                color = Purple40,
                                style = typography.labelSmall,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // DA RIVEDERE Pulsanti Azione Rapida (Chiama, Mappa, Sito)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (user.phone.isNotBlank()) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, "tel:${user.phone}".toUri())
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chiama", fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = {
                            val gmmIntentUri =
                                "geo:${user.lat},${user.lng}?q=${user.lat},${user.lng}(${user.name})".toUri()
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            context.startActivity(mapIntent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E5F5), contentColor = Purple40),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mappa", fontSize = 12.sp)
                    }

                    if (user.website.isNotBlank()) {
                        Button(
                            onClick = {
                                val url = if (!user.website.startsWith("http")) "https://${user.website}" else user.website
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E5F5), contentColor = Purple40),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sito Web", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        // --- CARD 1: POSIZIONE ---
        DetailSectionCard(title = "Posizione") {
            DetailRow(
                icon = Icons.Default.LocationOn,
                label = "Città",
                value = user.city,
                iconColor = Pink40
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            DetailRow(
                icon = Icons.Default.Home,
                label = "Indirizzo",
                value = user.address,
                iconColor = Purple40
            )
        }

        // --- CARD 2: CONTATTI DIRETTI ---
        val hasPhone = user.phone.isNotBlank()
        val hasEmail = user.email.isNotBlank() && user.email != "Email non disponibile"

        if (hasPhone || hasEmail) {
            DetailSectionCard(title = "Contatti Diretti") {
                if (hasPhone) {
                    DetailRow(
                        icon = Icons.Default.Phone,
                        label = "Telefono",
                        value = user.phone,
                        iconColor = Purple40,
                        isClickable = true,
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, "tel:${user.phone}".toUri())
                            context.startActivity(intent)
                        }
                    )
                }

                if (hasPhone && hasEmail) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                }

                if (hasEmail) {
                    DetailRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user.email,
                        iconColor = Pink40,
                        isClickable = true,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, "mailto:${user.email}".toUri())
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        // --- CARD 3: SITO WEB E SOCIAL ---
        val hasWebsite = user.website.isNotBlank()
        val hasFacebook = user.facebook.isNotBlank()
        val hasInstagram = user.instagram.isNotBlank()

        if (hasWebsite || hasFacebook || hasInstagram) {
            DetailSectionCard(title = "Sito Web e Social") {
                var needsDivider = false

                if (hasWebsite) {
                    DetailRow(
                        icon = Icons.Default.Info,
                        label = "Sito Web",
                        value = user.website,
                        iconColor = Purple40,
                        isClickable = true,
                        onClick = {
                            val url = if (!user.website.startsWith("http")) "https://${user.website}" else user.website
                            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                        }
                    )
                    needsDivider = true
                }

                if (hasFacebook) {
                    if (needsDivider) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                    }
                    DetailRow(
                        icon = Icons.Default.Share,
                        label = "Facebook",
                        value = user.facebook,
                        iconColor = Pink40,
                        isClickable = true,
                        onClick = {
                            val url = if (!user.facebook.startsWith("http")) "https://${user.facebook}" else user.facebook
                            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                        }
                    )
                    needsDivider = true
                }

                if (hasInstagram) {
                    if (needsDivider) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                    }
                    DetailRow(
                        icon = Icons.Default.Share,
                        label = "Instagram",
                        value = user.instagram,
                        iconColor = Purple40,
                        isClickable = true,
                        onClick = {
                            val url = if (!user.instagram.startsWith("http")) "https://${user.instagram}" else user.instagram
                            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                        }
                    )
                }
            }
        }

        // --- CARD 4: INFORMAZIONI PROFILO ---
        if (user.username.isNotBlank()) {
            DetailSectionCard(title = "Informazioni Profilo") {
                DetailRow(
                    icon = Icons.Default.Person,
                    label = "Username",
                    value = user.username,
                    iconColor = Purple40
                )
            }
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Purple40,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color = Purple40,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isClickable) Modifier.clickable { onClick() } else Modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = typography.labelMedium,
                color = PurpleGrey40
            )
            Text(
                text = value,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isClickable) Purple40 else Color(0xFF2C2C2C)
            )
        }
    }
}