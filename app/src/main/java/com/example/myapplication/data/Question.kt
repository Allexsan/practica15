package com.example.myapplication.data

import java.nio.file.attribute.AclEntry.Builder

class Question(
    val textQuestion: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val right: Int
) {
}