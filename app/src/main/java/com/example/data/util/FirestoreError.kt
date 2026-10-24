package com.example.data.util

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException

enum class OperationType {
  CREATE, GET, LIST, UPDATE, DELETE
}

fun handleFirestoreError(
  exception: Exception,
  operationType: OperationType,
  path: String
) {
  if (exception is kotlinx.coroutines.CancellationException) {
    return
  }
  val auth = Firebase.auth
  val currentUser = auth.currentUser
  val isPermissionDenied = exception is FirebaseFirestoreException &&
    exception.code == FirebaseFirestoreException.Code.PERMISSION_DENIED

  val errorLog = buildString {
    append("Firestore Error:\n")
    append("  Operation: ").append(operationType.name.lowercase()).append("\n")
    append("  Path: ").append(path).append("\n")
    append("  Exception: ").append(exception.javaClass.simpleName).append("\n")
    append("  Message: ").append(exception.message).append("\n")
    append("  User ID: ").append(currentUser?.uid ?: "Unauthenticated").append("\n")
    append("  Permission Denied: ").append(isPermissionDenied)
  }

  Log.e("FirestoreError", errorLog, exception)
}
