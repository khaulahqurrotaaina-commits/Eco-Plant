package com.khaulahqurrota.ecoplant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LoginForest = Color(0xFF174C38)
private val LoginMint = Color(0xFFE7F0E5)

@Composable
fun EcoPlantBackdrop(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF102F25), Color(0xFF286447), Color(0xFF4F8060)),
            ),
        ),
    )
}

@Composable
private fun LoginLeafBackdrop(modifier: Modifier = Modifier) {
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.ecoplant_leaf_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(Modifier.fillMaxSize().background(Color(0x980A281E)))
    }
}

@Composable
fun LoginScreen(onLogin: (AppRole) -> Unit, onBrowseAsBuyer: () -> Unit) {
    var selectedRole by remember { mutableStateOf(AppRole.Admin) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        LoginLeafBackdrop(Modifier.fillMaxSize())
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 25.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            EcoPlantBrand()
            Spacer(Modifier.height(26.dp))

            Surface(
                modifier = Modifier.border(1.dp, Color.White.copy(alpha = .42f), RoundedCornerShape(25.dp)),
                color = Color.White.copy(alpha = .14f),
                shape = RoundedCornerShape(25.dp),
                shadowElevation = 12.dp,
            ) {
                Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Masuk ke akun", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Pilih akses untuk melanjutkan", color = Color.White.copy(alpha = .78f), fontSize = 12.sp)
                    Spacer(Modifier.height(18.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        RoleChoice("Admin", selectedRole == AppRole.Admin, Modifier.weight(1f)) {
                            selectedRole = AppRole.Admin
                            error = ""
                        }
                        RoleChoice("Petugas", selectedRole == AppRole.Officer, Modifier.weight(1f)) {
                            selectedRole = AppRole.Officer
                            error = ""
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; error = "" },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Username") },
                        shape = RoundedCornerShape(14.dp),
                        colors = loginFieldColors(),
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; error = "" },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(14.dp),
                        colors = loginFieldColors(),
                    )
                    if (error.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(error, color = Color(0xFFB5483D), fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val expectedUsername = if (selectedRole == AppRole.Admin) BuildConfig.ADMIN_USERNAME else BuildConfig.OFFICER_USERNAME
                            val expectedPassword = if (selectedRole == AppRole.Admin) BuildConfig.ADMIN_PASSWORD else BuildConfig.OFFICER_PASSWORD
                            if (username.trim() == expectedUsername && password == expectedPassword) onLogin(selectedRole)
                            else error = "Username atau password belum sesuai. Periksa konfigurasi akun lokal."
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LoginForest),
                    ) {
                        Text("Masuk sebagai ${if (selectedRole == AppRole.Admin) "Admin" else "Petugas"}", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Akun admin dan petugas diatur pada local.properties", color = Color.White.copy(alpha = .72f), fontSize = 10.sp, textAlign = TextAlign.Center)
                    TextButton(onClick = onBrowseAsBuyer) {
                        Text("Jelajahi toko sebagai pembeli", color = LoginMint, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EcoPlantBrand() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(color = LoginMint, shape = CircleShape, modifier = Modifier.size(52.dp)) {
            androidx.compose.foundation.Canvas(Modifier.padding(12.dp).fillMaxSize()) {
                val w = size.width
                val h = size.height
                val stemColor = LoginForest
                drawLine(stemColor, Offset(w * .5f, h * .83f), Offset(w * .5f, h * .25f), strokeWidth = w * .07f)
                drawLine(stemColor, Offset(w * .5f, h * .56f), Offset(w * .23f, h * .34f), strokeWidth = w * .05f)
                drawLine(stemColor, Offset(w * .5f, h * .47f), Offset(w * .77f, h * .29f), strokeWidth = w * .05f)
                rotate(-35f, pivot = Offset(w * .28f, h * .34f)) {
                    drawOval(Color(0xFF6F9F5D), Offset(w * .10f, h * .23f), Size(w * .37f, h * .20f))
                }
                rotate(32f, pivot = Offset(w * .72f, h * .29f)) {
                    drawOval(Color(0xFF4E8A50), Offset(w * .54f, h * .18f), Size(w * .36f, h * .20f))
                }
                rotate(-8f, pivot = Offset(w * .5f, h * .18f)) {
                    drawOval(Color(0xFF8BB66B), Offset(w * .37f, h * .07f), Size(w * .27f, h * .19f))
                }
            }
        }
        Column(Modifier.padding(start = 12.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Eco", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Medium)
                Text("Plant", color = LoginMint, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text("TANAM BAHAGIA, HIDUP LEBIH HIJAU", color = Color.White.copy(alpha = .78f), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = .8.sp)
        }
    }
}

@Composable
private fun RoleChoice(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = if (selected) LoginForest.copy(alpha = .88f) else Color.White.copy(alpha = .14f),
        shape = RoundedCornerShape(13.dp),
    ) {
        Text(
            label,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun loginFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color.White.copy(alpha = .06f),
    unfocusedContainerColor = Color.White.copy(alpha = .04f),
    focusedBorderColor = Color.White.copy(alpha = .88f),
    unfocusedBorderColor = Color.White.copy(alpha = .52f),
    focusedLabelColor = LoginMint,
    unfocusedLabelColor = Color.White.copy(alpha = .76f),
    cursorColor = Color.White,
)
