package io.github.z3r0c00l_2k.aquadroid.utils

import KotlinParametrizedTest
import org.junit.Assert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.text.SimpleDateFormat
import java.util.*

class AppUtilsTest{

    @Nested
    @KotlinParametrizedTest
    @DisplayName("Difference in seconds: ")
    inner class DifferenceInSecondsTest {

        fun datetimeInSameDay(): Iterable<Array<Any>> {
            return arrayListOf(
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:01:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:02:00"),
                    60
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:02:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:01:00"),
                    -60
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 01:00:00"),
                    3600
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 01:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    -3600
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:01"),
                    1
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:01"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    -1
                )
            )
        }
        fun datetimeInDifferentDays(): Iterable<Array<Any>>{
            return arrayListOf(

                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:59:00"),
                    (23 * 3600) + (59 * 60)
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:59:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    -((23 * 3600) + (59 * 60))
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:00:00"),
                    (23 * 3600)
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    -(23 * 3600)
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:59:59"),
                    (23 * 3600) + (59 * 60) + 59
                ),
                arrayOf(
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-17 23:59:59"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-07-18 00:00:00"),
                    -((23 * 3600) + (59 * 60) + 59)
                )
            )
        }

        @ParameterizedTest
        @MethodSource("datetimeInSameDay")
        @DisplayName("given valid Dates in same day when called then results are correct")
        fun sameDayDifferenceInSecondsTest(first: Date, second: Date, expectedDifference: Int) {
            val difference = AppUtils.differenceInSeconds(first, second)
            Assert.assertEquals(expectedDifference, difference)
        }

        @ParameterizedTest
        @MethodSource("datetimeInDifferentDays")
        @DisplayName("given valid Dates in different days when called then it ignores the date part")
        fun differentDaysDifferenceInSecondsTest(first: Date, second: Date, expectedDifference: Int) {
            val difference = AppUtils.differenceInSeconds(first, second)
            Assert.assertEquals(expectedDifference, difference)
        }

    }
}
