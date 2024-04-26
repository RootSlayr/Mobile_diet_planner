package com.example.assignment_1.entity

import com.example.assignment_1.enum.Gender
import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDate

@IgnoreExtraProperties
data class UserData(val id: String, val name: String, val email: String, val gender: Gender, val dob: LocalDate)
