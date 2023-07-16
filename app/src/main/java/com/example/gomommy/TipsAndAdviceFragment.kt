package com.example.gomommy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Date
import java.util.Locale.filter
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [TipsAndAdviceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TipsAndAdviceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapter: TipsItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tipsItemArrayList: ArrayList<tipsItem>
    private lateinit var originalTipsItemList: ArrayList<tipsItem>
    private lateinit var searchView: SearchView
    //Weekly Tips And Advice
    private lateinit var weekTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var dueDate: Date

    lateinit var itemTitle: Array<String>
    lateinit var itemDesc: Array<String>
    lateinit var itemSource: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //SearchView and RecyclerView
        tipsItemArrayList = ArrayList()
        adapter = TipsItemAdapter(tipsItemArrayList)
        originalTipsItemList = ArrayList(tipsItemArrayList) // Store the original list

        // Initialize dbRef
        dbRef = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tips_and_advice, container, false)

        // Initialize the TextViews
        weekTextView = rootView.findViewById(R.id.tipsTitle)
        infoTextView = rootView.findViewById(R.id.tipsDesc)

        //Retrieve dueDate from Firebase database
        readDueDate()

        return rootView
    }


    private fun displayCurrentWeek() {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        // Calculate the difference between the current date and the due date
        val differenceInMillis = dueDate.time - currentDate.time
        val weeksDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis) / 7

        // Display the current week of pregnancy
        weekTextView.text = getString(R.string.week_of_pregnancy, weeksDifference)

        // Display the information based on the current week
        val infoResourceId = when (weeksDifference.toInt()) {
            1, 2, 3 -> R.string.tips_week_1_to_3
            4 -> R.string.tips_week_4
            5 -> R.string.tips_week_5
            6 -> R.string.tips_week_6
            7 -> R.string.tips_week_7
            8 -> R.string.tips_week_8
            9 -> R.string.tips_week_9
            10 -> R.string.tips_week_10
            // Add cases for other weeks up to 40
            else -> R.string.info_default
        }
        infoTextView.text = getString(infoResourceId)
    }


    private fun displayCurrentWeek(dueDate: Any?) {
        if (dueDate != null) {
            val calendar = Calendar.getInstance()
            val currentDate = calendar.time

            val differenceInMillis = (dueDate as Long) - currentDate.time
            val weeksDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis) / 7

            weekTextView.text = getString(R.string.week_of_pregnancy, weeksDifference)

            val infoResourceId = when (weeksDifference.toInt()) {
                1, 2, 3 -> R.string.tips_week_1_to_3
                4 -> R.string.tips_week_4
                5 -> R.string.tips_week_5
                6 -> R.string.tips_week_6
                7 -> R.string.tips_week_7
                8 -> R.string.tips_week_8
                9 -> R.string.tips_week_9
                10 -> R.string.tips_week_10
                // Add cases for other weeks up to 40
                else -> R.string.info_default
            }
            infoTextView.text = getString(infoResourceId)
        }
    }

    private fun readDueDate() {
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener { dataSnapshot ->
            Log.i("firebase", "Got value ${dataSnapshot.value}")
            val dueDate = dataSnapshot.value
            displayCurrentWeek(dueDate)
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data", exception)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TipsAndAdviceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TipsAndAdviceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.tipsRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = TipsItemAdapter(tipsItemArrayList)
        recyclerView.adapter = adapter


        //search
//        searchView = view.findViewById(R.id.tipsSearchView)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter.filter(newText)
//
//                return true
//            }
//        })
    }




    private fun dataInitialize(){

        tipsItemArrayList = ArrayList()
        tipsItemArrayList = arrayListOf<tipsItem>()

        itemTitle = arrayOf(
            getString(R.string.itemTitle_1),
            getString(R.string.itemTitle_2),
            getString(R.string.itemTitle_3),
            getString(R.string.itemTitle_4),
            getString(R.string.itemTitle_5),
            getString(R.string.itemTitle_6),
            getString(R.string.itemTitle_7),
            getString(R.string.itemTitle_8),
            getString(R.string.itemTitle_9),
            getString(R.string.itemTitle_10),
            getString(R.string.itemTitle_11),
            getString(R.string.itemTitle_12),
            getString(R.string.itemTitle_13),
            getString(R.string.itemTitle_14),
            getString(R.string.itemTitle_15),
            getString(R.string.itemTitle_16),
            getString(R.string.itemTitle_17),
            getString(R.string.itemTitle_18),
            getString(R.string.itemTitle_19),
            getString(R.string.itemTitle_20),
            getString(R.string.itemTitle_21),
            getString(R.string.itemTitle_22)


        )

        itemDesc= arrayOf(
            getString(R.string.itemDesc_1),
            getString(R.string.itemDesc_2),
            getString(R.string.itemDesc_3),
            getString(R.string.itemDesc_4),
            getString(R.string.itemDesc_5),
            getString(R.string.itemDesc_6),
            getString(R.string.itemDesc_7),
            getString(R.string.itemDesc_8),
            getString(R.string.itemDesc_9),
            getString(R.string.itemDesc_10),
            getString(R.string.itemDesc_11),
            getString(R.string.itemDesc_12),
            getString(R.string.itemDesc_13),
            getString(R.string.itemDesc_14),
            getString(R.string.itemDesc_15),
            getString(R.string.itemDesc_16),
            getString(R.string.itemDesc_17),
            getString(R.string.itemDesc_18),
            getString(R.string.itemDesc_19),
            getString(R.string.itemDesc_20),
            getString(R.string.itemDesc_21),
            getString(R.string.itemDesc_22)


        )

        itemSource = arrayOf(
            getString(R.string.itemSource_1),
            getString(R.string.itemSource_2),
            getString(R.string.itemSource_3),
            getString(R.string.itemSource_4),
            getString(R.string.itemSource_5),
            getString(R.string.itemSource_6),
            getString(R.string.itemSource_7),
            getString(R.string.itemSource_8),
            getString(R.string.itemSource_9),
            getString(R.string.itemSource_10),
            getString(R.string.itemSource_11),
            getString(R.string.itemSource_12),
            getString(R.string.itemSource_13),
            getString(R.string.itemSource_14),
            getString(R.string.itemSource_15),
            getString(R.string.itemSource_16),
            getString(R.string.itemSource_17),
            getString(R.string.itemSource_18),
            getString(R.string.itemSource_19),
            getString(R.string.itemSource_20),
            getString(R.string.itemSource_21),
            getString(R.string.itemSource_22)


        )

        for (i in itemTitle.indices) {
            val title = itemTitle[i]
            val desc = itemDesc[i]
            val source = itemSource[i]

            val tipsItem = tipsItem(title, desc, source)
            tipsItemArrayList.add(tipsItem)

            // Log the data for verification
            Log.d("TipsAndAdviceFragment", "Title: $title, Desc: $desc, Source: $source")
        }

    }

}