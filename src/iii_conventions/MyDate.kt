package iii_conventions

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate): Int {
        val ydiff = year - other.year
        if (ydiff != 0) return ydiff
        val mdiff = month - other.month
        if (mdiff != 0) return mdiff
        return dayOfMonth - other.dayOfMonth
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

interface TimeInterval

enum class BaseTimeInterval : TimeInterval {
    DAY,
    WEEK,
    YEAR
}

class DateRange(override val start: MyDate, override val endInclusive: MyDate) : ClosedRange<MyDate> {
    operator fun iterator(): Iterator<MyDate> = object : Iterator<MyDate> {

        var date = start
        override fun next(): MyDate {
            if (hasNext()) {
                val ret = date
                date = date.nextDay()
                return ret
            } else {
                throw IndexOutOfBoundsException()
            }
        }

        override fun hasNext(): Boolean {
            return date <= endInclusive
        }
    }
}

data class RepeatedTimeInterval(val interval: TimeInterval, val n: Int) : TimeInterval

data class SumTimeInterval(val l: TimeInterval, val r: TimeInterval): TimeInterval

infix operator fun MyDate.plus(interval: TimeInterval): MyDate {
    return when (interval) {
        is RepeatedTimeInterval -> (1..interval.n).fold(this) { d, n -> d + interval.interval }
        is SumTimeInterval -> this + interval.l + interval.r
        BaseTimeInterval.DAY -> return nextDay()
        BaseTimeInterval.WEEK -> (1..7).fold(this) { d, n -> d.nextDay() }
        BaseTimeInterval.YEAR -> {
            if (month == 2 && dayOfMonth == 29) {
                MyDate(year + 1, 3, 1)
            } else {
                MyDate(year + 1, month, dayOfMonth)
            }
        }
        else -> throw IllegalArgumentException("I haven't figured out how to seal this class but consider it sealed")

    }
}

infix operator fun TimeInterval.times(n: Int): TimeInterval = RepeatedTimeInterval(this, n)
infix operator fun TimeInterval.plus(other: TimeInterval): TimeInterval = SumTimeInterval(this, other)
