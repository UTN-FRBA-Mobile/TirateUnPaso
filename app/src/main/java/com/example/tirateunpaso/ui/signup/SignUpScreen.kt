package com.example.tirateunpaso.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController? = null){

    var username by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var secondPassword by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf("")
    }
    var height by remember {
        mutableStateOf("")
    }
    val sexList = listOf("Selecciona tu sexo","Masculino", "Femenino", "Otro")
    var sex by remember {
        mutableStateOf(sexList[0])
    }
    var isExpanded by remember{
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.signupimage),
            contentDescription = "Login image",
            modifier = Modifier.size(120.dp))

        Text(text = "Da tus primeros pasos",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = username, onValueChange = {
            username = it
        }, label = {
            Text(text = "Nombre de usuario")
        })

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

        OutlinedTextField(value = secondPassword, onValueChange = {
            secondPassword = it
        }, label = {
            Text(text = "Repetí tu contraseña")
        }, visualTransformation = PasswordVisualTransformation())

        OutlinedTextField(value = age, onValueChange = {
            age = it
        }, label = {
            Text(text = "Edad")
        })

        OutlinedTextField(value = height, onValueChange = {
            height = it
        }, label = {
            Text(text = "Altura")
        })

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {isExpanded = !isExpanded}
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = sex,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {ExposedDropdownMenuDefaults.
                    TrailingIcon(expanded = isExpanded)}
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }) {
                sexList.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            sex = if (index == 0){
                                sexList[3]
                            }else {
                                sexList[index]
                            }
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(onClick = {
            navController?.navigate(routes.home)
        }){
            Text(text = "Registrarme")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ya tengo una cuenta",
            modifier = Modifier.clickable {
                navController?.navigate(routes.login)
            },
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun DefaultSignUpPreview() {
    SignUpScreen()
}