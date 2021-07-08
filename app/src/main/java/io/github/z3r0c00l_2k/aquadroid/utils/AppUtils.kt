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
        fun formatTime(datetime: Date) : String{
            val df = SimpleDateFormat("HH:mm")
            return df.format(datetime)
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