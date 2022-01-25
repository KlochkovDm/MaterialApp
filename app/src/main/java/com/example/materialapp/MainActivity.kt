package com.example.materialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.materialapp.databinding.MainActivityBinding
import com.example.materialapp.view.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Parameters.getInstance().theme)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, MainFragment.newInstance())
                    .commitNow()
        }
    }
}