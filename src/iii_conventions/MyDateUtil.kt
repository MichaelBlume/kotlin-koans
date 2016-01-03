package iii_conventions

import java.util.Calendar
import iii_conventions.BaseTimeInterval.YEAR
import iii_conventions.BaseTimeInterval.DAY
import iii_conventions.BaseTimeInterval.WEEK

fun MyDate.nextDay() = addTimeIntervals(DAY, 1)

fun MyDate.addTimeIntervals(baseTimeInterval: BaseTimeInterval, number: Int): MyDate {
    val c = Calendar.getInstance()
    c.set(year + if (baseTimeInterval == YEAR) number else 0, month, dayOfMonth)
    var timeInMillis = c.timeInMillis
    val millisecondsInADay = 24 * 60 * 60 * 1000L
    timeInMillis += number * when (baseTimeInterval) {
        DAY -> millisecondsInADay
        WEEK -> 7 * millisecondsInADay
        YEAR -> 0L
    }
    val result = Calendar.getInstance()
    result.timeInMillis = timeInMillis
    return MyDate(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DATE))
}