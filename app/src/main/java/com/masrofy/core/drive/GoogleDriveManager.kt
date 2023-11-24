package com.masrofy.core.drive

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masrofy.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ActivityRetainedScoped
class GoogleDriveManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val scopes = listOf(Scope(DriveScopes.DRIVE_FILE))
    private val authorizeRequest = AuthorizationRequest.builder().setRequestedScopes(scopes).build()

    private val oneTap = Identity.getSignInClient(context)
    private val signInRequest =
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(
                    context.getString(
                        R.string.web_client
                    )
                ).setFilterByAuthorizedAccounts(false).build()
        ).setAutoSelectEnabled(true).build()

    private val authorize = Identity.getAuthorizationClient(context)

    private val firebaseAuth = Firebase.auth

    val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))

    suspend fun authorizeGoogleDrive():Result<AuthorizationResult>{
        return try {
            val authorizeResult = authorize.authorize(authorizeRequest).await()
            Result.success(authorizeResult)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    fun authorizeGoogleDriveIntent(intent: Intent):Result<AuthorizationResult>{
        return try {
            val getAuthorize = authorize.getAuthorizationResultFromIntent(intent)
            Result.success(getAuthorize)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
    suspend fun signInGoogle(): Result<IntentSender> {
        return runCatching {

            oneTap.beginSignIn(signInRequest).await().pendingIntent.intentSender
        }
    }

    suspend fun signOut() {
        oneTap.signOut().await()
        firebaseAuth.signOut()
    }

    suspend fun getSignInGoogleResult(intent: Intent): Result<UserInfo> {
        return runCatching {
            val credential = oneTap.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
            UserInfo(authResult.user!!.email!!)
        }
    }

    fun getSignInInfo(): UserInfo? {
        return firebaseAuth.currentUser?.run {
            UserInfo(email!!)
        }
    }

    suspend fun getDrive(): Drive? {
        val authUser = firebaseAuth.currentUser
        return if (authUser != null) {
            credential.selectedAccount = Account(authUser.email, "com.google")
            Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName("Masrofy").build()
        } else {
            null
        }
    }

    data class UserInfo(
        val email: String
    )

}



