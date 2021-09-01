@file:JvmName("BindingAdapters")

package asia.jhb.yournetwork

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@InverseBindingAdapter(attribute = "app:selection")
fun Spinner.getSelection() = this.selectedItemPosition

@BindingAdapter("app:selectionAttrChanged")
fun Spinner.setListener(listener: InverseBindingListener) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener.onChange()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}