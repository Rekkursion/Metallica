package com.rekkursion.metallica.model

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClassificationItem(groupName: String, wordList: ArrayList<WordItem> = arrayListOf()): Comparable<ClassificationItem>, Serializable {
    companion object {
        private const val serialVersionUID = 10615031L
        private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    private var mGroupName: String = groupName
    private var mWordList: ArrayList<WordItem> = wordList
    private var mLocalDateTime: LocalDateTime = LocalDateTime.now()

    fun addWord(word: WordItem) {
        mWordList.add(word)
    }

    fun getGroupName(): String = mGroupName
    fun setGroupName(groupName: String) { mGroupName = groupName }

    fun getWordList(): ArrayList<WordItem> = mWordList
    fun setWordList(wordList: ArrayList<WordItem>) { mWordList = wordList }

    fun getLocalDateTime(): LocalDateTime? = mLocalDateTime
    fun getLocalDateTimeStr(): String? = mLocalDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
    fun setLocalDateTime(date: LocalDateTime) { mLocalDateTime = date }

    override fun compareTo(other: ClassificationItem): Int = when {
        this.mLocalDateTime.isAfter(other.mLocalDateTime) -> 1
        this.mLocalDateTime.isBefore(other.mLocalDateTime) -> -1
        else -> 0
    }
}