package com.example.assignment_1.entity

import com.example.assignment_1.enum.Gender
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date
import java.util.UUID

@IgnoreExtraProperties
data class UserData(
    val id: UUID,
    val name: String,
    val email: String,
    val gender: Gender,
    val dob: Date
)
