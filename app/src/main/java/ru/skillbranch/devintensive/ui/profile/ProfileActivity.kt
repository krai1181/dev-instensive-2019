package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.CircleImageView
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


const val IS_EDIT_MODE = "IS_EDIT_MODE"

class ProfileActivity : AppCompatActivity() {


    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    lateinit var avatarView:CircleImageView

    lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()


    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
            "nickname" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        avatarView = iv_avatar
        avatarView.setBorderColor("#FFFFFF")
        avatarView.setBorderWidth(2)


        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (wr_repository.isErrorEnabled){
                wr_repository.error = null
                et_repository.text.clear()
            }
            if (isEditMode) saveProfileData()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        viewModel.profileData.observe(this, Observer { updateUI(it) })
        viewModel.appTheme.observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }

        updateAvatar()
    }

    private fun updateAvatar() {
        val firstName = viewFields.getValue("firstName").text.toString()
        val lastName = viewFields.getValue("lastName").text.toString()
        if (firstName.isNotEmpty().or(lastName.isNotEmpty())) {
            avatarView.setInitials(Utils.toInitials(firstName, lastName)!!)
        }
    }

    private fun checkRepositoryValidation() {
        et_repository.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isStartWith = s?.startsWith("https://github.com/")?.or(s.startsWith("www.github.com/"))?.
                    or(s.startsWith("https://www.github.com/"))?.or(s.startsWith("github.com/"))  ?: false

                if(isStartWith.or(s.isNullOrEmpty())) {
                    wr_repository.isErrorEnabled = false
                    wr_repository.error = null
                }else{
                    wr_repository.isErrorEnabled = true
                    wr_repository.error = "Невалидный адрес репозитория"
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val exceptions = listOf("enterprise","features","topics","collections","trending",
                    "events","marketplace","pricing","nonprofit","customer-stories","security","log","join","/")

                val isEndWith = exceptions.any { s?.endsWith(it) ?: false}
                val isContains = s?.contains("/tree") ?: false
                Log.d("ProfileActivity","afterTextChanged $isEndWith")
                if (isEndWith.or(isContains)){
                    wr_repository.isErrorEnabled = true
                    wr_repository.error = "Невалидный адрес репозитория"
                }

            }

        })
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0

        }
        iv_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        btn_edit.apply {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme), PorterDuff.Mode.SRC_IN)
            } else {
                null
            }

            val icon = if (isEdit)
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            else
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)


            updateAvatar()

            background.colorFilter = filter
            setImageDrawable(icon)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        if (isEdit){
            checkRepositoryValidation()
        }

    }

    private fun saveProfileData() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)

    }

}
