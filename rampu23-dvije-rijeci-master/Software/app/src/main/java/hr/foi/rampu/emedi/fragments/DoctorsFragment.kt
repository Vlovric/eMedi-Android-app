package hr.foi.rampu.emedi.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rampu.emedi.DoctorInformationActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.adapters.DoctorsAdapter
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.entities.Review
import hr.foi.rampu.emedi.helpers.MockDataCity
import hr.foi.rampu.emedi.helpers.MockDataSpecialization
import hr.foi.rampu.emedi.helpers.TextSizeUtility

class DoctorsFragment : Fragment() {
//    private val mockDoctors = MockDataDoctor.getDemoData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchTextBox : EditText
    private lateinit var errorMessage : TextView
    private lateinit var citySpinner : Spinner
    private lateinit var specializationSpinner : Spinner
    private lateinit var reviewSpinner : Spinner
    private lateinit var filterButton : Button
    private lateinit var clearButton : Button
    private lateinit var filteredList : List<Doctor>
    private val doctorsDAO = AppDatabase.getInstance().getDoctorsDao()
    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        citySpinner = view.findViewById(R.id.spn_city_filter)
        specializationSpinner = view.findViewById(R.id.spn_specialisation_filter)
        reviewSpinner = view.findViewById(R.id.spn_review_filter)
        filterButton = view.findViewById(R.id.btn_filter)
        clearButton = view.findViewById(R.id.btn_clear_filter)

        errorMessage = view.findViewById(R.id.tv_error_message)
        recyclerView = view.findViewById(R.id.rv_doctors)
        searchTextBox = view.findViewById(R.id.tv_search_doctor)
        listAllDoctors()

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        searchTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                getDoctorsByName(searchText)
            }
        })
        val citySpinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            MockDataCity.cityList
        )
        val specializationSpinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            MockDataSpecialization.specializationList
        )
        val reviewSpinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayOf<String>("Select item", "5", "4", "3", "2", "1")
        )

        citySpinner.adapter = citySpinnerAdapter
        specializationSpinner.adapter = specializationSpinnerAdapter
        reviewSpinner.adapter = reviewSpinnerAdapter

        filterButton.setOnClickListener{
            val city = citySpinner.selectedItem.toString()
            val specialization = specializationSpinner.selectedItem.toString()
            val review = reviewSpinner.selectedItem.toString()
            val doctorsList = getDoctorsByFilter(city, specialization, review)
            if (doctorsList.isNotEmpty()) {
                recyclerView.adapter = DoctorsAdapter(doctorsList) { doctor ->
                    val intent = Intent(requireContext(), DoctorInformationActivity::class.java)
                    intent.putExtra("doctor", doctor)
                    startActivity(intent)
                }
                errorMessage.visibility = View.GONE
            } else {
                recyclerView.adapter = DoctorsAdapter(emptyList()) {
                }
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = "Not a single doctor was found that meets the conditions!"
            }

        }
        clearButton.setOnClickListener {
            listAllDoctors()
            citySpinner.setSelection(0)
            specializationSpinner.setSelection(0)
            reviewSpinner.setSelection(0)
        }
        changeTextSize(view)

    }
    private fun getDoctorsByName(name: String) {
        val doctorsList = if (name.isNotBlank()) {
            doctorsDAO.getDoctorByName("$name%")
        } else {
            doctorsDAO.getAllDoctors()
        }
        if (doctorsList.isNotEmpty()) {
            recyclerView.adapter = DoctorsAdapter(doctorsList) { doctor ->
                val intent = Intent(requireContext(), DoctorInformationActivity::class.java)
                intent.putExtra("doctor", doctor)
                startActivity(intent)
            }
            errorMessage.visibility = View.GONE
        } else {
            recyclerView.adapter = DoctorsAdapter(emptyList()) {
            }
            errorMessage.visibility = View.VISIBLE
            errorMessage.text = "No doctor with that name was found!"
        }
    }
    private fun getDoctorsByFilter(city: String?, specialization: String?, review : String?) : List<Doctor>{
        var cityFilter = city
        var specializationFilter = specialization
        var reviewFilter = review

        if (city == "Select item") {
            cityFilter = null
        }
        if (specialization == "Select item") {
            specializationFilter = null
        }
        if (review == "Select item") {
            reviewFilter = null
        }
        val doctorsList = doctorsDAO.getAllDoctors()
        filteredList = doctorsList.filter {
            val doctorsRating = Review.getAverageRatingForDoctor(it)

            it.address.contains(cityFilter ?: "") &&
            it.specialization.contains(specializationFilter ?: "") &&
            (if (reviewFilter == null) true else (doctorsRating >= reviewFilter.toFloat() - 0.5f && doctorsRating <= reviewFilter.toFloat() + 0.5f))
        }
        return filteredList
    }
    fun listAllDoctors(){
        recyclerView.adapter = DoctorsAdapter(MockDataDoctor.getDemoData()){
            val intent = Intent(requireContext(), DoctorInformationActivity::class.java)
            val whichDoctor = doctorsDAO.getDoctor(it.id)
            intent.putExtra("doctor", whichDoctor)
            startActivity(intent)
        }
    }
    override fun onResume() {
        listAllDoctors()
        super.onResume()
    }

    private fun changeTextSize(view : View) {

        sharedPreferences = requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(requireContext())
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllTextViews(
            view.findViewById(R.id.tv_city),
            view.findViewById(R.id.tv_specialization),
            view.findViewById(R.id.tv_rate),
            )
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_city), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_specialization), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_rate), position)


        textSizeUtility.registerAllButtons(
            view.findViewById(R.id.btn_filter),
            view.findViewById(R.id.btn_clear_filter)
        )
        textSizeUtility.registerButtonStyle(requireContext(), view.findViewById(R.id.btn_filter), position)
        textSizeUtility.registerButtonStyle(requireContext(), view.findViewById(R.id.btn_clear_filter), position)

    }
}