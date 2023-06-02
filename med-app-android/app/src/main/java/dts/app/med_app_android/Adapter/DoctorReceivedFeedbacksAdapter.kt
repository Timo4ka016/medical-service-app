package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.Model.FeedbackDto
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.FeedbackCardBinding

class DoctorReceivedFeedbacksAdapter :
    ListAdapter<ClientMyFeedbacksItem, DoctorReceivedFeedbacksAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FeedbackCardBinding.bind(view)
        fun bind(feedback: ClientMyFeedbacksItem) = with(binding) {
            val rating = String.format("%.2f", feedback.rating)
            txtName.text = feedback.client.firstname + " " + feedback.client.lastname
            txtText.text = feedback.text
            txtRating.text = rating
        }
    }

    class Comparator : DiffUtil.ItemCallback<ClientMyFeedbacksItem>() {
        override fun areItemsTheSame(
            oldItem: ClientMyFeedbacksItem,
            newItem: ClientMyFeedbacksItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ClientMyFeedbacksItem,
            newItem: ClientMyFeedbacksItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.feedback_card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val feedback = getItem(position)
        holder.bind(feedback)
    }
}