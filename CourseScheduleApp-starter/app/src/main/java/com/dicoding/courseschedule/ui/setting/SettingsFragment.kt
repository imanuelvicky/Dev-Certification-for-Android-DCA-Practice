package com.dicoding.courseschedule.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    @SuppressLint("NewApi")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val darkModePref = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        darkModePref?.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue.toString()) {
                getString(R.string.pref_dark_auto) -> updateTheme(NightMode.AUTO.value)
                getString(R.string.pref_dark_on) -> updateTheme(NightMode.ON.value)
                getString(R.string.pref_dark_off) -> updateTheme(NightMode.OFF.value)
            }
            true
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val notificationPref = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        notificationPref?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue == true) {
                DailyReminder().setDailyReminder(requireContext())
                Toast.makeText(requireContext(), "Reminder: On", Toast.LENGTH_SHORT).show()
            } else {
                DailyReminder().cancelAlarm(requireContext())
                Toast.makeText(requireContext(), "Reminder: Off", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}