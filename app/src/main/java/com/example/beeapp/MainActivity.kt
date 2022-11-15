package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.ViewPagerAdapter
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private lateinit var mainGoUserButton: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var ivProfilePicture: ImageView



    private lateinit var fragmentAdapter: ViewPagerAdapter
    private lateinit var pager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        initVar()
        pager = viewBinding.pager
        pager.adapter = fragmentAdapter
        tabLayout = viewBinding.tabLayout
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,
            pager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Groups"
                }
                1 -> {
                    tab.text = "Chats"
                }
            }
        }
        tabLayoutMediator.attach()
        initListeners()

    }

    private fun initVar() {
        mainGoUserButton = viewBinding.mainGoUserButton
        tvUsername = viewBinding.tvUsername
        ivProfilePicture = viewBinding.ivUserImage
        loadProfilePicture()
        fragmentAdapter = ViewPagerAdapter(this)
        tvUsername.text = loggedUser.username

    }

    private fun loadProfilePicture() {

        var imageRef: String? = null
        try {
            /*dbRef.child("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (postSnapshot in snapshot.children) {
                            val currentUser = postSnapshot.getValue(User::class.java)

                            if (auth.currentUser?.uid.equals(currentUser?.uid)) {
                                //imageRef = currentUser?.profilePicture
                            }
                            try {
                                Glide.with(this@MainActivity).load(imageRef).into(mainGoUserButton)
                            }catch (e: Exception){
                                e.stackTrace
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //NADA
                    }
                })*/
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun tvUsername() {


        //tvUsername.text = response.body()!!.username ERROR AL INTENTAR VOLVER DESDE OTRA ACTIVITY


        /* dbRef.child("users").addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {

                 for (postSnapshot in snapshot.children){
                     val currentUser = postSnapshot.getValue(User::class.java)

                     if (auth.currentUser?.uid.equals(currentUser?.uid) ){
                         loggedUserEmail = currentUser?.email.toString()
                         loggedUser = currentUser?.username.toString()
                         tvUsername.text = loggedUser
                     }
                 }
             }
             override fun onCancelled(error: DatabaseError) {
                 Log.e("ERROR", "Something went wrong")
             }

         })*/

    }

    private fun initListeners() {
        mainGoUserButton.setOnClickListener { displayUser() }
    }

    private fun displayUser() {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra("username", loggedUser.username)
        //intent.putExtra("email",loggedUserEmail)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                // auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.add_friend -> {
                startActivity(Intent(this, AddContactActivity::class.java))
            }

            /*R.id.search_action -> {
                val actionView = item.actionView as SearchView
                actionView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        Log.e("ERROR", "Something went wrong")
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        tempList.clear()
                        val searchText = newText!!.lowercase(Locale.getDefault())

                        if (searchText.isNotEmpty()) {
                            userList.forEach {
                                if (it.username?.lowercase(Locale.getDefault())!!.contains(searchText)) {
                                    tempList.add(it)
                                }
                            }

                            rvChats.adapter?.notifyDataSetChanged()
                        } else {
                            tempList.clear()
                            tempList.addAll(userList)
                            rvChats.adapter!!.notifyDataSetChanged()
                        }

                        ("No modifica el rv por algún motivo")
                        return false
                    }
                })
            }*/
        }
        return true
    }
}