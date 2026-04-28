public class Timestamp
{
    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    private int hour;
    private int minute;

    public Timestamp()
    {
        dayOfMonth = 1;
        monthOfYear = 1;
        year = 2000;
        hour = 0;
        minute = 0;
    }

    public Timestamp(int dayOfMonth, int monthOfYear, int year, int hour, int minute)
    {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Timestamp(Timestamp other)
    {
        this.dayOfMonth = other.getDayOfMonth();
        this.monthOfYear = other.getMonthOfYear();
        this.year = other.Year();
        this.hour = other.getHour();
        this.minute = other.getMinute();
    }

    public int getDayOfMonth()
    {
        return dayOfMonth;
    }

    public int getMonthOfYear()
    {
        return monthOfYear;
    }

    public int getYear()
    {
        return year;
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public void setDayOfMonth(int dayOfMonth)
    {
        this.dayOfMonth = dayOfMonth
    }

    public void setMonthOfYear(int monthOfYear)
    {
        this.monthOfYear = monthOfYear
    }

    public void setYear (int year)
    {
        this.year = year;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }

    public void setMinute(int minute)
    {
        this.minute = minute;
    }

    public String toString()
    {
        return dayOfMonth + "/" + monthOfYear + "/" + year + " " + house + ":" + minute;
    }
}
