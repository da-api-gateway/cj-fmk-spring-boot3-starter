package com.cjlabs.core.collection;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Triple工具类，提供处理三元组的实用方法
 */
public class FmkTripleUtil {

    /**
     * 创建一个不可变的三元组
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param middle 中值
     * @param right 右值
     * @return 不可变的三元组
     */
    public static <L, M, R> Triple<L, M, R> of(L left, M middle, R right) {
        return ImmutableTriple.of(left, middle, right);
    }

    /**
     * 创建一个可变的三元组
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param middle 中值
     * @param right 右值
     * @return 可变的三元组
     */
    public static <L, M, R> MutableTriple<L, M, R> ofMutable(L left, M middle, R right) {
        return MutableTriple.of(left, middle, right);
    }

    /**
     * 从三元组中获取左值
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 左值
     */
    public static <L, M, R> L getLeft(Triple<L, M, R> triple) {
        return triple != null ? triple.getLeft() : null;
    }

    /**
     * 从三元组中获取中值
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 中值
     */
    public static <L, M, R> M getMiddle(Triple<L, M, R> triple) {
        return triple != null ? triple.getMiddle() : null;
    }

    /**
     * 从三元组中获取右值
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 右值
     */
    public static <L, M, R> R getRight(Triple<L, M, R> triple) {
        return triple != null ? triple.getRight() : null;
    }

    /**
     * 将三元组转换为包含左值和中值的二元组
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 包含左值和中值的二元组
     */
    public static <L, M, R> Pair<L, M> toLeftMiddlePair(Triple<L, M, R> triple) {
        return triple != null ? Pair.of(triple.getLeft(), triple.getMiddle()) : null;
    }

    /**
     * 将三元组转换为包含左值和右值的二元组
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 包含左值和右值的二元组
     */
    public static <L, M, R> Pair<L, R> toLeftRightPair(Triple<L, M, R> triple) {
        return triple != null ? Pair.of(triple.getLeft(), triple.getRight()) : null;
    }

    /**
     * 将三元组转换为包含中值和右值的二元组
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 包含中值和右值的二元组
     */
    public static <L, M, R> Pair<M, R> toMiddleRightPair(Triple<L, M, R> triple) {
        return triple != null ? Pair.of(triple.getMiddle(), triple.getRight()) : null;
    }

    /**
     * 比较两个三元组是否相等
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple1 第一个三元组
     * @param triple2 第二个三元组
     * @return 如果两个三元组相等则返回true
     */
    public static <L, M, R> boolean equals(Triple<L, M, R> triple1, Triple<L, M, R> triple2) {
        if (triple1 == triple2) {
            return true;
        }
        if (triple1 == null || triple2 == null) {
            return false;
        }
        return triple1.equals(triple2);
    }

    /**
     * 从三元组创建一个新的三元组，交换左值和右值
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 原三元组
     * @return 交换左值和右值后的新三元组
     */
    public static <L, M, R> Triple<R, M, L> swapLeftRight(Triple<L, M, R> triple) {
        return triple != null ? Triple.of(triple.getRight(), triple.getMiddle(), triple.getLeft()) : null;
    }

    /**
     * 从三个可能为null的值创建三元组，如果所有值都为null则返回null
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param left 左值
     * @param middle 中值
     * @param right 右值
     * @return 三元组，如果所有值都为null则返回null
     */
    public static <L, M, R> Triple<L, M, R> ofNullable(L left, M middle, R right) {
        return (left == null && middle == null && right == null) ? null : Triple.of(left, middle, right);
    }

    /**
     * 将三元组转换为字符串，格式为"(left,middle,right)"
     *
     * @param <L> 左值类型
     * @param <M> 中值类型
     * @param <R> 右值类型
     * @param triple 三元组
     * @return 三元组的字符串表示
     */
    public static <L, M, R> String toString(Triple<L, M, R> triple) {
        return triple != null ? triple.toString() : "null";
    }

    /**
     * 将二元组扩展为三元组，添加一个中值
     *
     * @param <L> 左值类型
     * @param <R> 右值类型
     * @param <M> 中值类型
     * @param pair 二元组
     * @param middle 要添加的中值
     * @return 包含二元组的左值、提供的中值和二元组的右值的三元组
     */
    public static <L, R, M> Triple<L, M, R> fromPair(Pair<L, R> pair, M middle) {
        return pair != null ? Triple.of(pair.getLeft(), middle, pair.getRight()) : null;
    }

    /**
     * 创建一个三元组，其中所有元素都是相同类型的相同值
     *
     * @param <T> 值类型
     * @param value 所有位置使用的值
     * @return 包含相同值的三元组
     */
    public static <T> Triple<T, T, T> tripleOf(T value) {
        return Triple.of(value, value, value);
    }
}