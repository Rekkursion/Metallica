package com.rekkursion.metallica.model

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WordItem(eng: String, speechList: ArrayList<PartOfSpeech>? = null, chiList: ArrayList<String>? = null, rmk: String? = null, diff: Int? = null, classificationList: ArrayList<ClassificationItem>? = null): Serializable {
    enum class PartOfSpeech(val abbr: String, val chinese: String) {
        // region speeches
        NOUN("n", "名詞"),
        COUNTABLE_NOUN("c", "可數名詞"),
        UNCOUNTABLE_NOUN("u", "不可數名詞"),
        VERB("v", "動詞"),
        TRANSITIVE_VERB("vt", "及物動詞"),
        INTRANSITIVE_VERB("vi", "不及物動詞"),
        ADVERB("adv", "副詞"),
        PHRASE("phr", "片語"),
        ADJECTIVE("a", "形容詞"),
        PREPOSITION("prep", "介係詞"),
        CONJUNCTION("conj", "連接詞"),
        PRONOUN("pron", "代名詞"),
        ARTICLE("art", "冠詞"), // 冠詞
        INTERJECTION("int", "感嘆詞"), // 感嘆詞
        AUXILIARY_VERB("aux", "助動詞"), // 助動詞
        PLURAL("pl", "複數"),
        ABBREVIATION("abbr", "縮寫");
        // endregion
    }

    companion object {
        private const val serialVersionUID = 8829975621220483374L
        private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    private var mEnglishWord: String? = eng
    private var mPartOfSpeechList: ArrayList<PartOfSpeech>? = speechList
    private var mChineseMeaningList: ArrayList<String>? = chiList
    private var mRemark: String? = rmk
    private var mLocalDateTime: LocalDateTime = LocalDateTime.now()
    private var mDifficulty: Int? = diff
    private var mClassificationList: ArrayList<ClassificationItem>? = classificationList

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

    fun getDifficulty(): Int? = mDifficulty
    fun setDifficulty(diff: Int) { mDifficulty = diff }

    fun getClassificationList(): ArrayList<ClassificationItem>? = mClassificationList
    fun setClassificationList(classificationList: ArrayList<ClassificationItem>) { mClassificationList = classificationList }
}