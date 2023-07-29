package com.example.gomommy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class TipsAndAdviceFragment : Fragment() {

    private lateinit var adapter: TipsItemAdapter
    private lateinit var tipsItemArrayList: ArrayList<tipsItem>
    private lateinit var originalTipsItemList: ArrayList<tipsItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }

        // Initialize your ArrayList and Adapter if needed
        tipsItemArrayList = ArrayList()
        adapter = TipsItemAdapter(tipsItemArrayList)
        originalTipsItemList = ArrayList(tipsItemArrayList) // Store the original list
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_tips_and_advice, container, false)

        // Find the LinearLayout by its ID
        val linearLayoutItem: View = rootView.findViewById(R.id.dietLayout)

        // Set OnClickListener to the LinearLayout
        linearLayoutItem.setOnClickListener {
            // Show a dialog when the LinearLayout is clicked

            showDialog("Description")
        }

        // Initialize RecyclerView and Adapter (if needed)
        // ...

        return rootView
    }

    // Function to show a dialog when the LinearLayout is clicked
    private fun showDialog(description: String) {
        val dialogView = layoutInflater.inflate(R.layout.tips_dialog_box, null)
        val descriptionText = dialogView.findViewById<TextView>(R.id.descriptionText)

        descriptionText.text = description

        val dialogBuilder = android.app.AlertDialog.Builder(requireContext(), R.style.CustomDialogStyle2)
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Set click listener for the close button
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}
