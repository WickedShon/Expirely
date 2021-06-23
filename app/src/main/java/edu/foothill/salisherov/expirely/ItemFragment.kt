package edu.foothill.salisherov.expirely

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.sql.Date
import java.util.*


private const val ARG_ITEM_ID = "item_id"
private const val REQUEST_DATE = 0

class ItemFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var item: Item
    private lateinit var nameField: EditText
    private lateinit var datePicker: Button
    private lateinit var notifiedCheckBox: CheckBox
    private lateinit var notificationTime: EditText
    private val itemDetailViewModel: ItemDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ItemDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = Item()
        val itemId: UUID = arguments?.getSerializable(ARG_ITEM_ID) as UUID
        itemDetailViewModel.loadItem(itemId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item, container, false)
        nameField = view.findViewById(R.id.item_name) as EditText
        datePicker = view.findViewById(R.id.item_datePicker) as Button
        notifiedCheckBox = view.findViewById(R.id.item_notified) as CheckBox
        notificationTime = view.findViewById(R.id.item_notificationTime) as EditText
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemDetailViewModel.itemLiveData.observe(
            viewLifecycleOwner,
            Observer { item ->
                item?.let {
                    this.item = item
                    updateUI()
                }
            })
    }


    private fun updateUI() {

        var stringDate = this.item.date.toString()
        var dateCut = stringDate.slice(0..10) + stringDate.slice(24..27)

        val calendar = Calendar.getInstance()
        calendar.time = item.date
        nameField.setText(item.name)
//        datePicker.init(
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH),
//            DatePicker.OnDateChangedListener { datePicker, year, month, day -> })
        datePicker.setText(dateCut)
        notifiedCheckBox.isChecked = item.notified
        notificationTime.setText(item.notificationTime.toString())

    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (sequence.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter the item name!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    item.name = sequence.toString()
                }
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        nameField.addTextChangedListener(titleWatcher)


        val timeWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (sequence.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        "Please enter the notification time!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    item.notificationTime = Integer.parseInt(sequence.toString())
                }
            }


            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        notificationTime.addTextChangedListener(timeWatcher)

        notificationTime.isEnabled = notifiedCheckBox.isChecked
        notifiedCheckBox.apply {
            setOnCheckedChangeListener { _, ischecked ->
                item.notified = ischecked
                notificationTime.isEnabled = notifiedCheckBox.isChecked
            }
        }

        datePicker.setOnClickListener {
            DatePickerFragment.newInstance(item.date).apply {
                setTargetFragment(this@ItemFragment, REQUEST_DATE)
                show(this@ItemFragment.requireFragmentManager(), "DialogDate")

            }
        }
//        val dateListener = DatePickerDialog.OnDateSetListener {
//                _: DatePicker, year: Int, month: Int, day: Int ->
//
//            val resultDate : Date = GregorianCalendar(year, month, day).time
//            item.date = resultDate
//        }
    }

    override fun onStop() {
        super.onStop()
        itemDetailViewModel.saveItem(item)
    }

    companion object {
        fun newInstance(itemId: UUID): ItemFragment {
            val args = Bundle().apply {
                putSerializable(
                    ARG_ITEM_ID,
                    itemId
                )
            }
            return ItemFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(date: java.util.Date) {
        item.date = date
        updateUI()
    }
}