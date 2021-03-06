package ru.dmpolyakov.timetable.models;


public class CalendarDay {

    private int number;
    private boolean isActive;
    private int load = 0;
    private boolean isSelected = false;

    public CalendarDay(int number, boolean isActive) {
        this.number = number;
        this.isActive = isActive;
    }

    public int getNumber() {
        return number;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getLoad() {
        return load;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
