package ru.dmpolyakov.timetable.main;


import android.content.SharedPreferences;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ru.dmpolyakov.timetable.common.utils.ConvertUtils;
import ru.dmpolyakov.timetable.models.Task;

class TimetableModel {

    private final Moshi moshi = new Moshi.Builder().build();
    private final Type TYPE_LIST_TASK = Types.newParameterizedType(List.class, Task.class);
    private final Type TYPE_LIST_LAST_TASK = Types.newParameterizedType(List.class, String.class);
    private final String KEY_PREFIX = "date+";
    private final String KEY_LAST_TASK = "last_task";

    private SharedPreferences preferences;

    private ArrayList<Task> selectedTask = new ArrayList<>();
    private String selectedDate = "";
    private String filter = "";

    TimetableModel(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    void addTask(Task task) {
        selectedTask.add(task);
        Collections.sort(
                selectedTask,
                (a, b) -> (a.getStHour() * 60 + a.getStMinute() - b.getStHour() * 60 - b.getStMinute()));
        saveTasks(selectedTask, selectedDate);
    }

    void addTask(Task task, String date) {
        ArrayList<Task> tasks = loadTasks(date);
        tasks.add(task);
        Collections.sort(
                tasks,
                (a, b) -> (a.getStHour() * 60 + a.getStMinute() - b.getStHour() * 60 - b.getStMinute()));
        saveTasks(tasks, date);
    }

    void removeTask(Task task) {
        selectedTask.remove(task);
        saveTasks(selectedTask, selectedDate);
    }

    ArrayList<Task> getTasks(String date) {
        selectedTask = loadTasks(date);
        selectedDate = date;

        if (Objects.equals(filter, "")) {
            return selectedTask;
        } else {
            return filterTask(selectedTask);
        }
    }

    private ArrayList<Task> filterTask(ArrayList<Task> selectedTask){
        ArrayList<Task> filtered = new ArrayList<>();

        for (Task task: selectedTask){
            if (Objects.equals(task.getTitle().toLowerCase(), filter)){
                filtered.add(task);
            }
        }
        return filtered;
    }

    int getCountTask(String date) {
        if (Objects.equals(filter, "")) {
            return loadTasks(date).size();
        } else {
            return filterTask(loadTasks(date)).size();
        }
    }

    private ArrayList<Task> loadTasks(String date) {
        String key = KEY_PREFIX + date;
        JsonAdapter<List<Task>> jsonAdapter = moshi.adapter(TYPE_LIST_TASK);
        List<Task> tasks = Collections.emptyList();
        String json = loadString(key);
        try {
            tasks = jsonAdapter.fromJson(json);
        } catch (IOException ignored) {
        }

        return ConvertUtils.convertListToArray(tasks);
    }

    private String loadString(String key) {
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        }
        return "";
    }

    private void saveTasks(List<Task> tasks, String date) {
        String key = KEY_PREFIX + date;
        JsonAdapter<List<Task>> jsonAdapter = moshi.adapter(TYPE_LIST_TASK);
        String json = jsonAdapter.toJson(tasks);
        save(key, json);
    }

    private void save(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    void saveState() {
        saveTasks(selectedTask, selectedDate);
    }

    ArrayList<String> getLastTasks() {
        JsonAdapter<List<String>> jsonAdapter = moshi.adapter(TYPE_LIST_LAST_TASK);
        List<String> tasks = Collections.emptyList();
        String json = loadString(KEY_LAST_TASK);
        try {
            tasks = jsonAdapter.fromJson(json);
        } catch (IOException ignored) {
        }

        return ConvertUtils.convertListToArray(tasks);
    }

    ArrayList<String> addTaskToDatabase(String title) {
        ArrayList<String> tasks = getLastTasks();
        tasks.add(title);

        Collections.sort(tasks, String::compareToIgnoreCase);
        saveDatabaseTask(tasks);
        return tasks;
    }

    ArrayList<String> removeTaskFromDatabase(String title) {
        ArrayList<String> tasks = getLastTasks();

        for (String task : tasks) {
            if (Objects.equals(task, title)) {
                tasks.remove(task);
                break;
            }
        }

        saveDatabaseTask(tasks);
        return tasks;
    }

    private void saveDatabaseTask(ArrayList<String> tasks) {
        JsonAdapter<List<String>> jsonAdapter = moshi.adapter(TYPE_LIST_LAST_TASK);
        String json = jsonAdapter.toJson(tasks);
        save(KEY_LAST_TASK, json);
    }

    void setFilter(String filter) {
        this.filter = filter.toLowerCase();
    }
}