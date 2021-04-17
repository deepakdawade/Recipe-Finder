package com.devdd.recipe.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
@OptIn(ExperimentalTime::class)
object DateFormatter {
    /*
    *  Date patterns constants
    */

    const val INDIAN_STANDARD_FULL_TIME_FORMAT: String = "dd MMM yyyy, hh:mm a"
    const val SERVER_YEAR_DATE_FORMAT: String = "yyyy-MM-dd"
    const val MONTH_YEAR_FORMAT: String = "MMMM, yyyy"
    const val MONTH_FORMAT: String = "MMM"
    const val SERVER_DAY_DATE_FORMAT: String = "dd-MM-yyyy"
    const val DAY_MONTH_YEAR_FORMAT: String = "dd/MM/yyyy"
    const val DAY_MONTH_DAY_FORMAT: String = "E, MMM dd"
    const val HOUR_MINUTE_FORMAT: String = "hh:mm a"

    val timeZoneUTC: TimeZone = TimeZone.getTimeZone("UTC")

    fun dayOfTheYear(): Int = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    fun timeToUTCFormattedDate(pattern: String, time: Long): Date? {
        val simpleDateFormat: SimpleDateFormat = inputSimpleDateFormat(pattern)
        simpleDateFormat.timeZone = timeZoneUTC
        val date: String = simpleDateFormat.format(time)
        return simpleDateFormat.parse(date)
    }

    fun inputSimpleDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

    fun inputSimpleDateFormatUTC(pattern: String): SimpleDateFormat {
        val simpleDateFormat: SimpleDateFormat = inputSimpleDateFormat(pattern)
        simpleDateFormat.timeZone = timeZoneUTC
        return simpleDateFormat
    }

    /*
    * @param time in seconds not milliseconds
    * @return Sat, Aug 10
    */
    fun timeToDayMonthDayFormatter(time: Long): String {
        return inputSimpleDateFormat(DAY_MONTH_DAY_FORMAT).format(Date(time.secondsToMilliSeconds()))
    }

    /*
   * @param time in seconds not milliseconds
   * @return 3 Sep 2019, 6:30 PM
   */
    fun indianStandardFullTimeFormat(time: Long): String {
        return inputSimpleDateFormat(INDIAN_STANDARD_FULL_TIME_FORMAT).format(Date(time.secondsToMilliSeconds()))
    }

    /*
   * @param time in seconds not milliseconds
   */
    fun userProvidedFormat(time: Long, format: String): String {
        return inputSimpleDateFormat(format).format(Date(time.secondsToMilliSeconds()))
    }

    /**
     * @param time in seconds
     * @return time remaining after given time in millis
     */
    fun remainingTime(time: Long, calculateFromStartOfTheDay: Boolean): Long {
        val currentMillis = System.currentTimeMillis()

        val today = if (calculateFromStartOfTheDay) {
            resetTimeToStartOfDay(time = currentMillis, utc = true).time
        } else currentMillis

        val remainingTime = (time.secondsToMilliSeconds()) - today
        return if (remainingTime < 0) 0 else remainingTime
    }

    /**
     * @param time in seconds
     * @return time remaining after given time in days
     */
    fun remainingDays(time: Long, calculateFromStartOfTheDay: Boolean): Int {
        val remainingTime = remainingTime(time, calculateFromStartOfTheDay)
        val remainingDay = remainingTime.milliSecondsToDay()
        return if (remainingDay < 0) 0 else remainingDay
    }

    /**
     * @param time in seconds
     * @return time passed after given time in millis
     */
    fun elapsedTime(time: Long): Long {
        val today = System.currentTimeMillis()
        val elapsedTime = today - (time.secondsToMilliSeconds())
        return if (elapsedTime < 0) 0 else elapsedTime
    }

    /**
     * @param time in seconds
     * @return time passed after given time in days
     */
    fun elapsedDays(time: Long): Int {
        val elapsedTime = elapsedTime(time)
        val elapsedDay = elapsedTime.milliSecondsToDay()
        return if (elapsedDay < 0) 0 else elapsedDay
    }

    /*
   * @param time in milliseconds
   * @return 03-12-2019
   */
    fun serverDayDateFormat(time: Long): String {
        return inputSimpleDateFormat(INDIAN_STANDARD_FULL_TIME_FORMAT).format(Date(time))
    }

    /*
   *  @return tomorrow UTC date
   */
    fun tomorrowUTCDate(): Date {
        val calendar = todayCalender(utc = true)
        calendar.timeInMillis += 1.daysToMilliSeconds()
        return calendar.time
    }

    /*
  *  @return today UTC date
  */
    fun todayUTCDate(): Date {
        return todayCalender(utc = true).time
    }

    /*
  *  @return tomorrow UTC date
  */
    fun yesterdayUTCDate(): Date {
        val calendar = todayCalender(utc = true)
        calendar.timeInMillis -= 1.daysToMilliSeconds()
        return calendar.time
    }

    fun resetTimeToStartOfDay(time: Long, utc: Boolean): Date {
        val calendar = calendar(time, utc)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        return calendar.time
    }


    fun parse(pattern: String, source: String, utc: Boolean = false): Date? {
        return if (utc) inputSimpleDateFormatUTC(pattern).parse(source)
        else inputSimpleDateFormat(pattern).parse(source)
    }

    /*
    * @param pattern to format date
    * @param time in milliseconds
    * @param utc used enable utc zone
    * */
    fun format(pattern: String, time: Long, utc: Boolean = false): String {
        return format(pattern, Date(time), utc)
    }

    fun format(pattern: String, date: Date, utc: Boolean = false): String {
        return if (utc) inputSimpleDateFormatUTC(pattern).format(date)
        else inputSimpleDateFormat(pattern).format(date)
    }

    fun todayCalender(utc: Boolean): Calendar {
        return calendar(time = System.currentTimeMillis(), utc)
    }

    /*
    * @param time in milliseconds
    * @param utc used enable utc zone
    * */
    fun calendar(time: Long, utc: Boolean = false): Calendar {
        return calendar(date = Date(time), utc = utc)
    }

    fun calendar(date: Date, utc: Boolean = false): Calendar {
        val cal = Calendar.getInstance()
        if (utc) cal.timeZone = timeZoneUTC
        cal.time = date
        return cal
    }

    fun Date?.notNull(): Date = requireNotNull(this)

    fun Long.secondsToMilliSeconds(): Long = seconds.toLong(DurationUnit.MILLISECONDS)
    /*
    * @param time in milliseconds
    */
    fun Long.milliSecondsToSeconds(): Long = milliseconds.toLong(DurationUnit.SECONDS)

    fun Int.daysToMilliSeconds(): Long = days.toLong(DurationUnit.MILLISECONDS)

    fun Long.milliSecondsToDay(): Int = milliseconds.toInt(DurationUnit.DAYS)

    /*
    * @param time in milliseconds
    */
    fun Long.toServerTime(): Int = milliseconds.toInt(DurationUnit.SECONDS)

    /*
    * @param time in milliseconds
    */
    fun Int.toServerTime(): Int = milliseconds.toInt(DurationUnit.SECONDS)

    /*
    * @param time in seconds not milliseconds
    */
    fun Long.serverTimeToMilliSeconds(): Long = secondsToMilliSeconds()

    /*
    * @param time in seconds not milliseconds
    */
    fun Int.serverTimeToMilliSeconds(): Long = toLong().secondsToMilliSeconds()
}