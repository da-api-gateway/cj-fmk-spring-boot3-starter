package com.cjlabs.core.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合工具类，提供处理集合的实用方法。
 * 该类利用了Apache Commons Collections4、Guava和Apache Commons Lang3的功能。
 */
public class FmkCollectionUtil {

    /**
     * 检查集合是否为空（null或大小为0）。
     *
     * @param collection 要检查的集合
     * @return 如果集合为空或null，则返回true
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 检查集合是否非空（不为null且大小大于0）。
     *
     * @param collection 要检查的集合
     * @return 如果集合非空，则返回true
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }

    /**
     * 如果输入为null，则返回空的不可变列表，否则返回输入列表。
     *
     * @param <T>  列表中元素的类型
     * @param list 要检查的列表
     * @return 输入列表或空的不可变列表（如果输入为null）
     */
    public static <T> List<T> emptyIfNull(List<T> list) {
        return ListUtils.emptyIfNull(list);
    }

    /**
     * 从给定元素创建不可变列表。
     *
     * @param <T>      列表中元素的类型
     * @param elements 要包含在列表中的元素
     * @return 包含指定元素的不可变列表
     */
    @SafeVarargs
    public static <T> List<T> immutableList(T... elements) {
        return ImmutableList.copyOf(elements);
    }

    /**
     * 将列表分割成指定大小的子列表。
     *
     * @param <T>  列表中元素的类型
     * @param list 要分割的列表
     * @param size 每个分区的大小
     * @return 子列表的列表
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }
        return Lists.partition(list, size);
    }

    /**
     * 使用谓词过滤集合。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要过滤的集合
     * @param predicate  要应用的谓词
     * @return 仅包含满足谓词的元素的新集合
     */
    public static <T> Collection<T> filter(Collection<T> collection, java.util.function.Predicate<? super T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 使用转换函数转换集合。
     *
     * @param <I>        输入类型
     * @param <O>        输出类型
     * @param collection 输入集合
     * @param function   要应用的转换函数
     * @return 具有转换元素的新集合
     */
    public static <I, O> Collection<O> transform(Collection<I> collection, Function<? super I, ? extends O> function) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().map(function).collect(Collectors.toList());
    }

    /**
     * 将集合转换为列表。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要转换的集合
     * @return 包含集合中所有元素的新ArrayList
     */
    public static <T> List<T> toList(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(collection);
    }

    /**
     * 将集合转换为集合。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要转换的集合
     * @return 包含集合中所有元素的新HashSet
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new HashSet<>();
        }
        return new HashSet<>(collection);
    }

    /**
     * 计算两个集合的交集。
     *
     * @param <T> 集合中元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含两个集合共有元素的新集合
     */
    public static <T> Collection<T> intersection(Collection<T> a, Collection<T> b) {
        return CollectionUtils.intersection(a, b);
    }

    /**
     * 计算两个集合的并集。
     *
     * @param <T> 集合中元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含任一集合中元素的新集合
     */
    public static <T> Collection<T> union(Collection<T> a, Collection<T> b) {
        return CollectionUtils.union(a, b);
    }

    /**
     * 计算两个集合的差集（a中有但b中没有的元素）。
     *
     * @param <T> 集合中元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含在a中但不在b中的元素的新集合
     */
    public static <T> Collection<T> subtract(Collection<T> a, Collection<T> b) {
        return CollectionUtils.subtract(a, b);
    }

    /**
     * 将数组转换为列表。
     *
     * @param <T>   数组中元素的类型
     * @param array 要转换的数组
     * @return 包含数组中所有元素的新ArrayList
     */
    public static <T> List<T> arrayToList(T[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * 将列表转换为数组。
     *
     * @param <T>  列表中元素的类型
     * @param list 要转换的列表
     * @param type 数组元素的类型
     * @return 包含列表中所有元素的新数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] listToArray(List<T> list, Class<T> type) {
        if (isEmpty(list)) {
            return (T[]) Array.newInstance(type, 0);
        }
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    /**
     * 根据键函数将集合转换为映射。
     *
     * @param <K>        映射中键的类型
     * @param <V>        映射中值的类型
     * @param collection 要转换的集合
     * @param keyMapper  从值提取键的函数
     * @return 新的映射，其中键由keyMapper函数确定，值为集合中的元素
     */
    public static <K, V> Map<K, V> toMap(Collection<V> collection, Function<V, K> keyMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(keyMapper, Function.identity(), (v1, v2) -> v1));
    }

    /**
     * 检查两个集合是否有共同元素。
     *
     * @param <T> 集合中元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 如果集合有至少一个共同元素，则返回true
     */
    public static <T> boolean containsAny(Collection<T> a, Collection<T> b) {
        return CollectionUtils.containsAny(a, b);
    }

    /**
     * 检查集合是否包含所有指定的元素。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要检查的集合
     * @param elements   要检查是否存在的元素
     * @return 如果集合包含所有指定元素，则返回true
     */
    @SafeVarargs
    public static <T> boolean containsAll(Collection<T> collection, T... elements) {
        if (isEmpty(collection) || ArrayUtils.isEmpty(elements)) {
            return false;
        }
        return collection.containsAll(Arrays.asList(elements));
    }

    /**
     * 从集合中移除null元素。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要处理的集合
     * @return 不包含null元素的新集合
     */
    public static <T> Collection<T> removeNulls(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 从集合中获取第一个元素，如果集合为空则返回null。
     *
     * @param <T>        集合中元素的类型
     * @param collection 要处理的集合
     * @return 集合的第一个元素，如果集合为空则为null
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }
}