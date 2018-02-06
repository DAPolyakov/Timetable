package ru.dmpolyakov.timetable.main;


import java.util.ArrayList;
import java.util.Objects;

import ru.dmpolyakov.timetable.common.utils.TimeUtils;
import ru.dmpolyakov.timetable.models.CalendarDay;

class CalendarModel {

    private String currentDate;
    private String initialMouth;
    private String initialYear;
    private int selectedDay = -1;

    CalendarModel() {
        initialMouth = TimeUtils.getMouth(TimeUtils.getCurrentDate());
        initialYear = TimeUtils.getYear(TimeUtils.getCurrentDate());

        setCurrentDate(String.valueOf(initialYear) + "-" + initialMouth + "-01");
    }

    String getDate() {
        return TimeUtils.getMouthName(currentDate) + " " + TimeUtils.getYear(currentDate);
    }

    String getYearMouth() {
        return TimeUtils.getYear(currentDate) + "-" + TimeUtils.getMouth(currentDate);
    }

    String getSelectedDate() {
        String year = TimeUtils.getYear(currentDate);
        String mouth = TimeUtils.getMouth(currentDate);
        String day = (selectedDay < 10 ? "0" : "") + String.valueOf(selectedDay);
        String date = year + "-" + mouth + "-" + day;
        return date;
    }

    void setNextMouth() {
        int mouth = Integer.parseInt(TimeUtils.getMouth(currentDate));
        int year = Integer.parseInt(TimeUtils.getYear(currentDate));
        mouth++;
        if (mouth > 12) {
            mouth = 1;
            year++;
        }
        String mouthStr = (mouth < 10 ? "0" : "") + String.valueOf(mouth);
        setCurrentDate(String.valueOf(year) + "-" + mouthStr + "-01");
    }

    void setPreviousMouth() {
        int mouth = Integer.parseInt(TimeUtils.getMouth(currentDate));
        int year = Integer.parseInt(TimeUtils.getYear(currentDate));
        mouth--;
        if (mouth < 1) {
            mouth = 12;
            year--;
        }
        String mouthStr = (mouth < 10 ? "0" : "") + String.valueOf(mouth);
        setCurrentDate(String.valueOf(year) + "-" + mouthStr + "-01");
    }

    ArrayList<CalendarDay> getDays() {
        int mouth = Integer.parseInt(TimeUtils.getMouth(currentDate));
        int year = Integer.parseInt(TimeUtils.getYear(currentDate));
        int total = TimeUtils.getDaysInMouth(mouth, year);
        int offset = TimeUtils.getWeekDayOffset(TimeUtils.getTimestamp(currentDate));

        ArrayList<CalendarDay> days = new ArrayList<>();

        for (int i = 1; i <= offset; i++) {
            days.add(new CalendarDay(i, false));
        }

        for (int i = 1; i <= total; i++) {
            days.add(new CalendarDay(i, true));
        }

        return days;
    }

    int getSelectedDay() {
        return selectedDay;
    }

    private void setCurrentDate(String date) {
        currentDate = date;
        String initial = initialMouth + initialYear;
        String current = TimeUtils.getMouth(currentDate) + TimeUtils.getYear(currentDate);
        selectedDay = Objects.equals(initial, current) ? TimeUtils.getDay(TimeUtils.getCurrentDate()) : 1;
    }

    public void selectDay(int day) {
        selectedDay = day;
    }
}
