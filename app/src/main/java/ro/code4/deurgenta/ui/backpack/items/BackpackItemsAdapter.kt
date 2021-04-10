package ro.code4.deurgenta.ui.backpack.items

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.BackpackItem


class BackpackItemsAdapter(
    context: Context,
    private val itemSelectedHandler: (BackpackItem) -> Unit,
    private val deleteItemRequested: (String) -> Unit
) : ListAdapter<BackpackItem, BackpackTypeViewHolder>(diffCallback) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackpackTypeViewHolder {
        val rowView = inflater.inflate(R.layout.item_backpack_item, parent, false)
        return BackpackTypeViewHolder(rowView, itemSelectedHandler, deleteItemRequested)
    }

    override fun onBindViewHolder(holder: BackpackTypeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<BackpackItem>() {
    override fun areItemsTheSame(oldItem: BackpackItem, newItem: BackpackItem) = oldItem === newItem

    override fun areContentsTheSame(oldItem: BackpackItem, newItem: BackpackItem) =
        oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.amount == newItem.amount && oldItem
            .type == newItem.type && oldItem.backpackId == newItem.backpackId && oldItem.expirationDate == newItem.expirationDate

}

class BackpackTypeViewHolder(
    rowView: View,
    itemSelectedHandler: (BackpackItem) -> Unit,
    deleteItemRequested: (String) -> Unit
) : RecyclerView.ViewHolder(rowView) {

    private val expirationString = rowView.context.getString(R.string.item_expiration_label)
    private val noExpirationString = rowView.context.getString(R.string.no_expiration_date)
    private val quantityString = rowView.context.getString(R.string.item_quantity_label)
    private val quantity: TextView = rowView.findViewById(R.id.quantity)
    private val name: TextView = rowView.findViewById(R.id.name)
    private val expireDate: TextView = rowView.findViewById(R.id.expiry_date)
    private val deleteBtn: Button = rowView.findViewById(R.id.delete_item)
    private var item: BackpackItem? = null

    init {
        rowView.setOnClickListener { item?.let { itemSelectedHandler(it) } }
        deleteBtn.setOnClickListener { item?.let { deleteItemRequested(it.id) } }
    }

    fun bind(item: BackpackItem) {
        this.item = item
        with(item) {
            this@BackpackTypeViewHolder.name.text = name
            quantity.text = String.format(quantityString, amount.toString())
            expireDate.text = String.format(expirationString, expirationDate?.format(formatter) ?: noExpirationString)
        }
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    }
}