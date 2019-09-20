package com.rekkursion.metallica.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class WordItem(eng: String, speech: PartOfSpeech? = null, chi: String? = null, rmk: String? = null) {
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
    private var mPartOfSpeech: PartOfSpeech? = speech
    private var mChineseMeaning: String? = chi
    private var mRemark: String? = rmk
    private var mLocalDateTime: LocalDateTime = LocalDateTime.now()

    companion object {
        private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    constructor(eng: String, speechStr: String, chi: String, rmk: String, dateTimeStr: String): this(eng, chi = chi, rmk = rmk) {
        try {
            mPartOfSpeech = PartOfSpeech.values().filter { it.abbr == speechStr }[0]
        } catch (e: IndexOutOfBoundsException) { e.printStackTrace() }

        try {
            mLocalDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
        } catch (e: DateTimeParseException) { e.printStackTrace() }
    }

    fun getEnglishWord(): String? = mEnglishWord
    fun setEnglishWord(eng: String) { mEnglishWord = eng }

    fun getPartOfSpeech(): PartOfSpeech? = mPartOfSpeech
    fun setPartOfSpeech(speech: PartOfSpeech) { mPartOfSpeech = speech }

    fun getChineseMeaning(): String? = mChineseMeaning
    fun setChineseMeaning(chi: String) { mChineseMeaning = chi }

    fun getRemark(): String? = mRemark
    fun setRemark(rmk: String) { mRemark = rmk }

    fun getLocalDateTime(): LocalDateTime? = mLocalDateTime
    fun getLocalDateTimeStr(): String? = mLocalDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))
    fun setLocalDateTime(date: LocalDateTime) { mLocalDateTime = date }
}