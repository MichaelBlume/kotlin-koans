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

enum class TimeInterval {
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
