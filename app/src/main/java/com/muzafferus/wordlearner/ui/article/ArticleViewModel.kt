package com.muzafferus.wordlearner.ui.article

import androidx.lifecycle.*
import com.muzafferus.wordlearner.model.ArticleModel
import com.muzafferus.wordlearner.model.WordModel
import com.muzafferus.wordlearner.repository.ArticleRepository
import com.muzafferus.wordlearner.typesdef.WordTypes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    // Using LiveData and caching what allArticles returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allArticles: LiveData<List<ArticleModel>> = repository.allArticles.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(article: ArticleModel) = viewModelScope.launch {
        repository.insert(article)
    }

    fun update(article: ArticleModel) = viewModelScope.launch {
        repository.update(article)
    }

    fun delete(id: Long) = viewModelScope.launch {
        repository.delete(id)
    }

    fun filteredList(
        articles: List<ArticleModel>,
        words: List<WordModel>?
    ): ArrayList<ArticleModel> {
        words?.let {
            articles.forEach { article ->
                article.percent = calculatePercent(article.description, words)
            }

            val shortedList = articles.sortedWith(compareBy { it.percent })

            return ArrayList(shortedList)
        } ?: run {
            return ArrayList(articles)
        }
    }

    private fun calculatePercent(description: String, learnWordList: List<WordModel>): Int {
        val articleWordList = getWordList(description)

        val articleMap: MutableMap<String, Int> = mutableMapOf()

        for (word in articleWordList) {
            val key = word.lowercase(Locale.getDefault())
            val count = articleMap[key]
            articleMap[key] = if (articleMap[key] != null) count!!.plus(1) else 1
        }

        val shortedArticleMap = articleMap.toList()
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
            shortedArticleMap,
            shortedNotWontLearnWordMap.keys,
            shortedWontLearnWordMap.keys,
            shortedLearnedWordMap.keys
        )
    }

    private fun calculate(
        shortedArticleMap: Map<String, Int>,
        notWontLearnWordList: Set<String>,
        wontLearnWordList: Set<String>,
        learnedWordList: Set<String>
    ): Int {
        val articles = ArrayList(shortedArticleMap.toList())
        val notWontLearnWords = ArrayList(notWontLearnWordList.toList())
        val wontLearnWords = ArrayList(wontLearnWordList.toList())
        val learnedWords = ArrayList(learnedWordList.toList())

        val notWontLearnPercent = getPercent(articles, notWontLearnWords)
        val wontLearnPercent = getPercent(articles, wontLearnWords)
        val learnedPercent = getPercent(articles, learnedWords)

        return (wontLearnPercent * 2) + learnedPercent - notWontLearnPercent
    }

    private fun getPercent(articles: ArrayList<Pair<String, Int>>, words: ArrayList<String>): Int {
        var number = 0
        var numberFull = 0
        articles.forEach { element ->
            numberFull += element.second
            words.forEach { word ->
                if (element.first == word) {
                    number += element.second
                }
            }
        }

        return (number.toDouble() / numberFull.toDouble() * 100).toInt()
    }


    fun getWordList(text: String): List<String> {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(text, "").split(" ")
    }
}

class ArticleViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


