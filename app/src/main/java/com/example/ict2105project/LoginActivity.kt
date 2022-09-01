package com.example.ict2105project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.ict2105project.databinding.ActivityLoginBinding
import com.example.ict2105project.utilities.MD5Hasher
import com.example.ict2105project.viewmodel.LoginViewModel
import com.example.ict2105project.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    //libraries to read up on for our theme:
    //https://material.io/components/text-fields/android
    //https://github.com/leandroBorgesFerreira/LoadingButtonAndroid#animate-and-revert-animation

    private lateinit var binding: ActivityLoginBinding

    //setting up the loginviewmodel for the login
    val loginViewModel: LoginViewModel by viewModels{
        LoginViewModelFactory((application as HRApp).repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //to clear error text
        binding.LoginEmailEditText.doAfterTextChanged {
            binding.LoginEmailEditTextLayout.error = null
        }
        binding.LoginPasswordText.doAfterTextChanged {
            binding.LoginPasswordTextLayout.error = null
        }

        binding.LoginButton.setOnClickListener{
            binding.LoginButton.startAnimation()
            //sanitize data
            val email = binding.LoginEmailEditText.text.toString().trim()
            val password = binding.LoginPasswordText.text.toString()

            if (email.isBlank() && password.isBlank()){
                binding.LoginEmailEditTextLayout.error = getString(R.string.login_email_missing)
                binding.LoginPasswordTextLayout.error = getString(R.string.login_password_missing)
                binding.LoginButton.revertAnimation()
            }
            else if (email.isBlank()){
                binding.LoginEmailEditTextLayout.error = getString(R.string.login_email_missing)
                binding.LoginButton.revertAnimation()
            }
            else if (password.isBlank()){
                binding.LoginPasswordTextLayout.error = getString(R.string.login_password_missing)
                binding.LoginButton.revertAnimation()
            }
            else {
                //logging in, which will take time
                loginViewModel.logInEmployee(email, MD5Hasher.getEncodedStringValue(password))
                //observe response from ViewModel
                loginViewModel.isLoggedIn.observe(this){
                    if (it){
                        loginViewModel.user.observe(this){
                            //store the data into the sharedpreferences
                            val sp: SharedPreferences = this.getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sp.edit()
                            editor.putString(getString(R.string.user_eid), it.eid.toString())
                            editor.putString(getString(R.string.user_role), it.role.toString())
                            editor.apply()

                            //clearing errors for the layout
                            binding.LoginEmailEditTextLayout.error = null
                            binding.LoginPasswordTextLayout.error = null

                            binding.LoginButton.revertAnimation()
                        }
                        goToMain()
                    }
                    else {
                        binding.LoginEmailEditTextLayout.error =
                            getString(R.string.login_emailpassword_wrong)
                        binding.LoginPasswordTextLayout.error =
                            getString(R.string.login_emailpassword_wrong)
                        binding.LoginButton.revertAnimation()
                    }
                }
            }
        }
    }

    /**
     * Function to wrap code up as to go to the main activity
     */
    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Logs out employee when resumed back to this activity
     */
    override fun onResume() {
        super.onResume()
        loginViewModel.logOutEmployee()
    }

}