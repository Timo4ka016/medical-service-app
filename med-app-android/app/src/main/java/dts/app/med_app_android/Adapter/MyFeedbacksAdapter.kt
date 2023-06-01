package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.MyFeedbacksCardBinding

class MyFeedbacksAdapter(
    private val onFeedbackClickListener: OnFeedbackClickListener,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onUpdateClickListener: OnUpdateClickListener
) : ListAdapter<ClientMyFeedbacksItem, MyFeedbacksAdapter.Holder>(Comparator()) {

    interface OnUpdateClickListener {
        fun onUpdateClick(feedbackId: Long)
    }

    interface OnFeedbackClickListener {
        fun onFeedbackClick(doctorId: Long)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(feedbackId: Long)
    }

    class Holder(
        view: View,
        private val onDeleteClickListener: OnDeleteClickListener,
        private val onFeedbackClickListener: OnFeedbackClickListener,
        private val onUpdateClickListener: OnUpdateClickListener
    ) : RecyclerView.ViewHolder(view) {
        private val binding = MyFeedbacksCardBinding.bind(view)

        fun bind(feedback: ClientMyFeedbacksItem) = with(binding) {
            val rating = String.format("%.2f", feedback.rating)
            txtName.text = feedback.client.firstname + " " + feedback.client.lastname
            txtText.text = feedback.text
            txtRating.text = rating

            imgDelete.setOnClickListener {
                val feedbackId = feedback.id
                onDeleteClickListener.onDeleteClick(feedbackId.toLong())
            }

            btnEdit.setOnClickListener {
                val feedbackId = feedback.id
                onUpdateClickListener.onUpdateClick(feedbackId.toLong())
            }

            itemView.setOnClickListener {
                val doctorId = feedback.doctorId
                onFeedbackClickListener.onFeedbackClick(doctorId)
            }
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

    fun removeFeedbackById(feedbackId: Long) {
        val updatedList = currentList.filter { it.id.toLong() != feedbackId }
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_feedbacks_card, parent, false)
        return MyFeedbacksAdapter.Holder(view, onDeleteClickListener, onFeedbackClickListener, onUpdateClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val feedbacksList = currentList

        if (position < feedbacksList.size) {
            val feedback = feedbacksList[position]
            holder.bind(feedback)
        }
    }
}