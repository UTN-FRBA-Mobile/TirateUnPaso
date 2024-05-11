package com.example.tirateunpaso.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tirateunpaso.R
import com.example.tirateunpaso.ui.routes

@Composable
fun LoginScreen(navController: NavController? = null){

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.loginimage),
            contentDescription = "Login image",
            modifier = Modifier.size(300.dp))
        Text(text = "Estás a sólo un paso",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = {
            email = it
        }, label = {
            Text(text = "Correo electrónico")
        })

        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = {
            Text(text = "Contraseña")
        }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController?.navigate(routes.home)
        }){
            Text(text = "Iniciar sesión")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Olvidé mi contraseña",
            modifier = Modifier.clickable {},
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No tengo una cuenta",
            modifier = Modifier.clickable {
                navController?.navigate(routes.signup)
            },
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun DefaultLoginPreview() {
    LoginScreen()
}