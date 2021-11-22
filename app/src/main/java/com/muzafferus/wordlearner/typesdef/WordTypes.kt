package com.muzafferus.wordlearner.typesdef

import androidx.annotation.IntDef

object WordTypes {
    const val UNCATEGORIZED = 0
    const val NOT_WONT_LEARN = 1
    const val WONT_LEARN = 2
    const val LEARNED = 3

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(UNCATEGORIZED, NOT_WONT_LEARN, WONT_LEARN, LEARNED)
    annotation class WordTypesDef
}
