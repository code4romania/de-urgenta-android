package ro.code4.deurgenta.ui.courses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Course
import ro.code4.deurgenta.helper.dateFormatter

class CoursesAdapter(
    context: Context,
    private val detailsRequestedHandler: (Course) -> Unit
) : ListAdapter<Course, CourseViewHolder>(courseDiffCallback) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(inflater.inflate(R.layout.item_course, parent, false), detailsRequestedHandler)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CourseViewHolder(
    rowView: View,
    private val handler: (Course) -> Unit
) : RecyclerView.ViewHolder(rowView) {

    private val name: TextView = rowView.findViewById(R.id.name)
    private val provider: TextView = rowView.findViewById(R.id.provider)
    private val date: TextView = rowView.findViewById(R.id.date)
    private val location: TextView = rowView.findViewById(R.id.location)
    private val details: Button = rowView.findViewById(R.id.btn_details)
    private var course: Course? = null

    init {
        details.setOnClickListener {
            course?.let { handler(it) }
        }
    }

    fun bind(course: Course) {
        course.let {
            this.course = course
            name.text = it.name
            provider.text = it.provider
            date.text = dateFormatter.format(it.date)
            location.text = it.location
        }
    }

}

private val courseDiffCallback = object : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

}