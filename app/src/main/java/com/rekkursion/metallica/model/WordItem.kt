package com.rekkursion.metallica.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class WordItem(eng: String, speechList: ArrayList<PartOfSpeech>? = null, chiList: ArrayList<String>? = null, rmk: String? = null) {
    enum class PartOfSpeech(val abbr: String) {
        // region speeches
        NOUN("n"),
        VERB("v"),
        ADVERB("adv"),
        PHRASE("phr"),
        ADJECTIVE("a"),
        PREPOSITION("prep"),
        CONJUNCTION("conj"),
        PRONOUN("pron"),
        ARTICLE("art"), // 冠詞
        INTERJECTION("int"), // 感嘆詞
        AUXILIARY_VERB("aux"), // 助動詞
        TRANSITIVE_VERB("vt"),
        INTRANSITIVE_VERB("vi"),
        COUNTABLE_NOUN("c"),
        UNCOUNTABLE_NOUN("u"),
        PLURAL("pl"),
        ABBREVIATION("abbr");
        // endregion
    }

    private var mEnglishWord: String? = eng
    private var mPartOfSpeechList: ArrayList<PartOfSpeech>? = speechList
    private var mChineseMeaningList: ArrayList<String>? = chiList
    private var mRemark: String? = rmk
    private var mLocalDateTime: LocalDateTime = LocalDateTime.now()

    companion object {
        private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    constructor(eng: String, speech: PartOfSpeech, chi: String, rmk: String, dateTimeStr: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))): this(eng, rmk = rmk) {
        try {
            mPartOfSpeechList = ArrayList()
            mPartOfSpeechList?.add(speech)

            mChineseMeaningList = ArrayList()
            mChineseMeaningList?.add(chi)
        } catch (e: IndexOutOfBoundsException) { e.printStackTrace() }

        try {
            mLocalDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
        } catch (e: DateTimeParseException) { e.printStackTrace() }
    }

    constructor(eng: String, speechStr: String, chi: String, rmk: String, dateTimeStr: String): this(eng, rmk = rmk) {
        try {
            mPartOfSpeechList = ArrayList()
            mPartOfSpeechList?.add(PartOfSpeech.values().filter { it.abbr == speechStr }[0])

            mChineseMeaningList = ArrayList()
            mChineseMeaningList?.add(chi)
        } catch (e: IndexOutOfBoundsException) { e.printStackTrace() }

        try {
            mLocalDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
        } catch (e: DateTimeParseException) { e.printStackTrace() }
    }

    fun getEnglishWord(): String? = mEnglishWord
    fun setEnglishWord(eng: String) { mEnglishWord = eng }

    fun getPartOfSpeechList(): ArrayList<PartOfSpeech>? = mPartOfSpeechList
    fun setPartOfSpeechList(speechList: ArrayList<PartOfSpeech>) { mPartOfSpeechList = speechList }

    fun getChineseMeaningList(): ArrayList<String>? = mChineseMeaningList
    fun setChineseMeaningList(chiList: ArrayList<String>) { mChineseMeaningList = chiList }

    fun getRemark(): String? = mRemark
    fun setRemark(rmk: String) { mRemark = rmk }

    fun getLocalDateTime(): LocalDateTime? = mLocalDateTime
    fun getLocalDateTimeStr(): String? = mLocalDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
    fun setLocalDateTime(date: LocalDateTime) { mLocalDateTime = date }
}