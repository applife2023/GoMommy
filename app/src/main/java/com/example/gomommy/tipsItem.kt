package com.example.gomommy

data class tipsItem(
    val itemTitle: String,
    val itemDesc: String,
    val itemSource: String
){

    // Empty constructor required for Firebase deserialization
    constructor() : this("", "", "")

}
