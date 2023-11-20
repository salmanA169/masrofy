package com.masrofy.core.drive

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.firebase.auth.GoogleAuthProvider

// TODO: continue here
class GoogleDriveManager(context: Context) {
    init {
//        Drive.Builder(
//            GoogleNetHttpTransport.newTrustedTransport(),
//            GsonFactory.getDefaultInstance(),
//
//        )
//
//        Identity
//            .getAuthorizationClient(context).authorize(AuthorizationRequest.Builder().build()).result.hasResolution()
    }
}