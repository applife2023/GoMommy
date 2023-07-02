package com.example.gomommy

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var calendarGridView: GridView
    private lateinit var daysOfWeekGridView: GridView

    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    private val years = arrayOf("2023", "2024")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        monthSpinner = view.findViewById(R.id.monthSpinner)
        yearSpinner = view.findViewById(R.id.yearSpinner)
        calendarGridView = view.findViewById(R.id.calendarGridView)
        daysOfWeekGridView = view.findViewById(R.id.daysOfWeekGridView)

        // Create custom spinner adapter for months
        val customMonthSpinnerAdapter = CustomSpinnerAdapter(requireContext(), months)
        monthSpinner.adapter = customMonthSpinnerAdapter

        // Create custom spinner adapter for years
        val customYearSpinnerAdapter = CustomSpinnerAdapter(requireContext(), years)
        yearSpinner.adapter = customYearSpinnerAdapter

        // Set selection listeners for the Spinners
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                val selectedYear = years[yearSpinner.selectedItemPosition]
                updateCalendarGrid(selectedMonth, selectedYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = months[monthSpinner.selectedItemPosition]
                val selectedYear = years[position]
                updateCalendarGrid(selectedMonth, selectedYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Initialize the calendar grid with the current month and year
        val currentMonth = getCurrentMonth()
        val currentYear = getCurrentYear()
        monthSpinner.setSelection(months.indexOf(currentMonth))
        yearSpinner.setSelection(years.indexOf(currentYear))

        return view
    }

    private fun updateCalendarGrid(month: String, year: String) {
        val daysOfWeek = arrayOf("S", "M", "T", "W", "T", "F", "S")
        val daysAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, daysOfWeek)
        daysOfWeekGridView.adapter = daysAdapter

        val days = getDaysForMonthYear(month, year)
        val adapter = CalendarGridAdapter(requireContext(), days)
        calendarGridView.adapter = adapter
    }

    private fun getDaysForMonthYear(month: String, year: String): ArrayList<String> {
        val days = ArrayList<String>()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, months.indexOf(month))
        calendar.set(Calendar.YEAR, year.toInt())
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val startingDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        for (i in 1 until startingDayOfWeek) {
            days.add("")
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..daysInMonth) {
            days.add(i.toString())
        }

        return days
    }

    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        return months[month]
    }

    private fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        return year.toString()
    }

    private inner class CalendarGridAdapter(context: Context, private val days: ArrayList<String>) :
        ArrayAdapter<String>(context, 0, days) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val viewHolder: ViewHolder

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_calendar, parent, false)
                viewHolder = ViewHolder()
                viewHolder.dayTextView = view.findViewById(R.id.dayTextView)
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }

            val day = days[position]

            // Set different background color for the current day
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val currentMonth = calendar.get(Calendar.MONTH).toString()
            val selectedMonth = months[monthSpinner.selectedItemPosition]
            val selectedYear = years[yearSpinner.selectedItemPosition]

            if (day.isNotEmpty() && day.toInt() == currentDay.toInt() && selectedMonth == getCurrentMonth() && selectedYear == getCurrentYear()) {
                viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background_current_day)
                viewHolder.dayTextView.setTextColor(Color.WHITE)
            } else {
                viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background)
                viewHolder.dayTextView.setTextColor(Color.BLACK)
            }

            viewHolder.dayTextView.text = day

            return view!!
        }

        private inner class ViewHolder {
            lateinit var dayTextView: TextView
        }
    }
    private class CustomSpinnerAdapter(context: Context, private val items: Array<String>) :
        ArrayAdapter<String>(context, R.layout.custom_spinner_item, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, convertView, parent)
        }

        private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.custom_spinner_item, parent, false)

            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = items[position]

            return view
        }
    }
}
private class CustomSpinnerAdapter(context: Context, private val items: Array<String>) :
    ArrayAdapter<String>(context, R.layout.custom_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]

        return view
    }
}