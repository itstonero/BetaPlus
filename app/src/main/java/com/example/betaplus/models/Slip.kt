package com.example.betaplus.models

class Slip
{
    var id: Int = 0
    var retryCount : Int = 0
    var retryLimit : Int = 0
    var progressCount: Int = 0
    var progressLimit: Int = 0
    var oddCount: Double = 0.0 
    var oddLimit: Double = 0.0
    var amount: Double = 0.0 
    var bonus:Double = 0.0
    var initOdd:Double = 0.0 
    var growOdd:Double = 0.0
    var retryOdd:Double = 0.0
}