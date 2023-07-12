package com.example.gomommy

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var calendarGridView: GridView
    private lateinit var daysOfWeekGridView: GridView
    private var dueDate: String = ""
    private var timeStamp: String? = null
    private var currentDate: String = ""

    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    private val years = arrayOf("2023", "2024")

    private lateinit var adapter: CalendarGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")
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
                readDueDate()
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
                readDueDate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Read the due date from Firebase
        readDueDate()

        // generate timestamp from Firebase
        readTimeStamp()

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
        adapter = CalendarGridAdapter(requireContext(), days, dueDate)
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

    private fun readDueDate() {
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val dueDate = it.value
            displayDueDate(dueDate)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun displayDueDate(dueDate: Any?) {
        val dueDateString = dueDate.toString()
        val parts = dueDateString.split(" ")
        if (parts.size >= 3) {
            val day = parts[0]
            val month = parts[1]
            val year = parts[2]

            val selectedMonthPosition = monthSpinner.selectedItemPosition
            val selectedYearPosition = yearSpinner.selectedItemPosition

            if (selectedMonthPosition >= 0 && selectedMonthPosition < months.size &&
                selectedYearPosition >= 0 && selectedYearPosition < years.size
            ) {
                val selectedMonth = months[selectedMonthPosition]
                val selectedYear = years[selectedYearPosition]

                val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
                val formattedDueDate = dateFormat.format(dateFormat.parse("$day $month $year"))

                val estimatedDueDateTextView = view?.findViewById<TextView>(R.id.estDueDate)
                if (month == selectedMonth && year == selectedYear) {
                    adapter.updateDueDate(formattedDueDate)
                } else {
                    estimatedDueDateTextView?.text = formattedDueDate
                }
            }
        }
    }


    private fun readTimeStamp() {
        dbRef.child("userProfile").child("firstDayTimeStamp").get().addOnSuccessListener { dataSnapshot ->
            Log.i("firebase", "Got value ${dataSnapshot.value}")
            val timeStamp = dataSnapshot.value as? String
            if (!timeStamp.isNullOrBlank()) {
                timeStampChecker(true, timeStamp)
            } else {
                timeStampChecker(false, null)
            }
        }.addOnFailureListener { e ->
            Log.e("firebase", "Error getting data", e)
        }
    }

    private fun timeStampChecker(value: Boolean, timeStamp: String?) {
        if (value) {
            println("timestamp already exist: $timeStamp")
            this.timeStamp = timeStamp
        } else {
            println("timestamp added")
            saveTimeStamp()
        }
    }


    private fun saveTimeStamp() {
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        timeStamp = currentDate
        this.currentDate = currentDate

        val newKeyValuePair = HashMap<String, Any>()
        newKeyValuePair["firstDayTimeStamp"] = currentDate
        dbRef.child("userProfile").updateChildren(newKeyValuePair)
    }

    private inner class CalendarGridAdapter(
        context: Context,
        private val days: ArrayList<String>,
        private var dueDate: String
    ) : ArrayAdapter<String>(context, 0, days) {

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


            if (day.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
                val currentMonth = calendar.get(Calendar.MONTH).toString()
                val selectedMonth = months[monthSpinner.selectedItemPosition]
                val selectedYear = years[yearSpinner.selectedItemPosition]

                if (isDayPassed(selectedMonth, day.toInt(), selectedYear)) {
                    // Apply the passed date styling
                    viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background_previous_day)
                    viewHolder.dayTextView.setTextColor(Color.WHITE)
                    Log.d("CalendarFragment", "Marked as passed date: $day $selectedMonth $selectedYear")
                } else if (day == currentDay && selectedMonth == getCurrentMonth() && selectedYear == getCurrentYear()) {
                    viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background_current_day)
                    viewHolder.dayTextView.setTextColor(Color.BLACK)
                } else if (day == dueDate && dueDate.isNotEmpty()) {
                    viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background_due_date)
                    viewHolder.dayTextView.setTextColor(Color.WHITE)
                } else {
                    viewHolder.dayTextView.setBackgroundResource(R.drawable.circle_background)
                    viewHolder.dayTextView.setTextColor(Color.BLACK)
                }

                viewHolder.dayTextView.text = day
            } else {
                viewHolder.dayTextView.setBackgroundResource(0) // Remove any background drawable
                viewHolder.dayTextView.text = "" // Set empty text
            }

            viewHolder.dayTextView.text = day

            return view!!
        }

        private fun isDayPassed(month: String, day: Int, year: String): Boolean {
            if (timeStamp.isNullOrEmpty()) {
                // Handle the case when the timestamp is not available
                return false
            }

            val timeStampDateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())

            // Get the current date
            val currentDate = timeStampDateFormat.format(Date())

            // Compare the selected date with the current date
            val selectedDate = "$month $day $year"
            val startDate = timeStampDateFormat.parse(timeStamp)
            val endDate = timeStampDateFormat.parse(currentDate)
            val selectedDateTime = startDate?.time ?: 0
            val currentDateTime = endDate?.time ?: 0

            // Compare the selected date with the start date (timestamp)
            val selectedDateOnly = timeStampDateFormat.parse(selectedDate)
            val selectedDateOnlyTime = selectedDateOnly?.time ?: 0

            if (selectedDateOnlyTime in selectedDateTime..currentDateTime) {
                val formattedSelectedDate = timeStampDateFormat.format(selectedDateOnly)
                Log.i("Marked Date", formattedSelectedDate)
                return true
            }

            return false
        }


        fun updateDueDate(day: String) {
            dueDate = day
            notifyDataSetChanged()
        }

        private inner class ViewHolder {
            lateinit var dayTextView: TextView
        }
    }

    private class CustomSpinnerAdapter(
        context: Context,
        private val items: Array<String>
    ) :
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