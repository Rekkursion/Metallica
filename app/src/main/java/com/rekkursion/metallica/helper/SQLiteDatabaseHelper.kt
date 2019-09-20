package com.rekkursion.metallica.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.content.ContentValues
import com.rekkursion.metallica.R
import com.rekkursion.metallica.model.WordItem






class SQLiteDatabaseHelper(pContext: Context): SQLiteOpenHelper(pContext, "words.db", null, 1) {
    private var mContext: Context = pContext

    private val DATABASE_VERSION = 1
    private val DATABASE_NAME = "words.db"
    private val RANKING_TABLE_NAME = "Words"
    private val RANKING_TABLE_COL_ID = "_id"
    private val RANKING_TABLE_COL_ENGLISH_WORD = "_eng"
    private val RANKING_TABLE_COL_PART_OF_SPEECH = "_speech"
    private val RANKING_TABLE_COL_CHINESE_MEANING = "_chi"
    private val RANKING_TABLE_COL_REMARK = "_rmk"
    private val RANKING_TABLE_COL_LOCAL_DATE_TIME = "_date"

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + RANKING_TABLE_NAME + " (" +
                RANKING_TABLE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RANKING_TABLE_COL_ENGLISH_WORD + " VARCHAR(70), " +
                RANKING_TABLE_COL_PART_OF_SPEECH + " VARCHAR(18), " +
                RANKING_TABLE_COL_CHINESE_MEANING + " VARCHAR(255), " +
                RANKING_TABLE_COL_REMARK + " VARCHAR(65535), " +
                RANKING_TABLE_COL_LOCAL_DATE_TIME + " VARCHAR(25)" +
                ");"
        db?.execSQL(sqlCreateTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    // insert
    fun insertData(item: WordItem) {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(RANKING_TABLE_COL_ENGLISH_WORD, item.getEnglishWord())
        cv.put(RANKING_TABLE_COL_PART_OF_SPEECH, item.getPartOfSpeech()?.abbr)
        cv.put(RANKING_TABLE_COL_CHINESE_MEANING, item.getChineseMeaning())
        cv.put(RANKING_TABLE_COL_REMARK, item.getRemark())
        cv.put(RANKING_TABLE_COL_LOCAL_DATE_TIME, item.getLocalDateTimeStr())

        val result = db.insert(RANKING_TABLE_NAME, null, cv)
        if (result == -1L)
            Toast.makeText(mContext, mContext.getString(R.string.str_error_when_inserting_data), Toast.LENGTH_SHORT).show()

        db.close()
    }

    // read
    fun readData(): ArrayList<WordItem> {
        val sqlQueryString = "SELECT * FROM $RANKING_TABLE_NAME"
        return readDataHelper(sqlQueryString)
    }

    // read data through the query string
    private fun readDataHelper(sqlQueryString: String): ArrayList<WordItem> {
        val db = readableDatabase
        val cursor = db.rawQuery(sqlQueryString, null)
        val retList = arrayListOf<WordItem>()

        if (cursor.moveToFirst()) {
            do {
                val eng = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_ENGLISH_WORD))
                val speechStr = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_PART_OF_SPEECH))
                val chi = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_CHINESE_MEANING))
                val rmk = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_REMARK))
                val dateTimeStr = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_LOCAL_DATE_TIME))

                retList.add(WordItem(eng, speechStr, chi, rmk, dateTimeStr))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return retList
    }
}