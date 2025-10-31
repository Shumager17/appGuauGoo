package com.example.appguaugo.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.ui.theme.GuauBlueText
import com.example.appguaugo.ui.theme.GuauYellow
import com.example.appguaugo.ui.theme.GuauYellowDark
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, String, String) -> Unit,
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasenha by remember { mutableStateOf("") }
    var confirmarContrasenha by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var prefijo by remember { mutableStateOf("+51") }

    // Estados de error
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasenha by remember { mutableStateOf<String?>(null) }
    var errorConfirmacion by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }

    val prefijos = listOf("+51 🇵🇪", "+52 🇲🇽", "+54 🇦🇷", "+56 🇨🇱", "+57 🇨🇴", "+58 🇻🇪", "+591 🇧🇴", "+593 🇪🇨", "+595 🇵🇾", "+598 🇺🇾")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuauYellow)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 🟢 SCRUM-11: Eliminar espacios en campos
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it.trimStart() },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-5: Validar correo válido
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it.trim()
                val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                errorCorreo = if (correo.isNotEmpty() && !emailRegex.matches(correo)) {
                    "Correo inválido"
                } else null
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorCorreo != null
        )
        errorCorreo?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-6: Validar contraseña fuerte
        var isPasswordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = contrasenha,
            onValueChange = {
                contrasenha = it.trim()
                val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,24}\$")
                errorContrasenha = if (contrasenha.isNotEmpty() && !passwordRegex.matches(contrasenha)) {
                    "Debe tener mayúscula, minúscula, número y caracter especial (6-24)"
                } else null
            },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isPasswordVisible) "Ocultar" else "Mostrar"
                TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) { Text(icon) }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorContrasenha != null
        )
        errorContrasenha?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-7: Validar contraseñas iguales
        OutlinedTextField(
            value = confirmarContrasenha,
            onValueChange = {
                confirmarContrasenha = it.trim()
                errorConfirmacion = if (confirmarContrasenha.isNotEmpty() && confirmarContrasenha != contrasenha) {
                    "Las contraseñas no coinciden"
                } else null
            },
            label = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorConfirmacion != null
        )
        errorConfirmacion?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-8: Validar edad mayor de 18
        OutlinedTextField(
            value = edad,
            onValueChange = {
                edad = it.filter { c -> c.isDigit() } // solo números
                errorEdad = if (edad.isNotEmpty() && edad.toIntOrNull() != null && edad.toInt() < 18) {
                    "Debes ser mayor de 18 años"
                } else null
            },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorEdad != null
        )
        errorEdad?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-10: Prefijo telefónico latinoamérica
        Box {
            OutlinedTextField(
                value = prefijo,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
                label = { Text("Prefijo") }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                prefijos.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p) },
                        onClick = {
                            prefijo = p.split(" ")[0]
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 🟢 SCRUM-9: Teléfono solo números
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it.filter { c -> c.isDigit() }.trim()
                errorTelefono = if (telefono.length < 6) "Teléfono demasiado corto" else null
            },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorTelefono != null
        )
        errorTelefono?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (errorCorreo == null && errorContrasenha == null && errorConfirmacion == null &&
                    errorEdad == null && errorTelefono == null &&
                    nombre.isNotBlank() && correo.isNotBlank() && contrasenha.isNotBlank()
                ) {
                    onRegisterClick(nombre, correo, contrasenha, edad, prefijo, telefono)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GuauYellowDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("REGISTRARME", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = { /* Navegar a login */ }) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = GuauBlueText)
        }
    }
}
