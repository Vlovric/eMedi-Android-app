package hr.foi.rampu.emedi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rampu.emedi.adapters.MainPagerAdapter
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.fragments.AppointmentsFragment
import hr.foi.rampu.emedi.fragments.DoctorsFragment
import hr.foi.rampu.emedi.fragments.ProfileFragment
import hr.foi.rampu.emedi.fragments.SettingsFragment
import hr.foi.rampu.emedi.helpers.MockDataUser
import hr.foi.rampu.emedi.helpers.UserSession

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setTabLayoutAndViewpager()
        if(UserSession.loggedIn == false){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        AppDatabase.buildInstance(applicationContext)
        MockDataUser.loadUsers()
        MockDataDoctor.loadDoctors()
    }

    private fun setTabLayoutAndViewpager() {
        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        mainPagerAdapter.addFragment(MainPagerAdapter.FragmentItem(R.string.doctors, R.drawable.ic_baseline_health_and_safety_24, DoctorsFragment::class))
        mainPagerAdapter.addFragment(MainPagerAdapter.FragmentItem(R.string.appointments, R.drawable.ic_baseline_assignment_24, AppointmentsFragment::class))
        mainPagerAdapter.addFragment(MainPagerAdapter.FragmentItem(R.string.profile, R.drawable.ic_baseline_person_24, ProfileFragment::class))
        mainPagerAdapter.addFragment(MainPagerAdapter.FragmentItem(R.string.settings, R.drawable.ic_baseline_settings_24, SettingsFragment::class))

        tabLayout = findViewById(R.id.tl_navigation)
        viewPager2 = findViewById(R.id.vp_fragments)

        viewPager2.adapter = mainPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.customView = createTabView(tabLayout.context, mainPagerAdapter.fragmentItems[position].titleRes, mainPagerAdapter.fragmentItems[position].iconRes)
        }.attach()
    }

    private fun createTabView(context: Context, titleRes: Int, iconRes: Int): View {
        val tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab_layout, null)
        val tabIcon = tabView.findViewById<ImageView>(R.id.tabIcon)
        val tabText = tabView.findViewById<TextView>(R.id.tabText)

        tabIcon.setImageResource(iconRes)
        tabText.setText(titleRes)

        if (titleRes == R.string.appointments) {
            tabText.textSize = 11f;
        }

        return tabView
    }
}