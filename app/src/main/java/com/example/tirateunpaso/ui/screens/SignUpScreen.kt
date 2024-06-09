package com.example.tirateunpaso.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tirateunpaso.R
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.components.LoginFooter
import com.example.tirateunpaso.ui.components.LoginTextField
import com.example.tirateunpaso.ui.values
import com.example.tirateunpaso.ui.values.defaultPadding
import com.example.tirateunpaso.ui.viewmodels.AchievementViewModel
import com.example.tirateunpaso.ui.viewmodels.AppViewModelProvider
import com.example.tirateunpaso.ui.viewmodels.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpClick:() -> Unit,
    onLoginClick:() -> Unit,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    /*
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
*/
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
        AnimatedVisibility(visible = !viewModel.signUpUiState.matchingPasswords) {
            Text(
                text = "Las contraseñas ingresadas no coinciden",
                color = MaterialTheme.colorScheme.error
            )
        }

        LoginTextField(
            value = viewModel.signUpUiState.username,
            onValueChange = viewModel::setUsername,
            labelText = "Nombre de usuario",
            leadingIcon = Icons.Default.AccountCircle,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = viewModel.signUpUiState.email,
            onValueChange = viewModel::setEmail,
            labelText = "Correo electrónico",
            leadingIcon = Icons.Default.Email,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = viewModel.signUpUiState.password,
            onValueChange = viewModel::setPassword,
            labelText = "Contraseña",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        LoginTextField(
            value = viewModel.signUpUiState.secondPassword,
            onValueChange = viewModel::setSecondPassword,
            labelText = "Repetí tu contraseña",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        LoginTextField(
            value = viewModel.signUpUiState.age,
            onValueChange = viewModel::setAge,
            labelText = "Edad",
            leadingIcon = Icons.Default.DateRange,
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = viewModel.signUpUiState.height,
            onValueChange = viewModel::setHeight,
            labelText = "Altura",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = viewModel.signUpUiState.isExpanded,
            onExpandedChange = viewModel::setIsExpanded
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = viewModel.signUpUiState.sex,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(30),
                trailingIcon = {ExposedDropdownMenuDefaults.
                    TrailingIcon(expanded = viewModel.signUpUiState.isExpanded)}
            )

            ExposedDropdownMenu(
                expanded = viewModel.signUpUiState.isExpanded,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = { viewModel.setIsExpanded(false) }) {
                viewModel.signUpUiState.sexList.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            viewModel.setSex(index)
                            viewModel.setIsExpanded(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.verifyMatchingPasswords(
                    onSignUpClick
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.fieldsCompleted()
        ){
            Text(
                text = "Registrarte",
                fontSize = values.fontsize
            )
        }
    }

    LoginFooter(
        onClick = onLoginClick,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomCenter),
        text = "¿Ya tenés una cuenta?",
        buttonText = "Inicia sesión"
    )
}

@Preview(showSystemUi = true)
@Composable
fun DefaultSignUpPreview() {
    SignUpScreen({},{})
}