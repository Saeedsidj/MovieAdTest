package com.saeedev.movieadinterviewtest.di

class AdManager {
    private var mainVideosCount: Int = 0
    private val adUrls = mutableListOf<String>()


    fun setAdUrls(urls: List<String>) {
        adUrls.clear()
        adUrls.addAll(urls)
    }

    fun recordMainVideoPlayed() {
        mainVideosCount++
    }

    fun shouldShowAd(): Boolean {
        return mainVideosCount > 0 && mainVideosCount % 4 == 0
    }

    fun getAdUrl(): String {

        val adTriggerCount = mainVideosCount / 4

        return when (adTriggerCount) {
            1, 2, 3 -> adUrls[0]
            4 -> adUrls[1]
            5 -> adUrls[2]
            else -> {
                val index = (adTriggerCount - 6) % adUrls.size
                adUrls[index]
            }
        }
    }

    fun onAdFinished() {
    }
}
