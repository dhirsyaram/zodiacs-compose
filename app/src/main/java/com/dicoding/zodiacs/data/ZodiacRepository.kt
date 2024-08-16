package com.dicoding.zodiacs.data

class ZodiacRepository {

    companion object {
        @Volatile
        private var instance: ZodiacRepository? = null

        fun getInstance(): ZodiacRepository =
            instance ?: synchronized(this) {
                ZodiacRepository().apply {
                    instance = this
                }
            }
    }
}