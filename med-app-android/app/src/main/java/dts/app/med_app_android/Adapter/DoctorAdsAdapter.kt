package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.GetMyAdsRequestItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.AdCardBinding


class DoctorAdsAdapter : ListAdapter<GetMyAdsRequestItem, DoctorAdsAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = AdCardBinding.bind(view)

        fun bind(myAdsRequestItem: GetMyAdsRequestItem) = with(binding) {
            txtTitle.text = myAdsRequestItem.title
            txtAddress.text = myAdsRequestItem.address
            txtPrice.text = myAdsRequestItem.price.toString() + " KZT"
            txtRating.text = myAdsRequestItem.rating.toString()
            txtCategory.text = myAdsRequestItem.category
        }
    }

    class Comparator : DiffUtil.ItemCallback<GetMyAdsRequestItem>() {
        override fun areItemsTheSame(
            oldItem: GetMyAdsRequestItem,
            newItem: GetMyAdsRequestItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetMyAdsRequestItem,
            newItem: GetMyAdsRequestItem
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
    }
}