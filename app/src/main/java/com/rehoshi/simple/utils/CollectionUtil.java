package com.rehoshi.simple.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hoshino on 2018/12/5.
 */

public class CollectionUtil {

    public interface Custom<E> {
        void custom(E e);
    }

    public interface Custom1<E> {
        void custom(int position, E e);
    }

    public interface Mapper<T, R> {
        R map(T e);
    }

    public interface Filter<T> {
        boolean filter(T e);
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> void foreach(Collection<E> collection, Custom<E> custom) {
        if (collection != null) {
            for (E e : collection) {
                custom.custom(e);
            }
        }
    }

    public static <T> T first(Collection<T> list) {
        if (list != null) {
            for (T t : list) {
                return t;
            }
        }
        return null;
    }

    public static <T> T findAny(Collection<T> collection) {
        T any = null;
        if (!CollectionUtil.isNullOrEmpty(collection)) {
            Iterator<T> iterator = collection.iterator();
            if (iterator.hasNext()) {
                any = iterator.next();
            }
        }
        return any;
    }

    public static <T, R> List<R> map(Collection<T> collection, Mapper<T, R> mapper) {
        List<R> list = new ArrayList<>();
        if (!CollectionUtil.isNullOrEmpty(collection)) {
            foreach(collection, t -> list.add(mapper.map(t)));
        }
        return list;
    }

    public static <T> int findFirstLoc(List<T> collection, Filter<T> filter) {
        int loc = -1;
        if (!CollectionUtil.isNullOrEmpty(collection)) {
            for (int i = 0; i < collection.size(); i++) {
                T t = collection.get(i);
                if (filter.filter(t)) {
                    loc = i;
                    break;
                }
            }
        }
        return loc;
    }

    public static <T> List<T> filter(Collection<T> allData, Filter<T> filter) {
        List<T> list = new ArrayList<>();
        Iterator<T> iterator = allData.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (filter.filter(next)) {
                list.add(next);
            }
        }
        return list;
    }

    public static <T> void remove(Collection<T> collection, Filter<T> filter) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (filter.filter(next)) {
                iterator.remove();
            }
        }
    }


    public static int count(Collection collection) {
        if (collection == null) {
            return 0;
        } else {
            return collection.size();
        }
    }

}
