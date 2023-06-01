package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.FavoriteAdsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.FavoriteAdCardBinding

class FavoriteAdsAdapter(
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onGoProfileClickListener: OnGoProfileClickListener,
    private val onAdClickListener: OnAdClickListener
) :
    ListAdapter<FavoriteAdsDtoItem, FavoriteAdsAdapter.Holder>(Comparator()) {

    interface OnDeleteClickListener {
        fun onDeleteClick(adId: Long)
    }

    interface OnGoProfileClickListener {
        fun onGoProfileClick(doctorId: Long)
    }

    interface OnAdClickListener {
        fun onAdClick(adId: Long)
    }


    class Holder(
        view: View,
        private val onDeleteClickListener: OnDeleteClickListener,
        private val onGoProfileClickListener: OnGoProfileClickListener,
        private val onAdClickListener: OnAdClickListener
    ) :
        RecyclerView.ViewHolder(view) {
        private val binding = FavoriteAdCardBinding.bind(view)

        fun bind(favoriteAd: FavoriteAdsDtoItem) = with(binding) {
            val firstname = favoriteAd.ad.doctor.firstname
            val lastname = favoriteAd.ad.doctor.lastname
            val rating = String.format("%.2f", favoriteAd.ad.rating)
            txtTitle.text = favoriteAd.ad.title
            txtName.text = "Имя: ${firstname} ${lastname}."
            txtAddress.text = favoriteAd.ad.address
            txtCategory.text = favoriteAd.ad.category.name
            txtPrice.text = favoriteAd.ad.price.toString() + "KZT"
            txtRating.text = rating

            btnDelete.setOnClickListener {
                val adId = favoriteAd.ad.id
                onDeleteClickListener.onDeleteClick(adId)
            }

            btnGoProfile.setOnClickListener {
                val doctorId = favoriteAd.ad.doctor.id
                onGoProfileClickListener.onGoProfileClick(doctorId.toLong())
            }

            itemView.setOnClickListener{
                val adId = favoriteAd.ad.id
                onAdClickListener.onAdClick(adId)
            }

        }
    }

    class Comparator : DiffUtil.ItemCallback<FavoriteAdsDtoItem>() {
        override fun areItemsTheSame(
            oldItem: FavoriteAdsDtoItem,
            newItem: FavoriteAdsDtoItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteAdsDtoItem,
            newItem: FavoriteAdsDtoItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    fun removeFavoriteAdById(adId: Long) {
        val updatedList = currentList.filter { it.id.toLong() != adId }
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_ad_card, parent, false)
        return FavoriteAdsAdapter.Holder(view, onDeleteClickListener, onGoProfileClickListener, onAdClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val favoriteList = currentList

        if (position < favoriteList.size) {
            val ad = favoriteList[position]
            holder.bind(ad)
        }
    }
}