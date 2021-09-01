@file:JvmName("Util")

package asia.jhb.yournetwork

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

fun sendGet(url: String, param: String = ""): String {
    val urlNameString = if (param != "") "$url?$param" else url
    val realUrl = URL(urlNameString)
    val connection = realUrl.openConnection()
    connection.connect()
    val input = BufferedReader(InputStreamReader(connection.getInputStream()))
    val result = StringBuilder()
    input.lineSequence().forEach { result.append(it) }
    return result.toString()
}

fun getLoginUrl(account_number: String, account_password: String, account_type: Type, pc_mode: Boolean) =
    StringBuilder().apply {
        append(
            "http://30.30.30.29/drcom/login" +
                    "?" +
                    "callback=dr1003" +
                    "&DDDDD=${account_number}${account_type.toUrlParam()}" +
                    "&upass=${account_password}"
        )
        if (account_type == Type.Campus)
            append("&0MKKey=123")
        if (!pc_mode)
            append("&R6=1")
    }.toString()

fun displayResult(context: Context, result: Result) = when (result) {
    is Result.Undefined ->
        AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, null).setTitle(result.message)
            .setNegativeButton(R.string.copy) { _, _ ->
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(ClipData.newPlainText("Label", result.response))
                Toast.makeText(context, R.string.copied, Toast.LENGTH_LONG).show()
            }
            .setMessage(result.response).show()
    else -> Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
}