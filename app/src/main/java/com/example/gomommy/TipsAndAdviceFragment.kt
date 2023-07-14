package com.example.gomommy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import java.util.Locale.filter

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

    private lateinit var adapter: TipsItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tipsItemArrayList: ArrayList<tipsItem>
    private lateinit var originalTipsItemList: ArrayList<tipsItem>
    private lateinit var searchView: SearchView

    lateinit var itemTitle: Array<String>
    lateinit var itemDesc: Array<String>
    lateinit var itemSource: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        tipsItemArrayList = ArrayList()
        adapter = TipsItemAdapter(tipsItemArrayList)
        originalTipsItemList = ArrayList(tipsItemArrayList) // Store the original list

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tips_and_advice, container, false)
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
        searchView = view.findViewById(R.id.tipsSearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)

                return true
            }
        })
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
            getString(R.string.itemTitle_10)


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
            getString(R.string.itemDesc_10)

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
            getString(R.string.itemSource_10)

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