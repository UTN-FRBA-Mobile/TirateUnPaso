package com.example.tirateunpaso.ui.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.tirateunpaso.ui.values
import com.example.tirateunpaso.ui.values.defaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpClick:() -> Unit,
    onLoginClick:() -> Unit
){

    val (username,setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (email,setEmail) = rememberSaveable {
        mutableStateOf("")
    }
    val (password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (secondPassword,setSecondPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (age,setAge) = rememberSaveable {
        mutableStateOf("")
    }
    val (height,setHeight) = rememberSaveable {
        mutableStateOf("")
    }

    val sexList = listOf("Selecciona tu sexo","Masculino", "Femenino", "Otro")
    var sex by rememberSaveable {
        mutableStateOf(sexList[0])
    }
    var isExpanded by remember{
        mutableStateOf(false)
    }

    val fieldsCompleted =
        username.isNotEmpty() &&
        email.isNotEmpty() &&
        password.isNotEmpty() &&
        secondPassword.isNotEmpty() &&
        age.isNotEmpty() &&
        height.isNotEmpty() &&
        sex != sexList[0]

    var matchingPasswords by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.signupimage),
            contentDescription = "Login image",
            modifier = Modifier.size(80.dp))

        HeaderText(
            text = "Da tu primer paso",
            modifier = Modifier.padding(vertical = defaultPadding)
        )
        AnimatedVisibility(visible = !matchingPasswords) {
            Text(
                text = "Las contraseñas ingresadas no coinciden",
                color = MaterialTheme.colorScheme.error
            )
        }

        LoginTextField(
            value = username,
            onValueChange = setUsername,
            labelText = "Nombre de usuario",
            leadingIcon = Icons.Default.AccountCircle,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = email,
            onValueChange = setEmail,
            labelText = "Correo electrónico",
            leadingIcon = Icons.Default.Email,
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
        LoginTextField(
            value = secondPassword,
            onValueChange = setSecondPassword,
            labelText = "Repetí tu contraseña",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        LoginTextField(
            value = age,
            onValueChange = setAge,
            labelText = "Edad",
            leadingIcon = Icons.Default.DateRange,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = height,
            onValueChange = setHeight,
            labelText = "Altura",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {isExpanded = !isExpanded}
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = sex,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(30),
                trailingIcon = {ExposedDropdownMenuDefaults.
                    TrailingIcon(expanded = isExpanded)}
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                modifier = Modifier.fillMaxWidth(),
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

        Button(
            onClick = {
                matchingPasswords = password == secondPassword
                if(matchingPasswords){
                    onSignUpClick()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = fieldsCompleted
        ){
            Text(
                text = "Registrarte",
                fontSize = values.fontsize
            )
        }
    }

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(
                align =
                Alignment.BottomCenter
            )
    ){
        Text(
            text = "¿Ya tenés una cuenta?",
            fontSize = values.fontsize
        )
        TextButton(
            onClick = onLoginClick
        ) {
            Text(
                text = "Inicia sesión",
                fontSize = values.fontsize
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultSignUpPreview() {
    SignUpScreen({},{})
}