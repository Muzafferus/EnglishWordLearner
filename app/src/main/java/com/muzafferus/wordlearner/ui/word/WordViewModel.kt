package com.muzafferus.wordlearner.ui.word

import androidx.lifecycle.*
import com.muzafferus.wordlearner.model.WordModel
import com.muzafferus.wordlearner.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<WordModel>> = repository.allWords.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: WordModel) = viewModelScope.launch {
        repository.insert(word)
    }

    fun update(word: WordModel) = viewModelScope.launch {
        repository.update(word)
    }

    fun delete(word: String) = viewModelScope.launch {
        repository.delete(word)
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
