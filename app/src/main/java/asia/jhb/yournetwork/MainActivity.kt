package asia.jhb.yournetwork

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import asia.jhb.yournetwork.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val SHARED_PREFS_KEY by lazy {
        this.localClassName
    }
    val sharedPref by lazy {
        getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }
    val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    val model = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        restorePreferences()
        binding.viewmodel = model
    }

    fun restorePreferences() {
        model.account_number = sharedPref.getString(getString(R.string.prefs_account_number), "").toString()
        model.account_password = sharedPref.getString(getString(R.string.prefs_account_password), "").toString()
        model.spinnerSelection = sharedPref.getInt(getString(R.string.prefs_account_type), 0)
        model.pc_mode = sharedPref.getBoolean(getString(R.string.prefs_pc_mode), false)
        model.save_password = sharedPref.getBoolean(getString(R.string.prefs_save_password), false)
    }

    fun savePreferences() {
        sharedPref.edit().apply {
            putString(getString(R.string.prefs_account_number), model.account_number)
            if (model.save_password)
                putString(getString(R.string.prefs_account_password), model.account_password)
            putInt(getString(R.string.prefs_account_type), model.spinnerSelection)
            putBoolean(getString(R.string.prefs_save_password), model.save_password)
            putBoolean(getString(R.string.prefs_pc_mode), model.pc_mode)
            commit()
        }
    }

    override fun onStop() {
        super.onStop()
        savePreferences()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                val projectAddress = getString(R.string.project_address)
                val uri: Uri = Uri.parse(projectAddress)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}