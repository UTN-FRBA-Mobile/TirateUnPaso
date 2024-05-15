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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.tirateunpaso.R
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.components.LoginTextField
import com.example.tirateunpaso.ui.values.defaultPadding
import com.example.tirateunpaso.ui.values.defaultSpacing
import com.example.tirateunpaso.ui.values.fontsize

@Composable
fun LoginScreen(onLoginClick:() -> Unit, onSignUpClick:() -> Unit){

    val (username,setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (checked,setChecked) = rememberSaveable {
        mutableStateOf(false)
    }
    val fieldsCompleted =
        username.isNotEmpty() &&
        password.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = fieldsCompleted
        ){
            Text(
                text = "Iniciar sesión",
                fontSize = fontsize)
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        ){
            Text(
                text = "¿No tenés una cuenta?",
                fontSize = fontsize
            )
            TextButton(
                onClick = onSignUpClick
            ) {
                Text(
                    text = "Registrate",
                    fontSize = fontsize
                )
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultLoginPreview() {
    LoginScreen({},{})
}