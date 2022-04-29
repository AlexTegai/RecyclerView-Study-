package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserListener
import com.example.recyclerview.model.UserService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val userService: UserService
        get() = (applicationContext as App).userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) = userService.moveUser(user, moveBy)

            override fun onUserFire(user: User) = userService.fireUser(user)

            override fun onUserDelete(user: User) = userService.deleteUser(user)

            override fun onUserDetails(user: User) {
                Toast.makeText(
                    this@MainActivity,
                    "User: ${user.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // Убираем анимацию ячейки, при удалении компании
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) itemAnimator.supportsChangeAnimations = false

        userService.addListener(userListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        userService.removeListener(userListener)
    }

    private val userListener: UserListener = {
        adapter.users = it
    }

}