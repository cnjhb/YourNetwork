package asia.jhb.yournetwork

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import kotlin.concurrent.thread
import kotlin.coroutines.resume

class MainViewModel : ViewModel() {
    lateinit var account_number: String
    lateinit var account_password: String
    lateinit var account_type: Type
    var logging = ObservableBoolean(false)
    var spinnerSelection: Int
        get() = if (account_type == Type.Campus) 0 else 1
        set(value) {
            account_type = if (value == 0) Type.Campus else Type.Telecom
        }
    var save_password = false
    var pc_mode = false
    fun login(context: Context) {
        if (account_number == "" || account_password == "") {
            displayResult(context, Result.InputIncomplete)
            return
        }
        logging.set(true)
        val json_regex = Regex("[{].+[}]")
        var loginUrl = getLoginUrl(account_number, account_password, account_type, pc_mode)
        viewModelScope.launch {
            val data = withTimeoutOrNull(3000) {
                return@withTimeoutOrNull suspendCancellableCoroutine<String> {
                    thread {
                        it.resume(sendGet(loginUrl))
                    }
                }
            }
            val result = when (data) {
                null -> Result.TimeOut
                else -> {
                    val json = JSONObject(json_regex.find(data)!!.value)
                    when (json.getInt("result")) {
                        1 -> Result.Success
                        else -> {
                            var msga = json.getString("msga")
                            when {
                                msga.contains("pc") -> Result.InusePC
                                msga.contains("mobile") -> Result.InuseMobile
                                else -> Result.Undefined.apply { response = data }
                            }
                        }
                    }
                }
            }
            logging.set(false)
            displayResult(context, result)
        }
    }
}

sealed interface Type {
    object Telecom : Type {
        override fun toUrlParam() = "@telecom"
    }

    object Campus : Type {
        override fun toUrlParam() = ""
    }

    fun toUrlParam(): String
}