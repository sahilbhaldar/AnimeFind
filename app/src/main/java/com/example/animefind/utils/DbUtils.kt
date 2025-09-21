package com.example.animefind.utils

import com.example.animefind.data.local.db.AnimeDatabase
import com.google.common.base.Strings.isNullOrEmpty

object DbUtils {

    // Make this a suspend function since it queries DB
    suspend fun isDbEmpty(database: AnimeDatabase): Boolean {
        return database.animeDao().getAllAnime().isNullOrEmpty()
    }
}