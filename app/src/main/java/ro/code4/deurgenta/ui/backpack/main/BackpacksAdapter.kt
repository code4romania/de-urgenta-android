package ro.code4.deurgenta.ui.backpack.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Backpack

class BackpacksAdapter(
    private val context: Context,
    private val backpackSelectedHandler: (Backpack) -> Unit,
) : ListAdapter<Backpack, BackpackItemViewHolder>(diffCallback) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackpackItemViewHolder {
        val rowView = inflater.inflate(R.layout.item_backpack, parent, false)
        return BackpackItemViewHolder(rowView, backpackSelectedHandler)
    }

    override fun onBindViewHolder(holder: BackpackItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<Backpack>() {

    override fun areItemsTheSame(oldItem: Backpack, newItem: Backpack) = oldItem === newItem

    override fun areContentsTheSame(oldItem: Backpack, newItem: Backpack) = oldItem.id == newItem.id &&
            oldItem.name == newItem.name
}

class BackpackItemViewHolder(rowView: View, handler: (Backpack) -> Unit) : RecyclerView.ViewHolder(rowView) {
    private val label: TextView = rowView.findViewById(R.id.backpack)
    private var backpack: Backpack? = null

    init {
        label.setOnClickListener { backpack?.let { handler(it) } }
    }

    fun bind(backpack: Backpack) {
        this.backpack = backpack
        label.text = backpack.name
        label.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(label.context, R.drawable.ic_shopping_bag), null, null, null
        )
    }
}
