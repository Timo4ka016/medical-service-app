package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.MyAppointmentCardBinding

class ClientMySentAppointAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MyAppointmentsDtoItem, ClientMySentAppointAdapter.Holder>(Comparator()) {

    interface OnClickListener {
        fun onClick(adId: Long)
    }

    class Holder(view: View, private val onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(view) {
        private val binding = MyAppointmentCardBinding.bind(view)

        fun bind(appointment: MyAppointmentsDtoItem) = with(binding) {
            txtTitle.text = appointment.ad.title
            txtName.text = "Имя: " + appointment.doctor.firstname + appointment.doctor.lastname
            txtAddress.text = "Адресс: " + appointment.ad.address
            txtCategory.text = appointment.ad.category.name
            txtYourPrice.text = appointment.ad.price.toString() + " KZT"
            txtClientPrice.text = appointment.desiredPrice.toString() + " KZT"
            txtNoteMessage.text = "Примечание: " + appointment.message

            itemView.setOnClickListener {
                val adId = appointment.ad.id
                onClickListener.onClick(adId)
            }

        }
    }

    class Comparator : DiffUtil.ItemCallback<MyAppointmentsDtoItem>() {
        override fun areItemsTheSame(
            oldItem: MyAppointmentsDtoItem,
            newItem: MyAppointmentsDtoItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MyAppointmentsDtoItem,
            newItem: MyAppointmentsDtoItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_appointment_card, parent, false)
        return Holder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val appointmentList = currentList

        if (position < appointmentList.size) {
            val ad = appointmentList[position]
            holder.bind(ad)
        }
    }
}