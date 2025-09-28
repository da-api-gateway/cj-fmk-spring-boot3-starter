package com.cjlabs.core.collection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Set工具类，提供常用的Set操作方法
 */
public class FmkSetUtil {

    /**
     * 检查Set是否为空（null或没有元素）
     *
     * @param set 要检查的Set
     * @return 如果Set为null或空，则返回true
     */
    public static boolean isEmpty(Set<?> set) {
        return CollectionUtils.isEmpty(set);
    }

    /**
     * 检查Set是否非空（不为null且有元素）
     *
     * @param set 要检查的Set
     * @return 如果Set不为null且包含至少一个元素，则返回true
     */
    public static boolean isNotEmpty(Set<?> set) {
        return CollectionUtils.isNotEmpty(set);
    }

    /**
     * 如果Set为null，则返回空Set，否则返回原Set
     *
     * @param <T> Set元素的类型
     * @param set 要检查的Set
     * @return 原Set或空Set（如果原Set为null）
     */
    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return SetUtils.emptyIfNull(set);
    }

    /**
     * 创建一个不可变Set
     *
     * @param <T>      Set元素的类型
     * @param elements 要包含在Set中的元素
     * @return 包含指定元素的不可变Set
     */
    @SafeVarargs
    public static <T> Set<T> of(T... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return Collections.emptySet();
        }
        return ImmutableSet.copyOf(elements);
    }

    /**
     * 创建一个HashSet
     *
     * @param <T> Set元素的类型
     * @return 空的HashSet
     */
    public static <T> HashSet<T> newHashSet() {
        return new HashSet<>();
    }

    /**
     * 创建一个包含指定元素的HashSet
     *
     * @param <T>      Set元素的类型
     * @param elements 要包含在Set中的元素
     * @return 包含指定元素的HashSet
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return newHashSet();
        }
        return Sets.newHashSet(elements);
    }

    /**
     * 创建一个具有指定初始容量的HashSet
     *
     * @param <T>             Set元素的类型
     * @param initialCapacity 初始容量
     * @return 具有指定初始容量的空HashSet
     */
    public static <T> HashSet<T> newHashSetWithCapacity(int initialCapacity) {
        return Sets.newHashSetWithExpectedSize(initialCapacity);
    }

    /**
     * 创建一个LinkedHashSet
     *
     * @param <T> Set元素的类型
     * @return 空的LinkedHashSet
     */
    public static <T> LinkedHashSet<T> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    /**
     * 创建一个包含指定元素的LinkedHashSet
     *
     * @param <T>      Set元素的类型
     * @param elements 要包含在Set中的元素
     * @return 包含指定元素的LinkedHashSet
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return newLinkedHashSet();
        }
        return Sets.newLinkedHashSet(Arrays.asList(elements));
    }

    /**
     * 创建一个TreeSet
     *
     * @param <T> Set元素的类型，必须实现Comparable接口
     * @return 空的TreeSet
     */
    public static <T extends Comparable<? super T>> TreeSet<T> newTreeSet() {
        return new TreeSet<>();
    }

    /**
     * 创建一个包含指定元素的TreeSet
     *
     * @param <T>      Set元素的类型，必须实现Comparable接口
     * @param elements 要包含在Set中的元素
     * @return 包含指定元素的TreeSet
     */
    @SafeVarargs
    public static <T extends Comparable<? super T>> TreeSet<T> newTreeSet(T... elements) {
        TreeSet<T> set = newTreeSet();
        if (ArrayUtils.isNotEmpty(elements)) {
            Collections.addAll(set, elements);
        }
        return set;
    }

    /**
     * 计算两个集合的并集
     *
     * @param <T> 集合元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含两个集合所有元素的新Set
     */
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        if (isEmpty(a)) {
            return emptyIfNull(b);
        }
        if (isEmpty(b)) {
            return emptyIfNull(a);
        }
        return Sets.union(a, b);
    }

    /**
     * 计算两个集合的交集
     *
     * @param <T> 集合元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含两个集合共有元素的新Set
     */
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        if (isEmpty(a) || isEmpty(b)) {
            return Collections.emptySet();
        }
        return Sets.intersection(a, b);
    }

    /**
     * 计算两个集合的差集（a中有但b中没有的元素）
     *
     * @param <T> 集合元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 包含在a中但不在b中的元素的新Set
     */
    public static <T> Set<T> difference(Set<T> a, Set<T> b) {
        if (isEmpty(a)) {
            return Collections.emptySet();
        }
        if (isEmpty(b)) {
            return new HashSet<>(a);
        }
        return Sets.difference(a, b);
    }

    /**
     * 计算两个集合的对称差集（只在其中一个集合中出现的元素）
     *
     * @param <T> 集合元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 只在其中一个集合中出现的元素的新Set
     */
    public static <T> Set<T> symmetricDifference(Set<T> a, Set<T> b) {
        if (isEmpty(a)) {
            return emptyIfNull(b);
        }
        if (isEmpty(b)) {
            return new HashSet<>(a);
        }
        return Sets.symmetricDifference(a, b);
    }

    /**
     * 检查一个集合是否是另一个集合的子集
     *
     * @param <T>      集合元素的类型
     * @param subset   可能的子集
     * @param superset 可能的超集
     * @return 如果subset是superset的子集，则返回true
     */
    public static <T> boolean isSubset(Set<T> subset, Set<T> superset) {
        if (isEmpty(subset)) {
            return true;
        }
        if (isEmpty(superset)) {
            return false;
        }
        return superset.containsAll(subset);
    }

    /**
     * 检查两个集合是否有共同元素
     *
     * @param <T> 集合元素的类型
     * @param a   第一个集合
     * @param b   第二个集合
     * @return 如果集合有至少一个共同元素，则返回true
     */
    public static <T> boolean containsAny(Set<T> a, Collection<T> b) {
        return CollectionUtils.containsAny(a, b);
    }

    /**
     * 将集合转换为Set
     *
     * @param <T>        集合元素的类型
     * @param collection 要转换的集合
     * @return 包含集合中所有元素的新HashSet
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashSet<>();
        }
        return new HashSet<>(collection);
    }

    /**
     * 将数组转换为Set
     *
     * @param <T>   数组元素的类型
     * @param array 要转换的数组
     * @return 包含数组中所有元素的新HashSet
     */
    @SafeVarargs
    public static <T> Set<T> toSet(T... array) {
        if (ArrayUtils.isEmpty(array)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * 将Set转换为List
     *
     * @param <T> Set元素的类型
     * @param set 要转换的Set
     * @return 包含Set中所有元素的新ArrayList
     */
    public static <T> List<T> toList(Set<T> set) {
        if (isEmpty(set)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(set);
    }

    /**
     * 根据谓词过滤Set
     *
     * @param <T>       Set元素的类型
     * @param set       要过滤的Set
     * @param predicate 过滤条件
     * @return 过滤后的新Set
     */
    public static <T> Set<T> filter(Set<T> set, Predicate<? super T> predicate) {
        if (isEmpty(set) || predicate == null) {
            return Collections.emptySet();
        }
        return set.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    /**
     * 转换Set中的元素
     *
     * @param <T>    原Set元素的类型
     * @param <R>    新Set元素的类型
     * @param set    要转换的Set
     * @param mapper 转换函数
     * @return 转换后的新Set
     */
    public static <T, R> Set<R> map(Set<T> set, Function<? super T, ? extends R> mapper) {
        if (isEmpty(set) || mapper == null) {
            return Collections.emptySet();
        }
        return set.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    /**
     * 从Set中移除null元素
     *
     * @param <T> Set元素的类型
     * @param set 要处理的Set
     * @return 不包含null元素的新Set
     */
    public static <T> Set<T> removeNulls(Set<T> set) {
        if (isEmpty(set)) {
            return Collections.emptySet();
        }
        return set.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 获取Set的第一个元素，如果Set为空则返回null
     *
     * @param <T> Set元素的类型
     * @param set 要处理的Set
     * @return Set的第一个元素，如果Set为空则为null
     */
    public static <T> T getFirst(Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        return set.iterator().next();
    }

    /**
     * 创建一个包含指定范围内整数的Set
     *
     * @param start 范围的起始值（包含）
     * @param end   范围的结束值（包含）
     * @return 包含指定范围内所有整数的Set
     */
    public static Set<Integer> range(int start, int end) {
        if (start > end) {
            return Collections.emptySet();
        }
        Set<Integer> result = new HashSet<>();
        for (int i = start; i <= end; i++) {
            result.add(i);
        }
        return result;
    }

    /**
     * 创建所有可能的子集
     *
     * @param <T> Set元素的类型
     * @param set 要处理的Set
     * @return 包含所有可能子集的Set
     */
    public static <T> Set<Set<T>> powerSet(Set<T> set) {
        if (isEmpty(set)) {
            Set<Set<T>> result = new HashSet<>();
            result.add(Collections.emptySet());
            return result;
        }
        return Sets.powerSet(set);
    }

    /**
     * 创建所有可能的笛卡尔积
     *
     * @param <B>  第一个Set元素的类型
     * @param <T>  第二个Set元素的类型
     * @param set1 第一个Set
     * @param set2 第二个Set
     * @return 两个Set的笛卡尔积
     */
    public static <B, T> Set<List<B>> cartesianProduct(Set<? extends B> set1, Set<? extends B> set2) {
        if (isEmpty(set1) || isEmpty(set2)) {
            return Collections.emptySet();
        }
        return Sets.cartesianProduct(set1, set2);
    }

    /**
     * 检查Set是否包含所有指定的元素
     *
     * @param <T>      Set元素的类型
     * @param set      要检查的Set
     * @param elements 要检查是否存在的元素
     * @return 如果Set包含所有指定元素，则返回true
     */
    @SafeVarargs
    public static <T> boolean containsAll(Set<T> set, T... elements) {
        if (isEmpty(set) || ArrayUtils.isEmpty(elements)) {
            return false;
        }
        return set.containsAll(Arrays.asList(elements));
    }

    /**
     * 检查Set是否包含任一指定的元素
     *
     * @param <T>      Set元素的类型
     * @param set      要检查的Set
     * @param elements 要检查是否存在的元素
     * @return 如果Set包含任一指定元素，则返回true
     */
    @SafeVarargs
    public static <T> boolean containsAny(Set<T> set, T... elements) {
        if (isEmpty(set) || ArrayUtils.isEmpty(elements)) {
            return false;
        }
        for (T element : elements) {
            if (set.contains(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将多个Set合并为一个
     *
     * @param <T>  Set元素的类型
     * @param sets 要合并的Set集合
     * @return 合并后的新Set
     */
    @SafeVarargs
    public static <T> Set<T> union(Set<T>... sets) {
        if (ArrayUtils.isEmpty(sets)) {
            return Collections.emptySet();
        }

        Set<T> result = new HashSet<>();
        for (Set<T> set : sets) {
            if (isNotEmpty(set)) {
                result.addAll(set);
            }
        }
        return result;
    }
}