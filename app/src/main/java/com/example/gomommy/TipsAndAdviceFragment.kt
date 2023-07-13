package com.example.gomommy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

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

    lateinit var tipsItemTitleTextView: Array<String>
    lateinit var tipsItemDescTextView: Array<String>
    lateinit var tipsItemSourceTextView: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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



    private fun dataInitialize(){

        tipsItemArrayList = arrayListOf<tipsItem>()

        tipsItemTitleTextView = arrayOf(
            getString(R.string.tipsItemTitleTextView_1),
            getString(R.string.tipsItemTitleTextView_2),
            getString(R.string.tipsItemTitleTextView_3),
            getString(R.string.tipsItemTitleTextView_4),
            getString(R.string.tipsItemTitleTextView_5),
            getString(R.string.tipsItemTitleTextView_6),
            getString(R.string.tipsItemTitleTextView_7),
            getString(R.string.tipsItemTitleTextView_8),
            getString(R.string.tipsItemTitleTextView_9),
            getString(R.string.tipsItemTitleTextView_10)


        )

        tipsItemDescTextView = arrayOf(
            getString(R.string.tipsItemDescTextView_1),
            getString(R.string.tipsItemDescTextView_2),
            getString(R.string.tipsItemDescTextView_3),
            getString(R.string.tipsItemDescTextView_4),
            getString(R.string.tipsItemDescTextView_5),
            getString(R.string.tipsItemDescTextView_6),
            getString(R.string.tipsItemDescTextView_7),
            getString(R.string.tipsItemDescTextView_8),
            getString(R.string.tipsItemDescTextView_9),
            getString(R.string.tipsItemDescTextView_10)

        )
        
        tipsItemSourceTextView = arrayOf(
            getString(R.string.tipsItemSourceTextView_1),
            getString(R.string.tipsItemSourceTextView_2),
            getString(R.string.tipsItemSourceTextView_3),
            getString(R.string.tipsItemSourceTextView_4),
            getString(R.string.tipsItemSourceTextView_5),
            getString(R.string.tipsItemSourceTextView_6),
            getString(R.string.tipsItemSourceTextView_7),
            getString(R.string.tipsItemSourceTextView_8),
            getString(R.string.tipsItemSourceTextView_9),
            getString(R.string.tipsItemSourceTextView_10)

        )

    }

}