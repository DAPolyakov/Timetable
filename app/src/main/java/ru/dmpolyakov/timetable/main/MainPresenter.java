package ru.dmpolyakov.timetable.main;

import android.content.SharedPreferences;

import java.util.ArrayList;

import ru.dmpolyakov.timetable.common.PresenterBase;
import ru.dmpolyakov.timetable.common.utils.TimeUtils;
import ru.dmpolyakov.timetable.models.CalendarDay;
import ru.dmpolyakov.timetable.models.Task;

public class MainPresenter extends PresenterBase<MainContract.View> implements MainContract.Presenter {

    private CalendarModel calendar = new CalendarModel();
    private TimetableModel timetable;

    MainPresenter(SharedPreferences preferences) {
        timetable = new TimetableModel(preferences);
    }

    @Override
    public void viewIsReady() {
        updateCalendarView();
    }

    @Override
    public void onPreviousMouth() {
        calendar.setPreviousMouth();
        updateCalendarView();
    }

    @Override
    public void onNextMouth() {
        calendar.setNextMouth();
        updateCalendarView();
    }

    @Override
    public void onDayClick(CalendarDay day) {
        calendar.selectDay(day.getNumber());
        getView().setSelectedDay(day.getNumber());
        updateTaskView();
    }

    private void updateCalendarView() {
        getView().setDate(calendar.getDate());
        getView().showDays(changeDaysLoad(calendar.getDays()));
        getView().setSelectedDay(calendar.getSelectedDay());
        updateTaskView();
    }

    private ArrayList<CalendarDay> changeDaysLoad(ArrayList<CalendarDay> days) {
        for (CalendarDay day : days) {
            if (day.isActive()) {
                int number = day.getNumber();
                String date = calendar.getYearMouth() + "-" + (number < 10 ? "0" : "") + number;
                day.setLoad(timetable.getCountTask(date));
            }
        }

        return days;
    }

    private void updateTaskView() {
        getView().showTasks(timetable.getTasks(calendar.getSelectedDate()));
    }

    @Override
    public void onAddTask() {
        getView().showNewTaskDialog();
    }

    @Override
    public void onTaskAdded(String title, int stHour, int stMinute, int fnHour, int fnMinute, boolean isRepeat) {
        getView().clearNewTaskDialog();
        getView().dismissNewTaskDialog();

        Task task = new Task(title, stHour, stMinute, fnHour, fnMinute);
        timetable.addTask(task);
        if (isRepeat) {
            repeatTask(task);
        }
        updateCalendarView();
    }

    private void repeatTask(Task task) {
        long week = 604800;
        long repeatCount = 3;

        long date = TimeUtils.getTimestamp(calendar.getSelectedDate());

        for (int i = 1; i <= repeatCount; i++) {
            date += week;
            timetable.addTask(task, TimeUtils.getDate(date*1000));
        }

    }

    @Override
    public void onTask(Task task) {
        getView().showInfoTaskDialog(task);
    }

    @Override
    public void onSaveTaskDescription(Task task, String description) {
        task.setDescription(description);
    }

    @Override
    public void onRemoveTask(Task task) {
        getView().dismissInfoTaskDialog();
        timetable.removeTask(task);
        updateCalendarView();
    }

    @Override
    public void saveState() {
        timetable.saveState();
    }
}
