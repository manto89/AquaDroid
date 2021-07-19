package io.github.z3r0c00l_2k.aquadroid.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class AppUtils {
    companion object {
        fun calculateIntake(weight: Int, workTime: Int): Double {

            return ((weight * 100 / 3.0) + (workTime / 6 * 7))

        }

        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }

        fun getCurrentDateTime(): String {
            val c = Calendar.getInstance().time
            return formatDateIso8601(c)
        }

        fun formatDateIso8601(datetime: Date): String{
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return df.format(datetime)
        }

        /**
         * Converts the sleeping time into HH:mm format, if the time is before midnight it returns 00:00
         *
         * @property endOfDay the sleeping time as saved in the preferences
         * @return a string representing the end of the day
         */
        fun getEndOfDayTimeString(endOfDay: Date): String{
            val c = Calendar.getInstance()
            c.time = endOfDay
            if (isBeforeMidnight(endOfDay)){
                return "00:00"
            }
            return SimpleDateFormat("HH:mm").format(c.time)
        }

        fun isBeforeMidnight(date: Date): Boolean{
            val c = Calendar.getInstance()
            c.time = date
            //if end of day is before midnight
            if (c.get(Calendar.AM_PM) == Calendar.PM && c.get(Calendar.HOUR) != 0){
                return true
            }
            return false

        }
        /**
         * Compares the *time* (disregarding date) and returns difference in seconds
         *
         * @return a int representing the difference in seconds, it can be negative
         */
        fun differenceInSeconds(first: Date, second:Date): Int {
            val firstDate = Calendar.getInstance()
            firstDate.time = first
            val secondDate = Calendar.getInstance()
            secondDate.time = second
            val diffHours = secondDate.get(Calendar.HOUR_OF_DAY) - firstDate.get(Calendar.HOUR_OF_DAY)
            val diffMinutes = secondDate.get(Calendar.MINUTE) - firstDate.get(Calendar.MINUTE)
            val diffSeconds = secondDate.get(Calendar.SECOND) - firstDate.get(Calendar.SECOND)
//            secondDate.set(Calendar.YEAR, firstDate.get(Calendar.YEAR))
//            secondDate.set(Calendar.DAY_OF_YEAR, firstDate.get(Calendar.DAY_OF_YEAR))
//            return (secondDate.timeInMillis - firstDate.timeInMillis)/1000
            return (diffHours * 3600) + (diffMinutes * 60) + diffSeconds
        }

        val USERS_SHARED_PREF = "user_pref"
        val PRIVATE_MODE = 0
        val WEIGHT_KEY = "weight"
        val WORK_TIME_KEY = "worktime"
        val TOTAL_INTAKE = "totalintake"
        val NOTIFICATION_STATUS_KEY = "notificationstatus"
        val NOTIFICATION_FREQUENCY_KEY = "notificationfrequency"
        val NOTIFICATION_MSG_KEY = "notificationmsg"
        val SLEEPING_TIME_KEY = "sleepingtime"
        val WAKEUP_TIME = "wakeuptime"
        val NOTIFICATION_TONE_URI_KEY = "notificationtone"
        val FIRST_RUN_KEY = "firstrun"
        val QUICK_INTAKE_1 = "intake1"
        val QUICK_INTAKE_2 = "intake2"
        val QUICK_INTAKE_3 = "intake3"
        val QUICK_INTAKE_4 = "intake4"
        val QUICK_INTAKE_5 = "intake5"

    }
}