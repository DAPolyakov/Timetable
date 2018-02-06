package ru.dmpolyakov.timetable.main;


import java.util.ArrayList;

import ru.dmpolyakov.timetable.common.MvpPresenter;
import ru.dmpolyakov.timetable.common.MvpView;
import ru.dmpolyakov.timetable.models.CalendarDay;
import ru.dmpolyakov.timetable.models.Task;

public interface MainContract {

    interface View extends MvpView {
        void showDays(ArrayList<CalendarDay> days);
        void setDate(String date);
        void setSelectedDay(int day);

        void showTasks(ArrayList<Task> tasks);
        void showNewTaskDialog(ArrayList<String> cache);
        void clearNewTaskDialog();
        void dismissNewTaskDialog();

        void showInfoTaskDialog(Task task);
        void dismissInfoTaskDialog();
    }

    interface Presenter extends MvpPresenter<View> {
        void onPreviousMouth();
        void onNextMouth();
        void onDayClick(CalendarDay day);

        void onAddTask();
        void onTaskAdded(String title, int stHour, int stMinute, int fnHour, int fnMinute, boolean repeat);
        void onTask(Task task);

        void onSaveTaskDescription(Task task, String description);
        void onRemoveTask(Task task);

        void saveState();
    }
}
