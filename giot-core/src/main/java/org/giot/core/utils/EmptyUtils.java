package org.giot.core.utils;

import java.util.Collection;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 判空工具类
 *
 * @author yuanguohua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyUtils {

    /**
     * 判断集合是否为空 coll->null->true coll-> coll.size() == 0 -> true
     */
    public static <T> boolean isEmpty(Collection<T> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不为空
     */
    public static <T> boolean isNotEmpty(Collection<T> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断map是否为空
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断map是否不为空
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * 判断一个对象是否为空
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        }
        if (t.toString() == null || "".equals(t.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否不为空
     */
    public static <T> boolean isNotEmpty(T[] datas) {
        return !isEmpty(datas);
    }

    /**
     * 判断数组是否不为空
     */
    public static <T> boolean isEmpty(T[] datas) {
        return datas == null || datas.length == 0;
    }

    /**
     * 判断一个对象是否不为空
     */
    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

}
