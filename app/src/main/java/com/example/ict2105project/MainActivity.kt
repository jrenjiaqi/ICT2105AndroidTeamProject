package com.example.ict2105project

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ict2105project.databinding.ActivityMainBinding
import com.example.ict2105project.fragments.*
import com.example.ict2105project.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    //to id the fragments:
    //val fragment = requireActivity().supportFragmentManager.findFragmentByTag("home")
    //val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHolder)

    private lateinit var binding: ActivityMainBinding

    //setting up fragment manager
    private val fragmentManager: FragmentManager = supportFragmentManager
    //setting up fragments for our modules
    private lateinit var homeFrag: Home
    private lateinit var attendanceFrag: Attendance
    private lateinit var claimsFrag: Claims
    private lateinit var leavesFrag: Leaves
    private lateinit var profileFrag: Profile
    //setting up for bottomnavbar
    private lateinit var navBar: BottomNavigationView

    /**
     * This map determines how the animation should go when accessing the fragments
     * if it is on the left: slide right -> left
     * if it is on the right: slide left <- right
     */
    private val fragmentOrdinalMap = mapOf(
        Home::class.java.simpleName to 0,
        Claims::class.java.simpleName to 1,
        Profile::class.java.simpleName to 2,
        Leaves::class.java.simpleName to 3,
        Attendance::class.java.simpleName to 4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting up view binding
        navBar = binding.bottomNavigation

        /**
         * Setting up:
         * - Home (default page), Attendance, Claims, Leaves, Profile modules
         */
        homeFrag = Home()
        attendanceFrag = Attendance()
        claimsFrag = Claims()
        leavesFrag = Leaves()
        profileFrag = Profile()
        //setting home page
        fragmentManager.commit {
            this.setCustomAnimations(
                R.anim.slide_in_from_right,     // enter
                R.anim.fade_out,                // exit
                R.anim.fade_in,                 // popEnter
                R.anim.slide_out_to_right       // popExit
            )
            replace<Home>(binding.fragmentHolder.id)
            addToBackStack(homeFrag.javaClass.name)
        }

        navBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    replaceFragment(homeFrag)
                    true
                }
                R.id.menuClaims -> {
                    replaceFragment(claimsFrag)
                    true
                }
                R.id.menuProfile -> {
                    replaceFragment(profileFrag)
                    true
                }
                R.id.menuLeave -> {
                    replaceFragment(leavesFrag)
                    true
                }
                R.id.menuAttendance -> {
                    replaceFragment(attendanceFrag)
                    true
                }
                else -> false
            }
        }
    }

    /**
     * when the user presses back on the mainactivity
     *
     * it acts as a log out and will clear the shared preferences and
     * tear down the library button simple fix
     * then kills the activity
     */
    override fun onBackPressed() {
        super.onBackPressed()
            //this handles the scenario where there's no more stack behind to be used after popping the current one
            //Clearing shared preferences as a form of logging out
            val sp: SharedPreferences = this.getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.clear()
            editor.apply()

            //Simple workaround for the library button issue
            HomeViewModel.isErr = null
            HomeViewModel.isIn = null

            //therefore, this ends the activity and brings the user back to the login page
            this.finish()
    }

    /**
     * https://stackoverflow.com/questions/18305945/how-to-resume-existing-fragment-from-backstack
     * Function below allows only 1 instance of the fragment ever in the stack (Singleton Pattern)
     * During the transactions, there are also transitional animations that are implemented here
     */
    private fun replaceFragment(frag: Fragment){
        val fragmentName = frag.javaClass.name
        val manager = supportFragmentManager
        val isPopped: Boolean = manager.popBackStackImmediate(fragmentName, 0) //checks if fragment alr exists

        if(!isPopped && manager.findFragmentByTag(fragmentName) == null){ //fragment not in backstack, so create it
            manager.beginTransaction().apply{
                //adding the animation
                manager.findFragmentById(R.id.fragmentHolder)?.let {
                    if(fragmentOrdinalMap[it.javaClass.simpleName]!! >
                        fragmentOrdinalMap[frag.javaClass.simpleName]!!) {
                        this.setCustomAnimations(
                            R.anim.slide_in_from_left, // enter
                            R.anim.fade_out, // exit
                            R.anim.fade_in,  // popEnter
                            R.anim.slide_out_to_left // popExit
                        )
                    }else {
                        this.setCustomAnimations(
                            R.anim.slide_in_from_right, // enter
                            R.anim.fade_out, // exit
                            R.anim.fade_in,  // popEnter
                            R.anim.slide_out_to_right // popExit
                        )
                    }
                }
                replace(R.id.fragmentHolder, frag, fragmentName)
                addToBackStack(fragmentName)
                commit()
            }
        }
    }

}