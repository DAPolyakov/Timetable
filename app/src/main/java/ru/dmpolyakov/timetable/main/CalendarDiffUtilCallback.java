package ru.dmpolyakov.timetable.main;


import android.support.v7.util.DiffUtil;

import java.util.List;

import ru.dmpolyakov.timetable.models.CalendarDay;

public class CalendarDiffUtilCallback extends DiffUtil.Callback {

    private final List<CalendarDay> oldList;
    private final List<CalendarDay> newList;

    public CalendarDiffUtilCallback(List<CalendarDay> oldList, List<CalendarDay> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CalendarDay oldItem = oldList.get(oldItemPosition);
        CalendarDay newItem = newList.get(newItemPosition);
        return oldItem.getNumber() == newItem.getNumber();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CalendarDay oldItem = oldList.get(oldItemPosition);
        CalendarDay newItem = newList.get(newItemPosition);
        return oldItem.isSelected() == newItem.isSelected()
                && oldItem.getLoad() == newItem.getLoad()
                && oldItem.isActive() == newItem.isActive();
    }

}
