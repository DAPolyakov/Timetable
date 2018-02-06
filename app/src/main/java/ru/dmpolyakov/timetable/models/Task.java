package ru.dmpolyakov.timetable.models;


public class Task {

    private String title;
    private int stHour;
    private int stMinute;
    private int finHour;
    private int finMinute;

    private String description = "";

    public Task(String title, int stHour, int stMinute, int finHour, int finMinute) {
        this.title = title;
        this.stHour = stHour;
        this.stMinute = stMinute;
        this.finHour = finHour;
        this.finMinute = finMinute;
    }

    public String getFullTime() {
        String stHourStr = (stHour < 10 ? "0" : "") + String.valueOf(stHour);
        String stMinuteStr = (stMinute < 10 ? "0" : "") + String.valueOf(stMinute);
        String finHourStr = (finHour < 10 ? "0" : "") + String.valueOf(finHour);
        String finMinuteStr = (finMinute < 10 ? "0" : "") + String.valueOf(finMinute);

        String time = stHourStr + ":" + stMinuteStr + " - " + finHourStr + ":" + finMinuteStr;
        return time;
    }

    public String getTitle() {
        return title;
    }

    public int getStHour() {
        return stHour;
    }

    public int getStMinute() {
        return stMinute;
    }

    public int getFinHour() {
        return finHour;
    }

    public int getFinMinute() {
        return finMinute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}