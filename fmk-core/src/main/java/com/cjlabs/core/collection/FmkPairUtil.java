package com.cjlabs.core.collection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 二元组工具类，提供处理二元组的实用方法
 */
public class FmkPairUtil {

    /**
     * 创建一个不可变的二元组
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param right 右值
     * @return 不可变的二元组
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return ImmutablePair.of(left, right);
    }

    /**
     * 创建一个可变的二元组
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param right 右值
     * @return 可变的二元组
     */
    public static <L, R> MutablePair<L, R> ofMutable(L left, R right) {
        return MutablePair.of(left, right);
    }

    /**
     * 从二元组中获取左值
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair 二元组
     * @return 左值，如果二元组为null则返回null
     */
    public static <L, R> L getLeft(Pair<L, R> pair) {
        return pair != null ? pair.getLeft() : null;
    }

    /**
     * 从二元组中获取右值
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair 二元组
     * @return 右值，如果二元组为null则返回null
     */
    public static <L, R> R getRight(Pair<L, R> pair) {
        return pair != null ? pair.getRight() : null;
    }

    /**
     * 创建一个新的二元组，交换左值和右值
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair 原二元组
     * @return 交换左值和右值后的新二元组
     */
    public static <L, R> Pair<R, L> swap(Pair<L, R> pair) {
        return pair != null ? Pair.of(pair.getRight(), pair.getLeft()) : null;
    }

    /**
     * 比较两个二元组是否相等
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair1 第一个二元组
     * @param pair2 第二个二元组
     * @return 如果两个二元组相等则返回true
     */
    public static <L, R> boolean equals(Pair<L, R> pair1, Pair<L, R> pair2) {
        if (pair1 == pair2) {
            return true;
        }
        if (pair1 == null || pair2 == null) {
            return false;
        }
        return pair1.equals(pair2);
    }

    /**
     * 从可能为null的值创建二元组，如果两个值都为null则返回null
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param right 右值
     * @return 二元组，如果两个值都为null则返回null
     */
    public static <L, R> Pair<L, R> ofNullable(L left, R right) {
        return (left == null && right == null) ? null : Pair.of(left, right);
    }

    /**
     * 将二元组转换为字符串，格式为"(left,right)"
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair 二元组
     * @return 二元组的字符串表示
     */
    public static <L, R> String toString(Pair<L, R> pair) {
        return pair != null ? pair.toString() : "null";
    }

    /**
     * 将Map.Entry转换为二元组
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param entry Map条目
     * @return 包含条目键和值的二元组
     */
    public static <K, V> Pair<K, V> fromEntry(Map.Entry<K, V> entry) {
        return entry != null ? Pair.of(entry.getKey(), entry.getValue()) : null;
    }

    /**
     * 将二元组转换为Map.Entry
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pair 二元组
     * @return 包含二元组左值作为键和右值作为值的Map.Entry
     */
    public static <L, R> Map.Entry<L, R> toEntry(Pair<L, R> pair) {
        return pair != null ? Map.entry(pair.getLeft(), pair.getRight()) : null;
    }

    /**
     * 将Map转换为二元组列表
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param map 要转换的Map
     * @return 包含Map中每个键值对的二元组列表
     */
    public static <K, V> List<Pair<K, V>> fromMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }
        return map.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 将二元组列表转换为Map
     *
     * @param <L> 左值类型（Map键类型）
     * @param <R> 右值类型（Map值类型）
     * @param pairs 二元组列表
     * @return 以二元组左值为键、右值为值的Map
     */
    public static <L, R> Map<L, R> toMap(List<Pair<L, R>> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            return Map.of();
        }
        return pairs.stream()
                .filter(pair -> pair != null && pair.getLeft() != null)
                .collect(Collectors.toMap(
                        Pair::getLeft,
                        Pair::getRight,
                        (v1, v2) -> v1 // 如果有重复键，保留第一个值
                ));
    }

    /**
     * 创建一个二元组，其中左右元素都是相同类型的相同值
     *
     * @param <T> 值类型
     * @param value 左右位置使用的值
     * @return 包含相同值的二元组
     */
    public static <T> Pair<T, T> pairOf(T value) {
        return Pair.of(value, value);
    }

    /**
     * 转换二元组的左值
     *
     * @param <L> 原左值类型
     * @param <R> 右值类型
     * @param <T> 新左值类型
     * @param pair 原二元组
     * @param leftMapper 左值转换函数
     * @return 转换左值后的新二元组
     */
    public static <L, R, T> Pair<T, R> mapLeft(Pair<L, R> pair, Function<L, T> leftMapper) {
        if (pair == null || leftMapper == null) {
            return null;
        }
        return Pair.of(leftMapper.apply(pair.getLeft()), pair.getRight());
    }

    /**
     * 转换二元组的右值
     *
     * @param <L> 左值类型
     * @param <R> 原右值类型
     * @param <T> 新右值类型
     * @param pair 原二元组
     * @param rightMapper 右值转换函数
     * @return 转换右值后的新二元组
     */
    public static <L, R, T> Pair<L, T> mapRight(Pair<L, R> pair, Function<R, T> rightMapper) {
        if (pair == null || rightMapper == null) {
            return null;
        }
        return Pair.of(pair.getLeft(), rightMapper.apply(pair.getRight()));
    }

    /**
     * 同时转换二元组的左值和右值
     *
     * @param <L> 原左值类型
     * @param <R> 原右值类型
     * @param <NL> 新左值类型
     * @param <NR> 新右值类型
     * @param pair 原二元组
     * @param leftMapper 左值转换函数
     * @param rightMapper 右值转换函数
     * @return 转换左值和右值后的新二元组
     */
    public static <L, R, NL, NR> Pair<NL, NR> map(
            Pair<L, R> pair,
            Function<L, NL> leftMapper,
            Function<R, NR> rightMapper) {
        if (pair == null || leftMapper == null || rightMapper == null) {
            return null;
        }
        return Pair.of(
                leftMapper.apply(pair.getLeft()),
                rightMapper.apply(pair.getRight())
        );
    }

    /**
     * 过滤二元组列表
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pairs 二元组列表
     * @param predicate 过滤条件
     * @return 过滤后的二元组列表
     */
    public static <L, R> List<Pair<L, R>> filter(
            List<Pair<L, R>> pairs,
            java.util.function.Predicate<Pair<L, R>> predicate) {
        if (pairs == null || pairs.isEmpty() || predicate == null) {
            return new ArrayList<>();
        }
        return pairs.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 将二元组列表转换为左值列表
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pairs 二元组列表
     * @return 左值列表
     */
    public static <L, R> List<L> leftList(List<Pair<L, R>> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            return new ArrayList<>();
        }
        return pairs.stream()
                .map(Pair::getLeft)
                .collect(Collectors.toList());
    }

    /**
     * 将二元组列表转换为右值列表
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param pairs 二元组列表
     * @return 右值列表
     */
    public static <L, R> List<R> rightList(List<Pair<L, R>> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            return new ArrayList<>();
        }
        return pairs.stream()
                .map(Pair::getRight)
                .collect(Collectors.toList());
    }
}