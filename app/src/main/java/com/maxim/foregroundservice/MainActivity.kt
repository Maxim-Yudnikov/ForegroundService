package com.maxim.foregroundservice

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.maxim.foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = (application as ProvideViewModel).viewModel()

        binding.titleEditText.addTextChangedListener {
            viewModel.changeTitle(binding.titleEditText.text.toString())
        }

        binding.startButton.setOnClickListener {
            viewModel.start()
        }

        binding.stopButton.setOnClickListener {
            viewModel.stop()
        }

        viewModel.observe(this) {
            it.show(
                binding.titleEditText,
                binding.textView,
                binding.startButton,
                binding.stopButton
            )
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    200
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "Please grand permission for notifications",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
}