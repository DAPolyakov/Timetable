package pro.redstart.myapplication.common.utils;


import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtils {

    private static int[] mouthDays = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static String getCurrentDate() {
        return getDate(System.currentTimeMillis());
    }

    public static String getDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru_RU"));
        String date = dateFormat.format(new Date(timestamp));
        return date;
    }

    public static int getWeekDayOffset(Long ts){
        long tsOfMonday = 345600L; // some monday

        int offset = (int) (Math.abs(ts - tsOfMonday)) % 604800 / 86400; // 604800 = 7 days, 86400 = 1 day
        return offset;
    }

    public static int getDay(String date) {
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

    public static String getMouth(String date) {
        String mouth = date.substring(5, 7);
        return mouth;
    }

    public static String getMouthName(String date) {
        return getMouthText(getMouth(date));
    }

    public static String getYear(String date) {
        return date.substring(0, 4);
    }

    public static Long getTimestamp(String date) {
        date = date + " 00:00:00";
        Timestamp tmp = java.sql.Timestamp.valueOf(date);
        return (tmp.getTime() / 1000) + 7201; // plus 2 hours, no comment...
    }

    private static String getMouthText(String mouthId) {
        switch (mouthId) {
            case "01":
                return "Январь";
            case "02":
                return "Февраль";
            case "03":
                return "Март";
            case "04":
                return "Апрель";
            case "05":
                return "Май";
            case "06":
                return "Июнь";
            case "07":
                return "Июль";
            case "08":
                return "Август";
            case "09":
                return "Сентябрь";
            case "10":
                return "Октябрь";
            case "11":
                return "Ноябрь";
            case "12":
                return "Декабрь";
        }

        return "none";
    }

    public static int getDaysInMouth(int mouth, int year) {
        if (mouth != 2) {
            return mouthDays[mouth];
        } else return mouthDays[2] + (year % 4 == 0 ? 1 : 0);
    }
}
