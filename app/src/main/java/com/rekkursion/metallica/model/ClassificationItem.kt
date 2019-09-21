package com.rekkursion.metallica.model

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClassificationItem(groupName: String, wordList: ArrayList<WordItem> = arrayListOf()): Serializable {
    companion object {
        private const val serialVersionUID = 10615031L
        private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    private var mGroupName: String = groupName
    private var mWordList: ArrayList<WordItem> = wordList
    private var mLocalDateTime: LocalDateTime = LocalDateTime.now()

    fun getGroupName(): String = mGroupName
    fun setGroupName(groupName: String) { mGroupName = groupName }

    fun getWordList(): ArrayList<WordItem> = mWordList
    fun setWordList(wordList: ArrayList<WordItem>) { mWordList = wordList }

    fun getLocalDateTime(): LocalDateTime? = mLocalDateTime
    fun getLocalDateTimeStr(): String? = mLocalDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
    fun setLocalDateTime(date: LocalDateTime) { mLocalDateTime = date }
}