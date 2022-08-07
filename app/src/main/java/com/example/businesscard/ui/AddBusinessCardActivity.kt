package com.example.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.businesscard.App
import com.example.businesscard.R
import com.example.businesscard.databinding.ActivityAddBusinessCardBinding
import com.example.businesscard.date.BusinessCard



class AddBusinessCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBusinessCardBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        insertListeners()
    }

    //Passando os valores  para view
    private fun insertListeners() {
        binding.btnConfirm.setOnClickListener {
            val businessCard = BusinessCard(
                nome = binding.tilNome.editText?.text.toString(),
                empresa = binding.tilEmpresa.editText?.text.toString(),
                telefone = binding.tilTelefone.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                fundoPersonalizado = binding.tilCor.editText?.text.toString()
            )
            mainViewModel.insert(businessCard) //Botão confirm
            Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}