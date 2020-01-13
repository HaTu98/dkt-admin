package vn.edu.vnu.uet.dktadmin.common.utilities;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static <T> boolean isNullOrEmpty(List<T> list) {
        if (list == null || list.isEmpty())
            return true;

        return false;
    }

    public static <T> String joinList(List<T> list, String delimiter) {
        if (isNullOrEmpty(list))
            return "";
        if (delimiter == null) {
            delimiter = ",";
        }
        return list.stream().map(T::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> String joinList(List<T> list) {
        return joinList(list, ",");
    }

    public static <T> boolean isBlank(List<T> list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }
}
