package dts.app.med_app_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dts.app.med_app_android.Fragment.ReceivedAppointmentsFragment
import dts.app.med_app_android.Model.FavoriteAdsDtoItem
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.ReceivedAppointmentCardBinding

class AppointmentsAdapter(private val onAcceptClickListener: OnAcceptClickListener) :
    ListAdapter<MyAppointmentsDtoItem, AppointmentsAdapter.Holder>(Comparator()) {

    interface OnAcceptClickListener {
        fun onAcceptClick(appointmentId: Long)
    }

    class Holder(view: View, private val onAcceptClickListener: OnAcceptClickListener) : RecyclerView.ViewHolder(view) {
        private val binding = ReceivedAppointmentCardBinding.bind(view)

        fun bind(appointment: MyAppointmentsDtoItem) = with(binding) {
            txtTitle.text = appointment.ad.title
            txtName.text = "Имя: " + appointment.doctor.firstname + appointment.doctor.lastname
            txtAddress.text = "Адресс: " + appointment.ad.address
            txtCategory.text = appointment.ad.category.name
            txtYourPrice.text = appointment.ad.price.toString() + " KZT"
            txtClientPrice.text = appointment.desiredPrice.toString() + " KZT"
            txtNoteMessage.text = "Примечание: " + appointment.message

            btnAccept.setOnClickListener {
                val appointmentId = appointment.id
                onAcceptClickListener.onAcceptClick(appointmentId)
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

    fun removeAppointmentById(appointmentId: Long) {
        val updatedList = currentList.filter { it.id != appointmentId }
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.received_appointment_card, parent, false)
        return AppointmentsAdapter.Holder(view, onAcceptClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val appointmentList = currentList

        if (position < appointmentList.size) {
            val ad = appointmentList[position]
            holder.bind(ad)
        }
    }
}