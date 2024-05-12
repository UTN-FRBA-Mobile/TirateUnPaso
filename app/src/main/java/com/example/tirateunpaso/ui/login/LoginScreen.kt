package com.example.tirateunpaso.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tirateunpaso.R
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.components.LoginTextField
import com.example.tirateunpaso.ui.routes

@Composable
fun LoginScreen(navController: NavController? = null){

    val defaultPadding = 16.dp
    val defaultSpacing = 16.dp
    val fontsize = 16.sp

    val (username,setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (checked,setChecked) = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.loginimage),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp))
        HeaderText(
            text = "Estás a sólo un paso",
            modifier = Modifier.padding(vertical = defaultPadding)
        )
        LoginTextField(
            value = username,
            onValueChange = setUsername,
            labelText = "Nombre de usuario",
            leadingIcon = Icons.Default.AccountCircle,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = "Contraseña",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(defaultSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(checked = checked, onCheckedChange = setChecked)
                Text(
                    text = "Recordarme",
                    fontSize = fontsize
                )
            }
            Row {
                TextButton(onClick = {  }) {
                    Text(
                        text = "Olvidé mi contraseña",
                        fontSize = fontsize
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(defaultSpacing))
        Button(
            onClick = {
                navController?.navigate(routes.home)
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Iniciar sesión",
                fontSize = fontsize)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "¿No tenés una cuenta?",
                fontSize = fontsize
            )
            TextButton(onClick = {
                navController?.navigate(routes.signup)
            }) {
                Text(
                    text = "Registrate",
                    fontSize = fontsize
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultLoginPreview() {
    LoginScreen()
}