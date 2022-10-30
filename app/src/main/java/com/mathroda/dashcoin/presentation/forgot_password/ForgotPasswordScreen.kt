package com.mathroda.dashcoin.presentation.forgot_password

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.presentation.coin_detail.components.BackStackButton
import com.mathroda.dashcoin.presentation.forgot_password.viewmodel.ResetPasswordViewModel
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomTextField
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = hiltViewModel()
    ) {
    Scaffold {

        var email by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }
        var isEnabled by remember { mutableStateOf(true) }
        var isLoading by remember { mutableStateOf(false) }
        val state = viewModel.resetPassword.collectAsState().value

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .requiredHeight(40.dp)
            ) {
                BackStackButton {
                    navController.popBackStack()
                }
            }

            CustomTextField(
                text = email,
                placeholder = "Email",
                onValueChange = { email = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid email address",
                isPasswordTextField = false,
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            CustomLoginButton(
                text = "Send Email",
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                isEnabled = false
                isError = email.isEmpty()
                viewModel.resetPasswordWithEmail(email)
                isLoading = !isLoading
            }
        }

        when {
            state.isLoading -> {
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            Modifier
                                .padding(top = 50.dp)
                        )
                    }
                }
            }

            state.Successful -> {

                Log.d("void", state.Successful.toString())
                SweetToastUtil.SweetSuccess(
                    message = "Email Sent Successfully",
                    duration = Toast.LENGTH_LONG,
                    padding = PaddingValues(bottom = 24.dp)
                )

                LaunchedEffect(Unit) {
                    navController.popBackStack()
                    navController.navigate(Screens.SignIn.route)
                }
            }

            state.error.isNotBlank() -> {
                isEnabled = true
                val errorMsg = state.error
                SweetToastUtil.SweetError(
                    message = errorMsg,
                    padding = PaddingValues(bottom = 24.dp)
                )
            }
        }
    }
}