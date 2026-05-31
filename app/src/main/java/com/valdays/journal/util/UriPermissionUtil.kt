package com.valdays.journal.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object UriPermissionUtil {
    fun takePersistableUriPermission(context: Context, uri: Uri) {
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
