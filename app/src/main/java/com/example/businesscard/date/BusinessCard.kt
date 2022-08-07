package com.example.businesscard.date

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
//Passa para o BD o que queremos gravar
data class BusinessCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val empresa: String,
    val telefone: String,
    val email: String,
    val fundoPersonalizado: String
)
