package com.example.financetrackerapplication.features.transaction

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityTransactionBinding
import com.example.financetrackerapplication.databinding.CustomDatePickerBinding
import com.example.financetrackerapplication.databinding.CustomTimePickerBinding
import com.example.financetrackerapplication.domain.model.TransOptions
import com.example.financetrackerapplication.features.transaction.previewcamera.AddPreviewActivity
import com.example.financetrackerapplication.utils.DialogUtils
import com.example.financetrackerapplication.utils.Extention.clearAllEditTexts
import com.example.financetrackerapplication.utils.Extention.focusAndHideKeyboard
import com.example.financetrackerapplication.utils.Extention.hideKeyboard
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.Extention.parseMoneyToLong
import com.example.financetrackerapplication.utils.Extention.showKeyboard
import com.example.financetrackerapplication.utils.TimeUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private val viewModel: TransactionViewModel by viewModels()
    private val listPhotoDescription: MutableList<Uri> = mutableListOf()

    // launcher
    private val launcherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dataUri = result.data?.getStringExtra(AddPreviewActivity.ARG_URI)?.toUri()
                dataUri?.let {
                    addPhotoDescription(dataUri)
                    Log.d(TAG, "list photo desc add = $listPhotoDescription: ")
                }
            }
        }

    // input selected
    private var asetSelectedId: Long = -1
    private var categorySelectedId: Long = -1

    private var transactionId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.transToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        setupRecyclerView()
        setupListener()
        observer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    // inisialize ui / setup ui pertama ketika baru tampil
    private fun init() {
        // inisialize id data
        transactionId = intent.getSerializableExtra(TRANS_ID) as? Long
        binding.addTotalLayout.isExpandedHintEnabled = false

        binding.apply {
            /* pastikan keyboard tidak muncul */
            addEtTotal.apply {
                showSoftInputOnFocus = false
                inputType = InputType.TYPE_NULL // pastikan keyboard tidak muncul
            }
            addEtKategori.apply {
                showSoftInputOnFocus = false
                inputType = InputType.TYPE_NULL // pastikan keyboard tidak muncul
            }
            addEtAset.apply {
                inputType = InputType.TYPE_NULL // pastikan keyboard tidak muncul
                showSoftInputOnFocus = false
            }
        }

        // setup behavior back system
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val keyNum = binding.keyboardNum.root
                val keyList = binding.keyboardListOptions.root

                if (keyNum.isVisible || keyList.isVisible) {
                    keyNum.isVisible = false
                    keyList.isVisible = false
                    // non aktifkan semua focus edt
                    binding.root.requestFocus()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }

        })

        updateDate()
        updateTime()

        // ketika update, kalo ada focus berarti update
        // pantau
        binding.root.viewTreeObserver.addOnGlobalFocusChangeListener { view, newFocus ->
            if (newFocus != null) transactionId?.let {
                binding.addBtnUpdate.isVisible = true
            }
        }
        transactionId?.let {
            binding.btnAddLayout.isVisible = false
        }

        // from tab header home
        val timeFromHeader = intent.getSerializableExtra(TRANS_TIME_MILLIS) as? Long
        timeFromHeader?.let { dateTimeMillis->
            updateDate(dateTimeMillis)
            updateTime(dateTimeMillis)
        }
    }

    private fun setupListener() {
        binding.apply {
            // total
            addEtTotal.setOnFocusChangeListener { _, hasFocus ->
                keyboardNum.root.apply {
                    isVisible = hasFocus
                }
            }

            // category
            addEtKategori.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    keyboardListOptions.root.isVisible = hasFocus

                    // show keyboard list on focus
                    if (hasFocus) {
                        showKeyboardOptionsList {
                            val adapter = OptionsAdapter<CategoryEntity> {
                                categorySelectedId = it.id
                                addEtKategori.setText(it.name)

                                addEtAset.requestFocus()

                            }
                            lifecycleScope.launch {
                                val data = viewModel.getAllCategory()
                                adapter.submitList(data)
                            }
                            adapter
                        }
                    }
                }
            }

            // aset
            addEtAset.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    keyboardListOptions.root.isVisible = hasFocus

                    // show keyboard list on focus
                    if (hasFocus) {
                        showKeyboardOptionsList {
                            val adapter = OptionsAdapter<AsetEntity> {
                                asetSelectedId = it.id
                                addEtAset.setText(it.name)

                                addEtCatatan.apply {
                                    requestFocus()
                                }
                            }
                            lifecycleScope.launch {
                                val data = viewModel.getAllAset()
                                adapter.submitList(data)
                            }
                            adapter
                        }
                    }
                }
            }

            // hide keyboard on non focus
            addEtCatatan.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) addEtCatatan.hideKeyboard(this@TransactionActivity)
                else addEtCatatan.showKeyboard(this@TransactionActivity)
            }
            addEtDeskripsi.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) addEtDeskripsi.hideKeyboard(this@TransactionActivity)
                else addEtDeskripsi.showKeyboard(this@TransactionActivity)
            }

//            buttonGroupTypeTransaction.setOnTouchListener { view, motionEvent ->
//                saveToUpdateButton()
//                true
//            }
            this.addBtnCamera.setOnClickListener {
                val intent = Intent(this@TransactionActivity, AddPreviewActivity::class.java)
                launcherCamera.launch(intent)
                saveToUpdateButton()
            }
            addEtTime.setOnClickListener { showTimePicker(addEtTime.text.toString()) }
            addEtDate.setOnClickListener { showDatePicker(addEtDate.text.toString()) }
            addBtnSave.setOnClickListener {
                saveTransaction(true)
            }
            addBtnNextLanjut.setOnClickListener {
                saveTransaction(false)
            }
            addBtnUpdate.setOnClickListener {
                updateTransaction()
            }

            // setup keyboard
            keyboardClickListener(
                {
                    // ketiak btn selesai di triger
                    addEtKategori.requestFocus()
                },
                addEtTotal,
                keyboardNum.keyNum1,
                keyboardNum.keyNum2,
                keyboardNum.keyNum3,
                keyboardNum.keyNum4,
                keyboardNum.keyNum5,
                keyboardNum.keyNum6,
                keyboardNum.keyNum7,
                keyboardNum.keyNum8,
                keyboardNum.keyNum9,
                keyboardNum.keyNumNol,
                keyboardNum.keyNumKoma,
                keyboardNum.keyNumMinus,
                keyboardNum.keyNumDelete,
                keyboardNum.keyNumCalculator,
                keyboardNum.keyNumSelesai
            )

            // tampilakan edtieks total ketika keyboard muncul
            keyboardNum.root.viewTreeObserver.addOnGlobalLayoutListener {
                transScroll.post {
                    transScroll.smoothScrollTo(0, addEtTotal.bottom)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        // aset
    }

    private fun saveToUpdateButton() {
        transactionId?.let {
            binding.addBtnUpdate.isVisible = true
            binding.btnAddLayout.isVisible = false
        }
    }


    private fun saveTransaction(isFinish: Boolean) {
        binding.apply {
            // perintah untuk lengkapi
            Log.d(TAG, "updateTransaction: list photo = $listPhotoDescription")
            when {
                addEtKategori.text.isNullOrBlank() -> {
                    addEtKategori.requestFocus()
                    DialogUtils.showToast(
                        this@TransactionActivity,
                        resources.getString(
                            R.string.warning_message_trans,
                            addKategoriLayout.hint.toString()
                        )
                    )
                    return@apply
                }

                addEtAset.text.isNullOrBlank() -> {
                    // perintah untuk lengkapi
                    addEtAset.requestFocus()
                    DialogUtils.showToast(
                        this@TransactionActivity,
                        resources.getString(
                            R.string.warning_message_trans,
                            addAsetLayout.hint.toString()
                        )
                    )
                    return@apply
                }
            }


            viewModel.insertTransaction(
                amount = addEtTotal.text.toString().parseMoneyToLong(),
                type = if (buttonGroupTypeTransaction.position == 0) TransactionEntity.TYPE_INCOME
                else TransactionEntity.TYPE_EXPANSE,
                dateTimeMillis = TimeUtils.combineDateTimeMillis(
                    addEtDate.text.toString(),
                    addEtTime.text.toString()
                ),
                description = addEtDeskripsi.text.toString(),
                catatan = addEtCatatan.text.toString(),
                accountId = asetSelectedId,
                photoDescription = listPhotoDescription,
                categoryId = categorySelectedId
            )

            if (isFinish) {
                finish()
                return@apply
            }

            root.clearAllEditTexts()// bersihkan edteks
            addGridPhotoTrans.removeAllViews()
            addEtTotal.requestFocus()

            updateDate()
            updateTime()
        }
    }

    private fun updateTransaction() {
        binding.apply {
            Log.d(TAG, "updateTransaction: list photo = $listPhotoDescription")
            // perintah untuk lengkapi
            transactionId?.let { id ->
                viewModel.updateTransaction(
                    id = id,
                    amount = addEtTotal.text.toString().parseMoneyToLong(),
                    type = if (buttonGroupTypeTransaction.position == 0) TransactionEntity.TYPE_INCOME
                    else TransactionEntity.TYPE_EXPANSE,
                    dateTimeMillis = TimeUtils.combineDateTimeMillis(
                        addEtDate.text.toString(),
                        addEtTime.text.toString()
                    ),
                    description = addEtDeskripsi.text.toString(),
                    catatan = addEtCatatan.text.toString(),
                    accountId = asetSelectedId,
                    photoDescription = listPhotoDescription,
                    categoryId = categorySelectedId
                ).also {
                    finish()
                }
            }
        }
    }

    private fun observer() {
        viewModel.apply {
            transactionId?.let { id ->
                binding.apply {
                    listOf(
                        addDateLayout,
                        addTimeLayout,
                        addTotalLayout,
                        addDescLayout,
                        addCatatanLayout,
                        addKategoriLayout,
                        addAsetLayout
                    ).forEach { it.isExpandedHintEnabled = false }
                }

                lifecycleScope.launch {
                    getTransaction(id).collect { data ->
                        data?.let {
                            val transaction = data.transaction
                            val category = data.category
                            val asset = data.account

                            // set fk
                            asetSelectedId = data.account.id
                            categorySelectedId = data.category.id
                            Log.d(TAG, "observer: get transaction is observe")

                            binding.apply {
                                buttonGroupTypeTransaction.setPosition(
                                    if (transaction.type == TransactionEntity.TYPE_INCOME) 0 else 1,
                                    false
                                )

                                addEtTotal.setText(transaction.amount.parseLongToMoney())
                                addEtCatatan.setText(transaction.catatan.toString())
                                addEtDeskripsi.setText(transaction.description.toString())

                                addEtKategori.setText(category.name)
                                addEtAset.setText(asset.name)

                                updateDate(transaction.dateTimeMillis)
                                updateTime(transaction.dateTimeMillis)

                                val uriIsNotEmpty = transaction.photoDescription.isNotEmpty() &&
                                        transaction.photoDescription.any { it.toString().isNotBlank() }
                                if (uriIsNotEmpty) {
                                    Log.d(TAG, "observer: photo desc = ${transaction.photoDescription}")
                                    addGridPhotoTrans.removeAllViews() // bersihkan biar nggk duplikat
                                    transaction.photoDescription.forEach { uri ->
                                        addPhotoDescription(uri)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // keyboard custom option list
    private fun <T : TransOptions> showKeyboardOptionsList(optionsAdapter: () -> OptionsAdapter<T>) {
        binding.keyboardListOptions.keyRvListOptions.apply {
            adapter = optionsAdapter.invoke()
            layoutManager = GridLayoutManager(this@TransactionActivity, 3)
        }
    }

    // set date
    private fun showDatePicker(textDate: String) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parseDate = dateFormat.parse(textDate)

        // Create a Calendar instance to get the current date
        val calendar = Calendar.getInstance().apply {
            time = parseDate!!
        }

        // menentukan date awal
        val viewDate = CustomDatePickerBinding.inflate(layoutInflater)
        viewDate.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )

        // dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(viewDate.root)
            .setPositiveButton("OK") { _, _ ->
                val year = viewDate.datePicker.year
                val month = viewDate.datePicker.month
                val day = viewDate.datePicker.dayOfMonth

                val selected = calendar.apply {
                    set(year, month, day)
                }
                updateDate(selected.timeInMillis)
            }
            .setNegativeButton("Batal", null)
            .create()
        dialog.show()
    }

    // update tanggal
    private fun updateDate(newDate: Long? = null) {
        val formatter = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(formatter, Locale.getDefault())
        val date = newDate?.let {
            simpleDateFormat.format(newDate)
        } ?: simpleDateFormat.format(Calendar.getInstance().timeInMillis)
        binding.addEtDate.setText(date)
        if (transactionId == null) {
            binding.addEtTotal.focusAndHideKeyboard()
        }

    }

    private fun showTimePicker(timeString: String) {
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parseTime = timeFormatter.parse(timeString) // konversi

        val timeBinding = CustomTimePickerBinding.inflate(layoutInflater)
        val c = Calendar.getInstance().apply {
            time = parseTime!! // set time di calender
        }

        // set picker from calendar
        timeBinding.timePicker.apply {
            setIs24HourView(DateFormat.is24HourFormat(this@TransactionActivity))
            hour = c.get(Calendar.HOUR_OF_DAY)
            minute = c.get(Calendar.MINUTE)
        }

        MaterialAlertDialogBuilder(this)
            .setView(timeBinding.root)
            .setPositiveButton("OK") { _, _ ->
                val hour = timeBinding.timePicker.hour
                val minute = timeBinding.timePicker.minute

                // set calendar dari picker
                val selected = c.apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                updateTime(selected.timeInMillis)
            }.setNegativeButton("Batal", null)
            .show()
    }

    private fun updateTime(newTime: Long? = null) {
        /**
         * hh = format 12-jam
         * HH = format 24-jam
         */
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = newTime?.let {
            formatter.format(it)
        } ?: formatter.format(Calendar.getInstance().timeInMillis)
        binding.addEtTime.setText(time)
        // non focus ketika edit
        if (transactionId == null) {
            binding.addEtTotal.focusAndHideKeyboard()
        }
    }

    private fun keyboardClickListener(
        nextFocus: () -> Unit,
        editText: TextInputEditText,
        vararg button: AppCompatButton
    ) {
        button.forEach { btn ->
            btn.setOnClickListener {
                when (btn.id) {
                    R.id.key_num_delete -> editText.text?.apply {
                        if (this.isNotBlank()) delete(length - 1, length) // hapus karakter terakhir
                    }

                    R.id.key_num_minus -> editText.text?.apply {
                        val minus = btn.text.toString()
                        if (!this.toString().contains(minus)) insert(0, btn.text.toString())
                        else delete(0, 1)
                    }

                    R.id.key_num_koma -> editText.text?.apply {
                        // di editteks apa ada koma
                        val comma = btn.text.toString()
                        if (!this.toString().contains(comma)) insert(this.length, comma)
                    }

                    R.id.key_num_selesai -> {
                        nextFocus.invoke()
                    }

                    else -> editText.append(btn.text.toString())
                }
                Log.d(TAG, "keyboardClickListener: input teks = ${btn.text}")
            }

        }
    }

    private fun addPhotoDescription(imgUri: Uri) {

        val cardView = MaterialCardView(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 300
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            radius = 32f
            cardElevation = 8f
        }

        val container = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        // ImageView foto
        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(imgUri)

            // add to list
            listPhotoDescription.add(imgUri)
            Log.d(TAG, "addPhotoDescription: listPhoto descripsiotn = $listPhotoDescription")
        }

        // tombol delete
        val deleteBtn = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(60, 60).apply {
                gravity = Gravity.END or Gravity.TOP
                setMargins(4, 4, 4, 4)
                setImageResource(R.drawable.ic_remove_circle_outline)

                setOnClickListener {
                    saveToUpdateButton()
                    binding.apply {
                        addGridPhotoTrans.removeView(cardView)
                        listPhotoDescription.remove(imgUri)
                        // tampilkan btn camera
                        addBtnCamera.visibility = View.VISIBLE

                        Log.d(TAG, "list photo desc remove = $listPhotoDescription: ")
                    }
                }
            }
        }

        // pasang view
        container.apply {
            addView(imageView)
            addView(deleteBtn)
        }
        cardView.addView(container)

        // tambah item grid
        binding.addGridPhotoTrans.apply {
            addView(cardView)
            // sembunyikan kamera
            if (this.childCount == this.columnCount) {
                binding.addBtnCamera.visibility = View.GONE
            }
        }
    }

    companion object {
        private val TAG = TransactionActivity::class.java.simpleName
        val TRANS_ID = "trans_id"
        val TRANS_TIME_MILLIS = "trans_time_millis"
    }
}