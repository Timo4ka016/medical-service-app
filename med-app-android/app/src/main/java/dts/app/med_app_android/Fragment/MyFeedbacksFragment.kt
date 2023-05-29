package dts.app.med_app_android.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dts.app.med_app_android.Adapter.MyFeedbacksAdapter
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.Model.FeedbackModel
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.FragmentMyFeedbacksBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFeedbacksFragment : Fragment(), MyFeedbacksAdapter.OnDeleteClickListener,
    MyFeedbacksAdapter.OnFeedbackClickListener, MyFeedbacksAdapter.OnUpdateClickListener {
    private lateinit var binding: FragmentMyFeedbacksBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var clientService: ClientService
    private lateinit var adapter: MyFeedbacksAdapter
    private lateinit var editFeedbackDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tokenManager = TokenManager(requireContext())
        adapter = MyFeedbacksAdapter(this, this, this)
        binding = FragmentMyFeedbacksBinding.inflate(inflater, container, false)
        editFeedbackDialog = Dialog(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        binding.rcFeedbacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedbacks.adapter = adapter
        getMyFeedbacks()

        return binding.root
    }

    private fun getMyFeedbacks() = with(binding) {
        val callGetMyFeedbacks = clientService.getMyFeedbacks()
        callGetMyFeedbacks.enqueue(object : Callback<List<ClientMyFeedbacksItem>> {
            override fun onResponse(
                call: Call<List<ClientMyFeedbacksItem>>,
                response: Response<List<ClientMyFeedbacksItem>>
            ) {
                if (response.isSuccessful) {
                    val feedbacks = response.body()
                    Log.i("Response good", response.body().toString())
                    if (feedbacks.isNullOrEmpty()) {
                        linearEmpty.visibility = View.VISIBLE
                    } else {
                        linearEmpty.visibility = View.GONE
                        adapter.submitList(feedbacks)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<ClientMyFeedbacksItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun deleteMyFeedback(feedbackId: Long) = with(binding) {
        val callDeleteMyFeedback = clientService.deleteFeedback(feedbackId)
        callDeleteMyFeedback.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getMyFeedbacks()
                    adapter.removeFeedbackById(feedbackId)
                    Log.i("Response good", response.body().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to delete feedback: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response Error", "Failed to delete feedback: ${t.message}")
            }
        })
    }

    private fun updateFeedback(feedbackId: Long) = with(binding) {
        val btnSend = editFeedbackDialog.findViewById<MaterialButton>(R.id.btn_send)
        btnSend.setOnClickListener {
            val body = editFeedbackBody() ?: return@setOnClickListener
            val callUpdateFeedback = clientService.updateFeedback(feedbackId, body)
            callUpdateFeedback.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        getMyFeedbacks()
                        Toast.makeText(requireContext(), "Спасибо за отзыв", Toast.LENGTH_SHORT)
                            .show()
                        editFeedbackDialog.dismiss()
                        Log.i("Response good", response.body().toString())
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to update feedback: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.i("Response Error", "Failed to update feedback: ${t.message}")
                }
            })
        }
    }

    private fun updateFeedbackDialog(feedbackId: Long) {
        editFeedbackDialog.setContentView(R.layout.create_feedback)
        editFeedbackDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        editFeedbackDialog.setCancelable(true)
        updateFeedback(feedbackId)
        editFeedbackDialog.show()
    }

    private fun editFeedbackBody(): FeedbackModel? {
        val ratingBar = editFeedbackDialog.findViewById<RatingBar>(R.id.rb_feedback_rating)
        val inputFeedbackText =
            editFeedbackDialog.findViewById<TextInputEditText>(R.id.input_feedback_text)
        val ratingText = ratingBar.rating.toString()
        val text = inputFeedbackText.text?.trim().toString()
        if (text.isEmpty() || ratingText.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return null
        } else {
            val rating: Double = ratingText.toDouble()

            return FeedbackModel(
                text = text,
                rating = rating
            )
        }
    }


    override fun onButtonsClick(feedbackId: Long) {
        deleteMyFeedback(feedbackId)
        updateFeedbackDialog(feedbackId)
    }

    override fun onFeedbackClick(doctorId: Long) {
        val bundle = Bundle()
        bundle.putLong("doctorId", doctorId)
        Log.i("BRUH", bundle.toString())
        findNavController().navigate(
            R.id.action_myFeedbacksFragment_to_doctorProfileDetailsFragment,
            bundle
        )
    }

    override fun onUpdateClick(feedbackId: Long) {
        updateFeedbackDialog(feedbackId)
    }

}