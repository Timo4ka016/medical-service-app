package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.FeedbackCardBinding

class MyFeedbacksAdapter(private val onFeedbackClickListener: OnFeedbackClickListener ): ListAdapter<ClientMyFeedbacksItem, MyFeedbacksAdapter.Holder>(Comparator()) {

    interface OnFeedbackClickListener {
        fun onFeedbackClick(feedbackId: Long)
    }

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feedback_card, parent, false)
        return MyFeedbacksAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val feedbackId = getItem(position).id
            onFeedbackClickListener.onFeedbackClick(feedbackId.toLong())
        }
    }
}