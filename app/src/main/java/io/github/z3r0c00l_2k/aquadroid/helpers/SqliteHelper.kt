package io.github.z3r0c00l_2k.aquadroid.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.mikephil.charting.data.Entry
import io.github.z3r0c00l_2k.aquadroid.utils.AppUtils
import java.text.SimpleDateFormat
import java.util.*

class SqliteHelper(val context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Aqua"
        private val TABLE_STATS = "stats"
        private val KEY_ID = "id"
        private val KEY_DATE = "date"
        private val KEY_INTOOK = "intook"
        private val KEY_TOTAL_INTAKE = "totalintake"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_STATS_TABLE = ("CREATE TABLE " + TABLE_STATS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT UNIQUE,"
                + KEY_INTOOK + " INT," + KEY_TOTAL_INTAKE + " INT" + ")")
        db?.execSQL(CREATE_STATS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS)
        onCreate(db)
    }

    fun addAll(date: String, intook: Int, totalintake: Int): Long {
        if (checkExistance(date) == 0) {
            val values = ContentValues()
            values.put(KEY_DATE, date)
            values.put(KEY_INTOOK, intook)
            values.put(KEY_TOTAL_INTAKE, totalintake)
            val db = this.writableDatabase
            val response = db.insert(TABLE_STATS, null, values)
            db.close()
            return response
        }
        return -1
    }

    fun getRequestedDate(date: String, endOfDay: Long): Date{

        val requestedDate = SimpleDateFormat("dd-MM-yyyy").parse(date)
        val now = Calendar.getInstance()
        val endOfDayIsBeforeMidnight = AppUtils.isBeforeMidnight(Date(endOfDay))
        val nowIsBeforeMidnight = AppUtils.isBeforeMidnight(now.time)
//        val secondsToMidnight = AppUtils.differenceInSeconds(now.time, SimpleDateFormat("HH:mm").parse("00:00"))
        val secondsToEndOfDay = AppUtils.differenceInSeconds(now.time,Date(endOfDay))
        //If somebody sleeps after midnight and now is between midnight and his/hers sleeping time
        //Go back one day
        if(!endOfDayIsBeforeMidnight && !nowIsBeforeMidnight && secondsToEndOfDay > 0 ){
            requestedDate.time = requestedDate.time - 86400000
        }
        return requestedDate
    }

    fun getIntook(date: String, endOfDay: Long ): Int {
        val requestedDate = getRequestedDate(date, endOfDay)
        val endOfDayFormatted = AppUtils.getEndOfDayTimeString(Date(endOfDay))
//        val endOfDayFormatted = AppUtils.formatTime(Date(endOfDay))
        var startDateTimeFormatted = SimpleDateFormat("yyyy-MM-dd").format(requestedDate.time).plus("T").plus(endOfDayFormatted)
        val c = Calendar.getInstance()
        c.time = requestedDate
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1)
        val endDateFormatted = SimpleDateFormat("yyyy-MM-dd").format(c.time)
        val endDateTimeFormatted = endDateFormatted.plus("T").plus(endOfDayFormatted)
        val selectQuery = "SELECT $KEY_INTOOK FROM $TABLE_STATS WHERE $KEY_DATE BETWEEN ? AND ?"
        val db = this.readableDatabase
        var ret = 0
        db.rawQuery(selectQuery, arrayOf(startDateTimeFormatted, endDateTimeFormatted)).use {
            if (it.moveToFirst()) {
                for (i in 0 until it.count){
                    ret += it.getInt(it.getColumnIndex(KEY_INTOOK))
                    it.moveToNext()
                }
            }
        }
        return ret
    }

    fun getTotalIntake(date: String, endOfDay: Long): Int{

        val requestedDate = getRequestedDate(date, endOfDay)
//        val endOfDayTimeString = AppUtils.formatTime(Date(endOfDay))
        val endOfDayTimeString = AppUtils.getEndOfDayTimeString(Date(endOfDay))
        val startDateTimeFormatted = SimpleDateFormat("yyyy-MM-dd").format(requestedDate.time).plus("T").plus(endOfDayTimeString)
        var c = Calendar.getInstance()
        c.time = requestedDate
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1)
        val endDateFormatted = SimpleDateFormat("yyyy-MM-dd").format(c.time)
        val endDateTimeFormatted = endDateFormatted.plus("T").plus(endOfDayTimeString)
        val selectQuery = "SELECT MAX($KEY_TOTAL_INTAKE) FROM $TABLE_STATS WHERE $KEY_DATE BETWEEN ? AND ?"
        val db = this.readableDatabase
        var ret = 0
        db.rawQuery(selectQuery, arrayOf(startDateTimeFormatted, endDateTimeFormatted)).use {
            if (it.moveToFirst()) {
                ret += it.getInt(0)
//                for (i in 0 until it.count){
//                    ret += it.getInt(it.getColumnIndex(KEY_INTOOK))
//                    it.moveToNext()
//                }
            }
        }
        return ret
    }

    fun addIntook(date: String, selectedOption: Int, totalintake: Int): Long {
        val values = ContentValues()
        values.put(KEY_DATE, date)
        values.put(KEY_INTOOK, selectedOption)
        values.put(KEY_TOTAL_INTAKE, totalintake)
        val db = this.writableDatabase
        val response = db.insert(TABLE_STATS, null, values)
        db.close()
        return response
//        val intook = getIntook(date)
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(KEY_INTOOK, intook + selectedOption)
//
//        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
//        db.close()
//        return response
    }

    fun checkExistance(date: String): Int {
        val selectQuery = "SELECT $KEY_INTOOK FROM $TABLE_STATS WHERE $KEY_DATE = ?"
        val db = this.readableDatabase
        db.rawQuery(selectQuery, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.count
            }
        }
        return 0
    }

    fun getStatsInRange(start: Date, end: Date, sleepingTime: Long): ArrayList<Entry>{

//        var s = Calendar.getInstance()
//        s.time = start
//        val startDay = s.get(Calendar.DAY_OF_YEAR)
//
//        var e = Calendar.getInstance()
//        e.time = end
//        val endDay = e.get(Calendar.Day)
        var ret = arrayListOf<Entry>()
        var counter = 0
        for (i in start.time..end.time step 86400000){
            val c = Calendar.getInstance()
            c.timeInMillis = i
            val intook = getIntook(SimpleDateFormat("dd-MM-yyyy").format(c.time), sleepingTime)
            val total = getTotalIntake(SimpleDateFormat("dd-MM-yyyy").format(c.time),sleepingTime)
            var percentage = 0.0f
            if(total > 0){
                percentage = (intook/total.toFloat()) * 100
            }
            ret.add(Entry(counter.toFloat(), percentage))
            counter++
        }
        return ret
    }

    fun getAllStats(): Cursor {
        val selectQuery = "SELECT * FROM $TABLE_STATS"
        val db = this.readableDatabase
        return db.rawQuery(selectQuery, null)

    }

    fun updateTotalIntake(date: String, totalintake: Int, sleepingTime: Long): Int {
        val intook = getIntook(date, sleepingTime)
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TOTAL_INTAKE, totalintake)

        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
        db.close()
        return response
    }

}
