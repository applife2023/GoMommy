package com.example.gomommy

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

class CalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        // Set the selected date change listener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Handle the selected date change here
            // You can access the selected date using year, month, and dayOfMonth parameters

            // Example: Convert the selected date to a readable format
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val selectedDate = calendar.time
            val readableDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)

            // Example: Show a toast with the selected date
            Toast.makeText(requireContext(), "Selected date: $readableDate", Toast.LENGTH_SHORT).show()
        }
    }
}
