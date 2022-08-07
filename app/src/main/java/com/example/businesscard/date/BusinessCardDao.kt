package com.example.businesscard.date

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BusinessCardDao {

    //Realiza as persistências ao BD
    @Query("SELECT * FROM BusinessCard") //Seleciona da classe BusinessCard
    fun getAll(): LiveData<List<BusinessCard>> //Carrega todos os cards

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(businessCard: BusinessCard)

}
