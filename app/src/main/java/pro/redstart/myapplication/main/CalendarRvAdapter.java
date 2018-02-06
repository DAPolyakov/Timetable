package pro.redstart.myapplication.main;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.redstart.myapplication.R;
import pro.redstart.myapplication.models.CalendarDay;

public class CalendarRvAdapter extends RecyclerView.Adapter<CalendarRvAdapter.ViewHolder> {

    private ArrayList<CalendarDay> days = new ArrayList<>();
    private MainContract.Presenter presenter;
    private int selected = -7;

    void changeData(ArrayList<CalendarDay> days) {
        this.days = days;
        this.selected = -7;
        notifyDataSetChanged();
    }

    void setSelected(int day) {
        selected = day;
        notifyDataSetChanged();
    }

    CalendarRvAdapter(MainContract.Presenter presenter, ArrayList<CalendarDay> days) {
        this.presenter = presenter;
        this.days = days;
    }

    @Override
    public CalendarRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new CalendarRvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarRvAdapter.ViewHolder holder, int position) {
        CalendarDay item = days.get(position);

        if (item.isActive()) {
            holder.background.setVisibility(View.VISIBLE);
            holder.number.setText(String.valueOf(item.getNumber()));

            switch (item.getLoad()) {
                case 0:
                    holder.background.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.stage_0));
                    break;
                case 1:
                    holder.background.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.stage_1));
                    break;
                case 2:
                    holder.background.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.stage_2));
                    break;
                case 3:
                    holder.background.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.stage_3));
                    break;
                default:
                    holder.background.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.stage_4));
                    break;
            }
        } else {
            holder.background.setVisibility(View.INVISIBLE);
        }

        if (item.getNumber() == selected) {
            holder.border.setBackgroundResource(R.drawable.border_selected);
        } else {
            holder.border.setBackgroundResource(R.drawable.border_default);
        }

        holder.background.setOnClickListener(view -> {
            presenter.onDayClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.background)
        View background;
        @BindView(R.id.border)
        View border;
        @BindView(R.id.hidden)
        View hidden;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}