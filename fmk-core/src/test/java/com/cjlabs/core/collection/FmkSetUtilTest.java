package com.cjlabs.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FmkSetUtil Tests")
class FmkSetUtilTest {

    @Nested
    @DisplayName("isEmpty and isNotEmpty Tests")
    class EmptyChecks {
        @Test
        @DisplayName("isEmpty should return true for null set")
        void isEmptyShouldReturnTrueForNullSet() {
            assertTrue(FmkSetUtil.isEmpty(null));
        }

        @Test
        @DisplayName("isEmpty should return true for empty set")
        void isEmptyShouldReturnTrueForEmptySet() {
            assertTrue(FmkSetUtil.isEmpty(Collections.emptySet()));
        }

        @Test
        @DisplayName("isEmpty should return false for non-empty set")
        void isEmptyShouldReturnFalseForNonEmptySet() {
            assertFalse(FmkSetUtil.isEmpty(Set.of("item")));
        }

        @Test
        @DisplayName("isNotEmpty should return false for null set")
        void isNotEmptyShouldReturnFalseForNullSet() {
            assertFalse(FmkSetUtil.isNotEmpty(null));
        }

        @Test
        @DisplayName("isNotEmpty should return false for empty set")
        void isNotEmptyShouldReturnFalseForEmptySet() {
            assertFalse(FmkSetUtil.isNotEmpty(Collections.emptySet()));
        }

        @Test
        @DisplayName("isNotEmpty should return true for non-empty set")
        void isNotEmptyShouldReturnTrueForNonEmptySet() {
            assertTrue(FmkSetUtil.isNotEmpty(Set.of("item")));
        }
    }

    @Nested
    @DisplayName("emptyIfNull Tests")
    class EmptyIfNullTests {
        @Test
        @DisplayName("emptyIfNull should return empty set for null input")
        void emptyIfNullShouldReturnEmptySetForNullInput() {
            Set<String> result = FmkSetUtil.emptyIfNull(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("emptyIfNull should return original set for non-null input")
        void emptyIfNullShouldReturnOriginalSetForNonNullInput() {
            Set<String> original = Set.of("item1", "item2");
            Set<String> result = FmkSetUtil.emptyIfNull(original);
            assertSame(original, result);
        }
    }

    @Nested
    @DisplayName("Set Creation Tests")
    class SetCreationTests {
        @Test
        @DisplayName("of should create immutable set with given elements")
        void ofShouldCreateImmutableSetWithGivenElements() {
            Set<String> result = FmkSetUtil.of("a", "b", "c");
            assertEquals(Set.of("a", "b", "c"), result);
        }

        @Test
        @DisplayName("of should create empty set when no elements provided")
        void ofShouldCreateEmptySetWhenNoElementsProvided() {
            Set<String> result = FmkSetUtil.of();
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("newHashSet should create empty HashSet")
        void newHashSetShouldCreateEmptyHashSet() {
            Set<String> result = FmkSetUtil.newHashSet();
            assertTrue(result instanceof HashSet);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("newHashSet should create HashSet with given elements")
        void newHashSetShouldCreateHashSetWithGivenElements() {
            Set<String> result = FmkSetUtil.newHashSet("a", "b", "c");
            assertTrue(result instanceof HashSet);
            assertEquals(3, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }

        @Test
        @DisplayName("newHashSetWithCapacity should create HashSet with specified capacity")
        void newHashSetWithCapacityShouldCreateHashSetWithSpecifiedCapacity() {
            Set<String> result = FmkSetUtil.newHashSetWithCapacity(10);
            assertTrue(result instanceof HashSet);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("newLinkedHashSet should create empty LinkedHashSet")
        void newLinkedHashSetShouldCreateEmptyLinkedHashSet() {
            Set<String> result = FmkSetUtil.newLinkedHashSet();
            assertTrue(result instanceof LinkedHashSet);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("newLinkedHashSet should create LinkedHashSet with given elements")
        void newLinkedHashSetShouldCreateLinkedHashSetWithGivenElements() {
            Set<String> result = FmkSetUtil.newLinkedHashSet("a", "b", "c");
            assertTrue(result instanceof LinkedHashSet);
            assertEquals(3, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }

        @Test
        @DisplayName("newTreeSet should create empty TreeSet")
        void newTreeSetShouldCreateEmptyTreeSet() {
            Set<String> result = FmkSetUtil.newTreeSet();
            assertTrue(result instanceof TreeSet);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("newTreeSet should create TreeSet with given elements")
        void newTreeSetShouldCreateTreeSetWithGivenElements() {
            Set<String> result = FmkSetUtil.newTreeSet("c", "a", "b");
            assertTrue(result instanceof TreeSet);
            assertEquals(3, result.size());

            // Check order
            List<String> asList = new ArrayList<>(result);
            assertEquals("a", asList.get(0));
            assertEquals("b", asList.get(1));
            assertEquals("c", asList.get(2));
        }
    }

    @Nested
    @DisplayName("Set Operation Tests")
    class SetOperationTests {
        @Test
        @DisplayName("union should return second set when first set is null")
        void unionShouldReturnSecondSetWhenFirstSetIsNull() {
            Set<String> set2 = Set.of("a", "b");
            Set<String> result = FmkSetUtil.union(null, set2);
            assertEquals(set2, result);
        }

        @Test
        @DisplayName("union should return first set when second set is null")
        void unionShouldReturnFirstSetWhenSecondSetIsNull() {
            Set<String> set1 = Set.of("a", "b");
            Set<String> result = FmkSetUtil.union(set1, null);
            assertEquals(set1, result);
        }

        @Test
        @DisplayName("union should combine elements from both sets")
        void unionShouldCombineElementsFromBothSets() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c"));

            Set<String> result = FmkSetUtil.union(set1, set2);

            assertEquals(3, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }

        @Test
        @DisplayName("intersection should return empty set when either set is null")
        void intersectionShouldReturnEmptySetWhenEitherSetIsNull() {
            Set<String> set = Set.of("a", "b");

            assertTrue(FmkSetUtil.intersection(null, set).isEmpty());
            assertTrue(FmkSetUtil.intersection(set, null).isEmpty());
        }

        @Test
        @DisplayName("intersection should return common elements")
        void intersectionShouldReturnCommonElements() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));

            Set<String> result = FmkSetUtil.intersection(set1, set2);

            assertEquals(2, result.size());
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }

        @Test
        @DisplayName("difference should return empty set when first set is null")
        void differenceShouldReturnEmptySetWhenFirstSetIsNull() {
            assertTrue(FmkSetUtil.difference(null, Set.of("a")).isEmpty());
        }

        @Test
        @DisplayName("difference should return copy of first set when second set is null")
        void differenceShouldReturnCopyOfFirstSetWhenSecondSetIsNull() {
            Set<String> set1 = Set.of("a", "b");
            Set<String> result = FmkSetUtil.difference(set1, null);

            assertEquals(set1, result);
            assertNotSame(set1, result);
        }

        @Test
        @DisplayName("difference should return elements in first set not in second")
        void differenceShouldReturnElementsInFirstSetNotInSecond() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));

            Set<String> result = FmkSetUtil.difference(set1, set2);

            assertEquals(1, result.size());
            assertTrue(result.contains("a"));
        }

        @Test
        @DisplayName("symmetricDifference should return second set when first set is null")
        void symmetricDifferenceShouldReturnSecondSetWhenFirstSetIsNull() {
            Set<String> set2 = Set.of("a", "b");
            Set<String> result = FmkSetUtil.symmetricDifference(null, set2);
            assertEquals(set2, result);
        }

        @Test
        @DisplayName("symmetricDifference should return first set when second set is null")
        void symmetricDifferenceShouldReturnFirstSetWhenSecondSetIsNull() {
            Set<String> set1 = Set.of("a", "b");
            Set<String> result = FmkSetUtil.symmetricDifference(set1, null);

            assertEquals(set1, result);
            assertNotSame(set1, result);
        }

        @Test
        @DisplayName("symmetricDifference should return elements in either set but not both")
        void symmetricDifferenceShouldReturnElementsInEitherSetButNotBoth() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));

            Set<String> result = FmkSetUtil.symmetricDifference(set1, set2);

            assertEquals(2, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("d"));
        }
    }

    @Nested
    @DisplayName("Subset Tests")
    class SubsetTests {
        @Test
        @DisplayName("isSubset should return true when subset is null or empty")
        void isSubsetShouldReturnTrueWhenSubsetIsNullOrEmpty() {
            Set<String> superset = Set.of("a", "b");

            assertTrue(FmkSetUtil.isSubset(null, superset));
            assertTrue(FmkSetUtil.isSubset(Collections.emptySet(), superset));
        }

        @Test
        @DisplayName("isSubset should return false when superset is null or empty but subset is not")
        void isSubsetShouldReturnFalseWhenSupersetIsNullOrEmptyButSubsetIsNot() {
            Set<String> subset = Set.of("a", "b");

            assertFalse(FmkSetUtil.isSubset(subset, null));
            assertFalse(FmkSetUtil.isSubset(subset, Collections.emptySet()));
        }

        @Test
        @DisplayName("isSubset should return true when all elements of subset are in superset")
        void isSubsetShouldReturnTrueWhenAllElementsOfSubsetAreInSuperset() {
            Set<String> subset = Set.of("a", "b");
            Set<String> superset = Set.of("a", "b", "c");

            assertTrue(FmkSetUtil.isSubset(subset, superset));
        }

        @Test
        @DisplayName("isSubset should return false when some elements of subset are not in superset")
        void isSubsetShouldReturnFalseWhenSomeElementsOfSubsetAreNotInSuperset() {
            Set<String> subset = Set.of("a", "d");
            Set<String> superset = Set.of("a", "b", "c");

            assertFalse(FmkSetUtil.isSubset(subset, superset));
        }
    }

    @Nested
    @DisplayName("Contains Tests")
    class ContainsTests {
        @Test
        @DisplayName("containsAny(Set, Collection) should return false when either input is null or empty")
        void containsAnySetCollectionShouldReturnFalseWhenEitherInputIsNullOrEmpty() {
            Set<String> set = Set.of("a", "b");
            Collection<String> collection = List.of("c", "d");

            assertFalse(FmkSetUtil.containsAny(null, collection));
            assertFalse(FmkSetUtil.containsAny(Collections.emptySet(), collection));
            // assertFalse(FmkSetUtil.containsAny(set, null));
            assertFalse(FmkSetUtil.containsAny(set, Collections.emptyList()));
        }

        @Test
        @DisplayName("containsAny(Set, Collection) should return true when sets have common elements")
        void containsAnySetCollectionShouldReturnTrueWhenSetsHaveCommonElements() {
            Set<String> set = Set.of("a", "b", "c");
            Collection<String> collection = List.of("c", "d");

            assertTrue(FmkSetUtil.containsAny(set, collection));
        }

        @Test
        @DisplayName("containsAny(Set, Collection) should return false when sets have no common elements")
        void containsAnySetCollectionShouldReturnFalseWhenSetsHaveNoCommonElements() {
            Set<String> set = Set.of("a", "b");
            Collection<String> collection = List.of("c", "d");

            assertFalse(FmkSetUtil.containsAny(set, collection));
        }

        @Test
        @DisplayName("containsAll should return false when set is null or empty")
        void containsAllShouldReturnFalseWhenSetIsNullOrEmpty() {
            assertFalse(FmkSetUtil.containsAll(null, "a", "b"));
            assertFalse(FmkSetUtil.containsAll(Collections.emptySet(), "a", "b"));
        }

        @Test
        @DisplayName("containsAll should return false when elements array is null or empty")
        void containsAllShouldReturnFalseWhenElementsArrayIsNullOrEmpty() {
            Set<String> set = Set.of("a", "b");

            assertFalse(FmkSetUtil.containsAll(set));
            assertFalse(FmkSetUtil.containsAll(set, (String[]) null));
        }

        @Test
        @DisplayName("containsAll should return true when set contains all elements")
        void containsAllShouldReturnTrueWhenSetContainsAllElements() {
            Set<String> set = Set.of("a", "b", "c");

            assertTrue(FmkSetUtil.containsAll(set, "a", "c"));
        }

        @Test
        @DisplayName("containsAll should return false when set doesn't contain all elements")
        void containsAllShouldReturnFalseWhenSetDoesntContainAllElements() {
            Set<String> set = Set.of("a", "b");

            assertFalse(FmkSetUtil.containsAll(set, "a", "c"));
        }

        @Test
        @DisplayName("containsAny(Set, varargs) should return false when set is null or empty")
        void containsAnyVarargsShouldReturnFalseWhenSetIsNullOrEmpty() {
            assertFalse(FmkSetUtil.containsAny(null, "a", "b"));
            assertFalse(FmkSetUtil.containsAny(Collections.emptySet(), "a", "b"));
        }

        @Test
        @DisplayName("containsAny(Set, varargs) should return false when elements array is null or empty")
        void containsAnyVarargsShouldReturnFalseWhenElementsArrayIsNullOrEmpty() {
            Set<String> set = Set.of("a", "b");

            assertFalse(FmkSetUtil.containsAny(set));
            assertFalse(FmkSetUtil.containsAny(set, (String[]) null));
        }

        @Test
        @DisplayName("containsAny(Set, varargs) should return true when set contains any element")
        void containsAnyVarargsShouldReturnTrueWhenSetContainsAnyElement() {
            Set<String> set = Set.of("a", "b");

            assertTrue(FmkSetUtil.containsAny(set, "a", "c"));
        }

        @Test
        @DisplayName("containsAny(Set, varargs) should return false when set contains no elements")
        void containsAnyVarargsShouldReturnFalseWhenSetContainsNoElements() {
            Set<String> set = Set.of("a", "b");

            assertFalse(FmkSetUtil.containsAny(set, "c", "d"));
        }
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {
        @Test
        @DisplayName("toSet(Collection) should return empty set for null or empty collection")
        void toSetCollectionShouldReturnEmptySetForNullOrEmptyCollection() {
            assertTrue(FmkSetUtil.toSet((Collection<String>) null).isEmpty());
            assertTrue(FmkSetUtil.toSet(Collections.emptyList()).isEmpty());
        }

        @Test
        @DisplayName("toSet(Collection) should convert collection to set")
        void toSetCollectionShouldConvertCollectionToSet() {
            List<String> list = Arrays.asList("a", "b", "a");
            Set<String> result = FmkSetUtil.toSet(list);

            assertEquals(2, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
        }

        @Test
        @DisplayName("toSet(varargs) should return empty set for null or empty array")
        void toSetVarargsShouldReturnEmptySetForNullOrEmptyArray() {
            assertTrue(FmkSetUtil.toSet((String[]) null).isEmpty());
            assertTrue(FmkSetUtil.toSet().isEmpty());
        }

        @Test
        @DisplayName("toSet(varargs) should convert array to set")
        void toSetVarargsShouldConvertArrayToSet() {
            Set<String> result = FmkSetUtil.toSet("a", "b", "a");

            assertEquals(2, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
        }

        @Test
        @DisplayName("toList should return empty list for null or empty set")
        void toListShouldReturnEmptyListForNullOrEmptySet() {
            assertTrue(FmkSetUtil.toList(null).isEmpty());
            assertTrue(FmkSetUtil.toList(Collections.emptySet()).isEmpty());
        }

        @Test
        @DisplayName("toList should convert set to list")
        void toListShouldConvertSetToList() {
            Set<String> set = Set.of("a", "b", "c");
            List<String> result = FmkSetUtil.toList(set);

            assertEquals(3, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }
    }

    @Nested
    @DisplayName("Filter and Transform Tests")
    class FilterAndTransformTests {
        @Test
        @DisplayName("filter should return empty set for null inputs")
        void filterShouldReturnEmptySetForNullInputs() {
            Set<String> set = Set.of("a", "b");

            assertTrue(FmkSetUtil.filter(null, s -> true).isEmpty());
            assertTrue(FmkSetUtil.filter(set, null).isEmpty());
        }

        @Test
        @DisplayName("filter should return elements that satisfy predicate")
        void filterShouldReturnElementsThatSatisfyPredicate() {
            Set<String> set = new HashSet<>(Arrays.asList("a", "ab", "abc"));
            Set<String> result = FmkSetUtil.filter(set, s -> s.length() > 1);

            assertEquals(2, result.size());
            assertTrue(result.contains("ab"));
            assertTrue(result.contains("abc"));
        }

        @Test
        @DisplayName("map should return empty set for null inputs")
        void mapShouldReturnEmptySetForNullInputs() {
            Set<String> set = Set.of("a", "b");

            assertTrue(FmkSetUtil.map(null, String::length).isEmpty());
            assertTrue(FmkSetUtil.map(set, null).isEmpty());
        }

        @Test
        @DisplayName("map should transform elements")
        void mapShouldTransformElements() {
            Set<String> set = new HashSet<>(Arrays.asList("a", "bb", "ccc"));
            Set<Integer> result = FmkSetUtil.map(set, String::length);

            assertEquals(3, result.size());
            assertTrue(result.contains(1));
            assertTrue(result.contains(2));
            assertTrue(result.contains(3));
        }
    }

    @Nested
    @DisplayName("Null Handling Tests")
    class NullHandlingTests {
        @Test
        @DisplayName("removeNulls should return empty set for null or empty input")
        void removeNullsShouldReturnEmptySetForNullOrEmptyInput() {
            assertTrue(FmkSetUtil.removeNulls(null).isEmpty());
            assertTrue(FmkSetUtil.removeNulls(Collections.emptySet()).isEmpty());
        }

        @Test
        @DisplayName("removeNulls should remove null elements")
        void removeNullsShouldRemoveNullElements() {
            Set<String> set = new HashSet<>();
            set.add("a");
            set.add(null);
            set.add("b");

            Set<String> result = FmkSetUtil.removeNulls(set);

            assertEquals(2, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertFalse(result.contains(null));
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {
        @Test
        @DisplayName("getFirst should return null for null or empty set")
        void getFirstShouldReturnNullForNullOrEmptySet() {
            assertNull(FmkSetUtil.getFirst(null));
            assertNull(FmkSetUtil.getFirst(Collections.emptySet()));
        }

        @Test
        @DisplayName("getFirst should return first element of set")
        void getFirstShouldReturnFirstElementOfSet() {
            // Using TreeSet to ensure predictable order
            Set<String> set = new TreeSet<>(Arrays.asList("a", "b", "c"));
            assertEquals("a", FmkSetUtil.getFirst(set));
        }

        @Test
        @DisplayName("range should return empty set when start > end")
        void rangeShouldReturnEmptySetWhenStartGreaterThanEnd() {
            assertTrue(FmkSetUtil.range(5, 3).isEmpty());
        }

        @Test
        @DisplayName("range should return set with integers in range")
        void rangeShouldReturnSetWithIntegersInRange() {
            Set<Integer> result = FmkSetUtil.range(1, 3);

            assertEquals(3, result.size());
            assertTrue(result.contains(1));
            assertTrue(result.contains(2));
            assertTrue(result.contains(3));
        }
    }

    @Nested
    @DisplayName("Power Set and Cartesian Product Tests")
    class PowerSetAndCartesianProductTests {
        @Test
        @DisplayName("powerSet should return set with empty set for null or empty input")
        void powerSetShouldReturnSetWithEmptySetForNullOrEmptyInput() {
            Set<Set<String>> result1 = FmkSetUtil.powerSet(null);
            Set<Set<String>> result2 = FmkSetUtil.powerSet(Collections.emptySet());

            assertEquals(1, result1.size());
            assertTrue(result1.contains(Collections.emptySet()));

            assertEquals(1, result2.size());
            assertTrue(result2.contains(Collections.emptySet()));
        }

        @Test
        @DisplayName("powerSet should return all possible subsets")
        void powerSetShouldReturnAllPossibleSubsets() {
            Set<String> set = new HashSet<>(Arrays.asList("a", "b"));
            Set<Set<String>> result = FmkSetUtil.powerSet(set);

            assertEquals(4, result.size());

            // Check for specific subsets
            assertTrue(result.contains(Collections.emptySet()));
            assertTrue(result.stream().anyMatch(s -> s.size() == 1 && s.contains("a")));
            assertTrue(result.stream().anyMatch(s -> s.size() == 1 && s.contains("b")));
            assertTrue(result.stream().anyMatch(s -> s.size() == 2 && s.contains("a") && s.contains("b")));
        }

        @Test
        @DisplayName("cartesianProduct should return empty set when either input is null or empty")
        void cartesianProductShouldReturnEmptySetWhenEitherInputIsNullOrEmpty() {
            Set<String> set = Set.of("a", "b");

            assertTrue(FmkSetUtil.cartesianProduct(null, set).isEmpty());
            assertTrue(FmkSetUtil.cartesianProduct(set, null).isEmpty());
            assertTrue(FmkSetUtil.cartesianProduct(Collections.emptySet(), set).isEmpty());
            assertTrue(FmkSetUtil.cartesianProduct(set, Collections.emptySet()).isEmpty());
        }

        @Test
        @DisplayName("cartesianProduct should return all combinations")
        void cartesianProductShouldReturnAllCombinations() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
            Set<String> set2 = new HashSet<>(Arrays.asList("1", "2"));

            Set<List<String>> result = FmkSetUtil.cartesianProduct(set1, set2);

            assertEquals(4, result.size());

            // Convert to a more easily testable format
            Set<String> combinations = result.stream()
                    .map(list -> String.join("", list))
                    .collect(Collectors.toSet());

            assertTrue(combinations.contains("a1"));
            assertTrue(combinations.contains("a2"));
            assertTrue(combinations.contains("b1"));
            assertTrue(combinations.contains("b2"));
        }
    }

    @Nested
    @DisplayName("Multiple Set Union Tests")
    class MultipleSetUnionTests {
        @Test
        @DisplayName("union(varargs) should return empty set for null or empty input")
        void unionVarargsShouldReturnEmptySetForNullOrEmptyInput() {
            assertTrue(FmkSetUtil.union((Set<String>[]) null).isEmpty());
            assertTrue(FmkSetUtil.union().isEmpty());
        }

        @Test
        @DisplayName("union(varargs) should combine elements from all sets")
        void unionVarargsShouldCombineElementsFromAllSets() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c"));
            Set<String> set3 = new HashSet<>(Arrays.asList("c", "d"));

            Set<String> result = FmkSetUtil.union(set1, set2, set3);

            assertEquals(4, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
            assertTrue(result.contains("d"));
        }

        @Test
        @DisplayName("union(varargs) should handle null sets")
        void unionVarargsShouldHandleNullSets() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
            Set<String> set3 = new HashSet<>(Arrays.asList("c", "d"));

            Set<String> result = FmkSetUtil.union(set1, null, set3);

            assertEquals(4, result.size());
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
            assertTrue(result.contains("d"));
        }
    }

    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTests {

        static Stream<Arguments> emptySetTestCases() {
            return Stream.of(
                    Arguments.of(null, true),
                    Arguments.of(Collections.emptySet(), true),
                    Arguments.of(Set.of("item"), false)
            );
        }

        @ParameterizedTest(name = "isEmpty({0}) should return {1}")
        @MethodSource("emptySetTestCases")
        void isEmptyParameterizedTest(Set<?> set, boolean expected) {
            assertEquals(expected, FmkSetUtil.isEmpty(set));
        }

        static Stream<Arguments> notEmptySetTestCases() {
            return Stream.of(
                    Arguments.of(null, false),
                    Arguments.of(Collections.emptySet(), false),
                    Arguments.of(Set.of("item"), true)
            );
        }

        @ParameterizedTest(name = "isNotEmpty({0}) should return {1}")
        @MethodSource("notEmptySetTestCases")
        void isNotEmptyParameterizedTest(Set<?> set, boolean expected) {
            assertEquals(expected, FmkSetUtil.isNotEmpty(set));
        }

        static Stream<Arguments> setOperationTestCases() {
            Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));
            Set<String> emptySet = Collections.emptySet();

            return Stream.of(
                    // Union test cases: set1, set2, expected result
                    Arguments.of("union", set1, set2, new HashSet<>(Arrays.asList("a", "b", "c", "d"))),
                    Arguments.of("union", set1, emptySet, set1),
                    Arguments.of("union", emptySet, set2, set2),

                    // Intersection test cases
                    Arguments.of("intersection", set1, set2, new HashSet<>(Arrays.asList("b", "c"))),
                    Arguments.of("intersection", set1, emptySet, emptySet),

                    // Difference test cases
                    Arguments.of("difference", set1, set2, new HashSet<>(Arrays.asList("a"))),
                    Arguments.of("difference", set1, emptySet, set1),
                    Arguments.of("difference", emptySet, set2, emptySet),

                    // Symmetric difference test cases
                    Arguments.of("symmetricDifference", set1, set2, new HashSet<>(Arrays.asList("a", "d"))),
                    Arguments.of("symmetricDifference", set1, emptySet, set1),
                    Arguments.of("symmetricDifference", emptySet, set2, set2)
            );
        }

        @ParameterizedTest(name = "{0}({1}, {2}) should return {3}")
        @MethodSource("setOperationTestCases")
        void setOperationParameterizedTest(String operation, Set<String> set1, Set<String> set2, Set<String> expected) {
            Set<String> result;

            switch (operation) {
                case "union":
                    result = FmkSetUtil.union(set1, set2);
                    break;
                case "intersection":
                    result = FmkSetUtil.intersection(set1, set2);
                    break;
                case "difference":
                    result = FmkSetUtil.difference(set1, set2);
                    break;
                case "symmetricDifference":
                    result = FmkSetUtil.symmetricDifference(set1, set2);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + operation);
            }

            assertEquals(expected, result);
        }
    }
}