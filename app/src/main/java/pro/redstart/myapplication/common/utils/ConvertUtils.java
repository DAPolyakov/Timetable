package pro.redstart.myapplication.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {

    public static <T> ArrayList<T> convertListToArray(List<T> list) {
        ArrayList<T> arr = new ArrayList<>();
        if (list != null) {
            arr.addAll(list);
        }
        return arr;
    }

}
