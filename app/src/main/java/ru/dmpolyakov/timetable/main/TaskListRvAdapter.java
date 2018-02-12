package ru.dmpolyakov.timetable.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dmpolyakov.timetable.R;

public class TaskListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;

    private ArrayList<String> tasks = new ArrayList<>();
    private MainContract.Presenter presenter;

    void changeData(ArrayList<String> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    TaskListRvAdapter(MainContract.Presenter presenter, ArrayList<String> tasks) {
        this.presenter = presenter;
        this.tasks = tasks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
                return new TaskListRvAdapter.ItemViewHolder(view);
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list_footer, parent, false);
                return new TaskListRvAdapter.FooterViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskListRvAdapter.ItemViewHolder) {
            TaskListRvAdapter.ItemViewHolder itemHolder = ((TaskListRvAdapter.ItemViewHolder) holder);
            String item = tasks.get(position);
            itemHolder.title.setText(item);
            itemHolder.title.setOnClickListener(view -> presenter.onSetFilter(item));
            itemHolder.icRemove.setOnClickListener(view -> presenter.onRemoveTaskFromDatabase(item));
        } else if (holder instanceof TaskListRvAdapter.FooterViewHolder) {
            TaskListRvAdapter.FooterViewHolder footerHolder = ((TaskListRvAdapter.FooterViewHolder) holder);
            footerHolder.icAdd.setOnClickListener(view -> {
                presenter.onAddTaskToDatabase(footerHolder.title.getText().toString());
                footerHolder.title.setText("");
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
        @BindView(R.id.remove)
        View icRemove;
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
        @BindView(R.id.title)
        EditText title;
        @BindView(R.id.add)
        View icAdd;

        FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
