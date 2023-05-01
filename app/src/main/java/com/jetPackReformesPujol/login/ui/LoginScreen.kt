package com.jetPackReformesPujol.login.ui


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.login.data.UserPreferences
import com.jetPackReformesPujol.login.domain.model.Response
import com.jetPackReformesPujol.model.Routes
import timber.log.Timber

@Composable
fun MainContentLogin(navigationController: NavHostController, loginViewModel: LoginViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFDFDFDF))
            .padding(8.dp)
    ) {
        Header(Modifier.align(Alignment.TopEnd))
        Body(Modifier.align(Alignment.Center), loginViewModel)
        AuthState(loginViewModel, navigationController)
    }
}

@Composable
fun Header(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "close app",
        modifier = modifier
            .clickable { activity.finish() }
            .padding(8.dp)
    )
}

@Composable
fun Body(modifier: Modifier, loginViewModel: LoginViewModel) {

    val email: String by loginViewModel.email.observeAsState("")
    val password: String by loginViewModel.password.observeAsState("")
    val isLoginEnable: Boolean by loginViewModel.isLoginEnable.observeAsState(false)

    Column(modifier = modifier) {
        ImageLogo(Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.size(10.dp))
        Email(email) {
            loginViewModel.onLoginChanged(email = it, password = password)
        }
        Spacer(modifier = Modifier.size(10.dp))
        Password(password) {
            loginViewModel.onLoginChanged(email = email, password = it)
        }
        Spacer(modifier = Modifier.size(10.dp))
        LoginButton(isLoginEnable, email, password, loginViewModel)
        Spacer(modifier = Modifier.size(10.dp))
        ForgotPassword(Modifier.align(Alignment.End))
    }
}

@Composable
fun LoginButton(
    loginEnable: Boolean,
    email: String,
    password: String,
    loginViewModel: LoginViewModel
) {
    Button(
        onClick = {
            loginViewModel.signInWithEmailAndPassword(email, password)
            loginViewModel.saveUserCredentials(email, password)
        },
        enabled = loginViewModel.onLoginConfirm(email, password),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF4EA8E9),
            disabledBackgroundColor = Color(0xFF78C8F9),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Log In")
    }
}

@Composable
fun ImageLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.reformespujol),
        contentDescription = null, modifier.size(250.dp)
    )
}

@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Correo electrónico") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFFB2B2B2),
            backgroundColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Password") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFFB2B2B2),
            backgroundColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val imagen = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = imagen, contentDescription = "show password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Olvidaste la contraseña?",
        modifier
            .clickable {}, color = Color.Blue
    )
}

@Composable
fun AuthState(loginViewModel: LoginViewModel, navController: NavHostController) {

    val signInResponse by loginViewModel.signInResponse.observeAsState(false)
    val isAuthenticated by loginViewModel.isAuthenticated.observeAsState(false)

    when (signInResponse) {
        is Response.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Error -> {
            // Handle error
        }
        is Response.Success<*> -> {
            if (isAuthenticated) {
                loginViewModel.checkAuthState()
                NavigateToHomeScreen(navController)
            }
        }
    }
}

@Composable
private fun NavigateToSignInScreen(navController: NavHostController) =
    navController.navigate(Routes.LoginScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }

@Composable
private fun NavigateToHomeScreen(navController: NavHostController) =
    navController.navigate(Routes.HomeScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }



