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

    // Create a data class to hold the information for each item
    data class TipsItem(
        val title: String,
        val description: String,
        val source: String
    )

    // Create an ArrayList to store multiple items
    private val tipsItemList = ArrayList<TipsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }

        // Initialize your ArrayList and Adapter if needed
        tipsItemArrayList = ArrayList()
        adapter = TipsItemAdapter(tipsItemArrayList)
        originalTipsItemList = ArrayList(tipsItemArrayList) // Store the original list

        // Populate the tipsItemList with data
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_1), getString(R.string.itemDesc_1), getString(R.string.itemSource_1)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_2), getString(R.string.itemDesc_2), getString(R.string.itemSource_2)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_3), getString(R.string.itemDesc_3), getString(R.string.itemSource_3)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_4), getString(R.string.itemDesc_4), getString(R.string.itemSource_4)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_5), getString(R.string.itemDesc_5), getString(R.string.itemSource_5)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_6), getString(R.string.itemDesc_6), getString(R.string.itemSource_6)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_7), getString(R.string.itemDesc_7), getString(R.string.itemSource_7)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_8), getString(R.string.itemDesc_8), getString(R.string.itemSource_8)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_9), getString(R.string.itemDesc_9), getString(R.string.itemSource_9)))
        tipsItemList.add(TipsItem(getString(R.string.itemTitle_10), getString(R.string.itemDesc_10), getString(R.string.itemSource_10)))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_tips_and_advice, container, false)

        // Find the LinearLayout by its ID
        val linearLayoutItem1: View = rootView.findViewById(R.id.linearLayoutItem1)
        val linearLayoutItem2: View = rootView.findViewById(R.id.linearLayoutItem2)
        val linearLayoutItem3: View = rootView.findViewById(R.id.linearLayoutItem3)
        val linearLayoutItem4: View = rootView.findViewById(R.id.linearLayoutItem4)
        val linearLayoutItem5: View = rootView.findViewById(R.id.linearLayoutItem5)
        val linearLayoutItem6: View = rootView.findViewById(R.id.linearLayoutItem6)
        val linearLayoutItem7: View = rootView.findViewById(R.id.linearLayoutItem7)
        val linearLayoutItem8: View = rootView.findViewById(R.id.linearLayoutItem8)
        val linearLayoutItem9: View = rootView.findViewById(R.id.linearLayoutItem9)
        val linearLayoutItem10: View = rootView.findViewById(R.id.linearLayoutItem10)

        // Set OnClickListener to the LinearLayout
        linearLayoutItem1.setOnClickListener {
            val clickedItem = tipsItemList[0] // Change the index according to the clicked item
            // Show a dialog when the LinearLayout is clicked
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem2.setOnClickListener {
            val clickedItem = tipsItemList[1]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem3.setOnClickListener {
            val clickedItem = tipsItemList[2]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem4.setOnClickListener {
            val clickedItem = tipsItemList[3]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem5.setOnClickListener {
            val clickedItem = tipsItemList[4]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem6.setOnClickListener {
            val clickedItem = tipsItemList[5]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem7.setOnClickListener {
            val clickedItem = tipsItemList[6]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem8.setOnClickListener {
            val clickedItem = tipsItemList[7]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem9.setOnClickListener {
            val clickedItem = tipsItemList[8]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        linearLayoutItem10.setOnClickListener {
            val clickedItem = tipsItemList[9]
            showDialog(clickedItem.title, clickedItem.description, clickedItem.source)
        }

        return rootView
    }

    // Function to show a dialog when the LinearLayout is clicked
    private fun showDialog(title:String, description: String, source:String) {
        val dialogView = layoutInflater.inflate(R.layout.tips_dialog_box, null)
        val titleText = dialogView.findViewById<TextView>(R.id.titleText)
        val descriptionText = dialogView.findViewById<TextView>(R.id.descriptionText)
        val sourceText = dialogView.findViewById<TextView>(R.id.sourceText)

        titleText.text = title
        descriptionText.text = description
        sourceText.text = source

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
