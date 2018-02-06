package ru.dmpolyakov.timetable.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dmpolyakov.timetable.R;
import ru.dmpolyakov.timetable.models.Task;

public class TaskRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;

    private ArrayList<Task> tasks = new ArrayList<>();
    private MainContract.Presenter presenter;

    void changeData(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    TaskRvAdapter(MainContract.Presenter presenter, ArrayList<Task> tasks) {
        this.presenter = presenter;
        this.tasks = tasks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
                return new ItemViewHolder(view);
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks_footer, parent, false);
                return new FooterViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = ((ItemViewHolder) holder);
            Task item = tasks.get(position);
            itemHolder.title.setText(item.getTitle());
            itemHolder.time.setText(item.getFullTime());
            itemHolder.view.setOnClickListener(view -> presenter.onTask(item));
            itemHolder.icAnnouncement.setVisibility(item.getDescription() == null || item.getDescription().length() == 0 ? View.GONE : View.VISIBLE);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = ((FooterViewHolder) holder);
            footerHolder.footer.setOnClickListener(view -> {
                presenter.onAddTask();
            });
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == tasks.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.announcement)
        View icAnnouncement;
        View view;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.footer)
        View footer;

        FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}