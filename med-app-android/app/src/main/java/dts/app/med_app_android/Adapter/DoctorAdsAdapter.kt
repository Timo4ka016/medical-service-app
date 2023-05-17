package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.GetDoctorAdsItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.AdCardBinding


class DoctorAdsAdapter(private val onAdClickListener: OnAdClickListener) : ListAdapter<GetDoctorAdsItem, DoctorAdsAdapter.Holder>(Comparator()) {

    interface OnAdClickListener {
        fun onAdClick(adId: Long)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = AdCardBinding.bind(view)

        fun bind(getDoctorAdsItem: GetDoctorAdsItem) = with(binding) {
            val rating = String.format("%.2f", getDoctorAdsItem.rating)
            txtTitle.text = getDoctorAdsItem.title
            txtAddress.text = getDoctorAdsItem.address
            txtPrice.text = getDoctorAdsItem.price.toString() + " KZT"

            txtRating.text = rating
            txtCategory.text = getDoctorAdsItem.category
            txtName.text = getDoctorAdsItem.doctor?.firstname + " " + getDoctorAdsItem?.doctor?.lastname
        }
    }

    class Comparator : DiffUtil.ItemCallback<GetDoctorAdsItem>() {
        override fun areItemsTheSame(
            oldItem: GetDoctorAdsItem,
            newItem: GetDoctorAdsItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetDoctorAdsItem,
            newItem: GetDoctorAdsItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val adId = getItem(position).id
            onAdClickListener.onAdClick(adId.toLong())
        }
    }
}