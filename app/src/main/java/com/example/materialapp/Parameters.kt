package com.example.materialapp

class Parameters {

    var resetFragment: Boolean = false
    var theme: Int = R.style.Theme_MaterialApp

    companion object {
        @Volatile
        private var INSTANCE: Parameters? = null
        fun getInstance(): Parameters {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Parameters()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}