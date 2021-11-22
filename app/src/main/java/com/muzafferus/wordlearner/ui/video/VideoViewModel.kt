package com.muzafferus.wordlearner.ui.video

import androidx.lifecycle.*
import com.muzafferus.wordlearner.model.VideoModel
import com.muzafferus.wordlearner.model.WordModel
import com.muzafferus.wordlearner.repository.VideoRepository
import com.muzafferus.wordlearner.typesdef.WordTypes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {

    // Using LiveData and caching what allVideos returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allVideos: LiveData<List<VideoModel>> = repository.allVideos.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(video: VideoModel) = viewModelScope.launch {
        repository.insert(video)
    }

    fun update(video: VideoModel) = viewModelScope.launch {
        repository.update(video)
    }

    fun delete(id: Long) = viewModelScope.launch {
        repository.delete(id)
    }

    fun filteredList(
        videos: List<VideoModel>,
        words: List<WordModel>?
    ): ArrayList<VideoModel> {
        words?.let {
            videos.forEach { video ->
                video.percent = calculatePercent(video.transcript, words)
            }

            val shortedList = videos.sortedWith(compareBy { it.percent })

            return ArrayList(shortedList)
        } ?: run {
            return ArrayList(videos)
        }
    }

    private fun calculatePercent(description: String, learnWordList: List<WordModel>): Int {
        val videoWordList = getWordList(description)

        val videoMap: MutableMap<String, Int> = mutableMapOf()

        for (word in videoWordList) {
            val key = word.lowercase(Locale.getDefault())
            val count = videoMap[key]
            videoMap[key] = if (videoMap[key] != null) count!!.plus(1) else 1
        }

        val shortedVideoMap = videoMap.toList()
            .sortedBy { (_, value) -> -value }
            .toMap()

        //UNCATEGORIZED
        val uncategorizedWord: MutableMap<String, Int> = mutableMapOf()

        for (word in learnWordList.filter { it.type == WordTypes.UNCATEGORIZED }) {
            val key = word.word.lowercase(Locale.getDefault())
            val count = uncategorizedWord[key]
            uncategorizedWord[key] = if (uncategorizedWord[key] != null) count!!.plus(1) else 1
        }

        val shortedUncategorizedWordMap = uncategorizedWord.toList()
            .sortedBy { (_, value) -> -value }
            .toMap()

        //NOT_WONT_LEARN
        val notWontLearnWord: MutableMap<String, Int> = mutableMapOf()

        for (word in learnWordList.filter { it.type == WordTypes.NOT_WONT_LEARN }) {
            val key = word.word.lowercase(Locale.getDefault())
            val count = notWontLearnWord[key]
            notWontLearnWord[key] = if (notWontLearnWord[key] != null) count!!.plus(1) else 1
        }

        val shortedNotWontLearnWordMap = notWontLearnWord.toList()
            .sortedBy { (_, value) -> -value }
            .toMap()

        //WONT_LEARN
        val wontLearnWord: MutableMap<String, Int> = mutableMapOf()

        for (word in learnWordList.filter { it.type == WordTypes.WONT_LEARN }) {
            val key = word.word.lowercase(Locale.getDefault())
            val count = wontLearnWord[key]
            wontLearnWord[key] = if (wontLearnWord[key] != null) count!!.plus(1) else 1
        }

        val shortedWontLearnWordMap = wontLearnWord.toList()
            .sortedBy { (_, value) -> -value }
            .toMap()

        //LEARNED
        val learnedWord: MutableMap<String, Int> = mutableMapOf()

        for (word in learnWordList.filter { it.type == WordTypes.LEARNED }) {
            val key = word.word.lowercase(Locale.getDefault())
            val count = learnedWord[key]
            learnedWord[key] = if (learnedWord[key] != null) count!!.plus(1) else 1
        }

        val shortedLearnedWordMap = learnedWord.toList()
            .sortedBy { (_, value) -> -value }
            .toMap()

        return calculate(
            shortedVideoMap,
            shortedUncategorizedWordMap.keys,
            shortedNotWontLearnWordMap.keys,
            shortedWontLearnWordMap.keys,
            shortedLearnedWordMap.keys
        )
    }

    private fun calculate(
        shortedVideoMap: Map<String, Int>,
        uncategorizedWordList: Set<String>,
        notWontLearnWordList: Set<String>,
        wontLearnWordList: Set<String>,
        learnedWordList: Set<String>
    ): Int {
        val videos = ArrayList(shortedVideoMap.toList())
        val uncategorizedWords = ArrayList(uncategorizedWordList.toList())
        val notWontLearnWords = ArrayList(notWontLearnWordList.toList())
        val wontLearnWords = ArrayList(wontLearnWordList.toList())
        val learnedWords = ArrayList(learnedWordList.toList())

        val uncategorizedPercent = getPercent(videos, uncategorizedWords)
        val notWontLearnPercent = getPercent(videos, notWontLearnWords)
        val wontLearnPercent = getPercent(videos, wontLearnWords)
        val learnedPercent = getPercent(videos, learnedWords)

        return (wontLearnPercent * 2) + learnedPercent - notWontLearnPercent + (uncategorizedPercent / 2)
    }

    private fun getPercent(videos: ArrayList<Pair<String, Int>>, words: ArrayList<String>): Int {
        var number = 0
        var numberFull = 0
        videos.forEach { element ->
            numberFull += element.second
            words.forEach { word ->
                if (element.first == word) {
                    number += element.second
                }
            }
        }

        return (number.toDouble() / numberFull.toDouble() * 100).toInt()
    }


    private fun getWordList(text: String): List<String> {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(text, "").split(" ")
    }
}

class VideoViewModelFactory(private val repository: VideoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


