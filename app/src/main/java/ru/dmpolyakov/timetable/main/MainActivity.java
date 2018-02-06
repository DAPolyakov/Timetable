package ru.dmpolyakov.timetable.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dmpolyakov.timetable.R;
import ru.dmpolyakov.timetable.models.CalendarDay;
import ru.dmpolyakov.timetable.models.Task;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.rv_calendar)
    RecyclerView rvCalendar;
    @BindView(R.id.rv_task)
    RecyclerView rvTask;
    @BindView(R.id.previous_mouth)
    TextView previous;
    @BindView(R.id.next_mouth)
    TextView next;
    @BindView(R.id.mouthYear)
    TextView date;

    private MainContract.Presenter presenter;
    private SharedPreferences preferences;
    private AlertDialog newTaskDialog;
    private AlertDialog infoTaskDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE);

        presenter = new MainPresenter(preferences);
        presenter.attachView(this);

        initRv();
        createNewTaskDialog();
        createInfoTaskDialog();

        previous.setOnClickListener(view -> presenter.onPreviousMouth());
        next.setOnClickListener(view -> presenter.onNextMouth());

        presenter.viewIsReady();
    }

    private void initRv() {
        GridLayoutManager manager = new GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false);
        rvCalendar.setLayoutManager(manager);
        rvCalendar.setAdapter(new CalendarRvAdapter(presenter, new ArrayList<>()));

        rvTask.setLayoutManager(new LinearLayoutManager(this));
        rvTask.setAdapter(new TaskRvAdapter(presenter, new ArrayList<>()));
    }

    @Override
    public void showDays(ArrayList<CalendarDay> days) {
        ((CalendarRvAdapter) rvCalendar.getAdapter()).changeData(days);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (isFinishing()) {
            presenter.destroy();
        }
    }

    @Override
    public void setDate(String date) {
        this.date.setText(date);
    }

    @Override
    public void setSelectedDay(int day) {
        ((CalendarRvAdapter) rvCalendar.getAdapter()).setSelected(day);
    }

    @Override
    public void showTasks(ArrayList<Task> tasks) {
        ((TaskRvAdapter) rvTask.getAdapter()).changeData(tasks);
    }

    @Override
    public void showNewTaskDialog() {
        newTaskDialog.show();

        EditText tvStHour = newTaskDialog.findViewById(R.id.startHour);
        EditText tvStartMinutes = newTaskDialog.findViewById(R.id.startMinutes);
        EditText tvFinHour = newTaskDialog.findViewById(R.id.finishHour);
        EditText tvFinMinutes = newTaskDialog.findViewById(R.id.finishMinutes);
        EditText tvTitle = newTaskDialog.findViewById(R.id.title);
        CheckBox repeat = newTaskDialog.findViewById(R.id.isRepeat);
        TextView add = newTaskDialog.findViewById(R.id.add);

        tvTitle.requestFocus();

        add.setOnClickListener(view -> {
            String title = tvTitle.getText().toString();

            int stHour = tvStHour.getText().length() == 0 ? 24 : Integer.parseInt(tvStHour.getText().toString());
            int stMinute = tvStartMinutes.getText().length() == 0 ? 0 : Integer.parseInt(tvStartMinutes.getText().toString());
            int fnHour = tvFinHour.getText().length() == 0 ? 24 : Integer.parseInt(tvFinHour.getText().toString());
            int fnMinute = tvFinMinutes.getText().length() == 0 ? 0 : Integer.parseInt(tvFinMinutes.getText().toString());
            boolean isRepeated = repeat.isChecked();

            presenter.onTaskAdded(title, stHour, stMinute, fnHour, fnMinute, isRepeated);
        });
    }

    @Override
    public void clearNewTaskDialog() {
        if (newTaskDialog.isShowing()) {
            EditText tvStHour = newTaskDialog.findViewById(R.id.startHour);
            EditText tvStartMinutes = newTaskDialog.findViewById(R.id.startMinutes);
            EditText tvFinHour = newTaskDialog.findViewById(R.id.finishHour);
            EditText tvFinMinutes = newTaskDialog.findViewById(R.id.finishMinutes);
            EditText tvTitle = newTaskDialog.findViewById(R.id.title);
            CheckBox repeat = newTaskDialog.findViewById(R.id.isRepeat);

            repeat.setChecked(false);
            tvTitle.setText("");
            tvStHour.setText("");
            tvStartMinutes.setText("");
            tvFinHour.setText("");
            tvFinMinutes.setText("");
        }
    }

    @Override
    public void dismissNewTaskDialog() {
        newTaskDialog.dismiss();
    }

    private void createNewTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_new_task);
        newTaskDialog = builder.create();
    }

    private void createInfoTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_info_task);
        infoTaskDialog = builder.create();
    }

    @Override
    public void showInfoTaskDialog(Task task) {
        infoTaskDialog.show();

        TextView description = infoTaskDialog.findViewById(R.id.description);
        TextView title = infoTaskDialog.findViewById(R.id.title);
        String s = task.getFullTime() + " " + task.getTitle();
        title.setText(s);
        description.setText(task.getDescription());

        TextView remove = infoTaskDialog.findViewById(R.id.remove);
        remove.setOnClickListener(view -> {
            presenter.onRemoveTask(task);
        });

        infoTaskDialog.setOnCancelListener(view -> {
            presenter.onSaveTaskDescription(task, description.getText().toString());
            rvTask.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void dismissInfoTaskDialog() {
        infoTaskDialog.dismiss();
    }
}
