package com.cjlabs.web.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FmkJacksonUtil 测试类
 */
@Slf4j
@DisplayName("FmkJacksonUtil Tests")
class FmkJacksonUtilTest_JsonNode {

    @Test
    @DisplayName("createObjectNode should create empty ObjectNode")
    void createObjectNodeShouldCreateEmptyObjectNode() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();
        assertNotNull(node);
        assertTrue(node.isObject());
        assertEquals(0, node.size());
    }

    @Test
    @DisplayName("ObjectNode should support put operations")
    void objectNodeShouldSupportPutOperations() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();

        // Test various put operations
        node.put("stringField", "test value");
        node.put("intField", 123);
        node.put("longField", 456L);
        node.put("doubleField", 78.9);
        node.put("booleanField", true);
        node.putNull("nullField");

        // Verify values
        assertEquals("test value", node.get("stringField").asText());
        assertEquals(123, node.get("intField").asInt());
        assertEquals(456L, node.get("longField").asLong());
        assertEquals(78.9, node.get("doubleField").asDouble(), 0.001);
        assertTrue(node.get("booleanField").asBoolean());
        assertTrue(node.get("nullField").isNull());

        // Verify size
        assertEquals(6, node.size());
    }

    @Test
    @DisplayName("ObjectNode should support nested structures")
    void objectNodeShouldSupportNestedStructures() {
        ObjectNode root = FmkJacksonUtil.createObjectNode();

        // Create nested object
        ObjectNode nested = FmkJacksonUtil.createObjectNode();
        nested.put("nestedField", "nested value");
        nested.put("nestedNumber", 42);

        root.set("nested", nested);
        root.put("rootField", "root value");

        // Verify structure
        assertEquals("root value", root.get("rootField").asText());
        assertNotNull(root.get("nested"));
        assertTrue(root.get("nested").isObject());
        assertEquals("nested value", root.get("nested").get("nestedField").asText());
        assertEquals(42, root.get("nested").get("nestedNumber").asInt());
    }

    @Test
    @DisplayName("ObjectNode should support putPOJO")
    void objectNodeShouldSupportPutPOJO() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();

        FmkJacksonUtilTest.TestUser user = new FmkJacksonUtilTest.TestUser(1L, "Test User", 30);
        node.putPOJO("user", user);

        assertNotNull(node.get("user"));
        assertTrue(node.get("user").isObject());
        assertEquals(1, node.get("user").get("id").asLong());
        assertEquals("Test User", node.get("user").get("name").asText());
        assertEquals(30, node.get("user").get("age").asInt());
    }

    @Test
    @DisplayName("ArrayNode should support add operations")
    void arrayNodeShouldSupportAddOperations() {
        ArrayNode array = FmkJacksonUtil.createArrayNode();

        // Test various add operations
        array.add("string");
        array.add(123);
        array.add(456L);
        array.add(78.9);
        array.add(true);
        array.addNull();

        // Verify values
        assertEquals(6, array.size());
        assertEquals("string", array.get(0).asText());
        assertEquals(123, array.get(1).asInt());
        assertEquals(456L, array.get(2).asLong());
        assertEquals(78.9, array.get(3).asDouble(), 0.001);
        assertTrue(array.get(4).asBoolean());
        assertTrue(array.get(5).isNull());
    }

    @Test
    @DisplayName("ArrayNode should support nested structures")
    void arrayNodeShouldSupportNestedStructures() {
        ArrayNode root = FmkJacksonUtil.createArrayNode();

        // Add primitive values
        root.add("first");

        // Add nested object
        ObjectNode obj = FmkJacksonUtil.createObjectNode();
        obj.put("key", "value");
        root.add(obj);

        // Add nested array
        ArrayNode nested = FmkJacksonUtil.createArrayNode();
        nested.add(1);
        nested.add(2);
        nested.add(3);
        root.add(nested);

        // Verify structure
        assertEquals(3, root.size());
        assertEquals("first", root.get(0).asText());
        assertTrue(root.get(1).isObject());
        assertEquals("value", root.get(1).get("key").asText());
        assertTrue(root.get(2).isArray());
        assertEquals(3, root.get(2).size());
        assertEquals(2, root.get(2).get(1).asInt());
    }

    @Test
    @DisplayName("ArrayNode should support addPOJO")
    void arrayNodeShouldSupportAddPOJO() {
        ArrayNode array = FmkJacksonUtil.createArrayNode();

        FmkJacksonUtilTest.TestUser user1 = new FmkJacksonUtilTest.TestUser(1L, "User 1", 25);
        FmkJacksonUtilTest.TestUser user2 = new FmkJacksonUtilTest.TestUser(2L, "User 2", 30);

        array.addPOJO(user1);
        array.addPOJO(user2);

        assertEquals(2, array.size());
        assertEquals(1, array.get(0).get("id").asLong());
        assertEquals("User 1", array.get(0).get("name").asText());
        assertEquals(2, array.get(1).get("id").asLong());
        assertEquals("User 2", array.get(1).get("name").asText());
    }

    @Test
    @DisplayName("ObjectNode with ArrayNode should work together")
    void objectNodeWithArrayNodeShouldWorkTogether() {
        ObjectNode root = FmkJacksonUtil.createObjectNode();

        // Add array to object
        ArrayNode users = FmkJacksonUtil.createArrayNode();

        ObjectNode user1 = FmkJacksonUtil.createObjectNode();
        user1.put("id", 1);
        user1.put("name", "User 1");

        ObjectNode user2 = FmkJacksonUtil.createObjectNode();
        user2.put("id", 2);
        user2.put("name", "User 2");

        users.add(user1);
        users.add(user2);

        root.set("users", users);
        root.put("total", 2);

        // Verify structure
        assertEquals(2, root.get("total").asInt());
        assertTrue(root.get("users").isArray());
        assertEquals(2, root.get("users").size());
        assertEquals("User 1", root.get("users").get(0).get("name").asText());
        assertEquals("User 2", root.get("users").get(1).get("name").asText());
    }

    @Test
    @DisplayName("JsonNode should convert to JSON string")
    void jsonNodeShouldConvertToJsonString() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();
        node.put("name", "Test");
        node.put("age", 30);

        String json = FmkJacksonUtil.toJson(node);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test\""));
        assertTrue(json.contains("\"age\":30"));

        log.info("JsonNode to JSON: {}", json);
    }

    @Test
    @DisplayName("ArrayNode should convert to JSON string")
    void arrayNodeShouldConvertToJsonString() {
        ArrayNode array = FmkJacksonUtil.createArrayNode();
        array.add("one");
        array.add("two");
        array.add("three");

        String json = FmkJacksonUtil.toJson(array);
        assertNotNull(json);
        assertEquals("[\"one\",\"two\",\"three\"]", json);

        log.info("ArrayNode to JSON: {}", json);
    }

    @Test
    @DisplayName("ObjectNode should support remove operations")
    void objectNodeShouldSupportRemoveOperations() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();
        node.put("field1", "value1");
        node.put("field2", "value2");
        node.put("field3", "value3");

        assertEquals(3, node.size());

        // Remove a field
        node.remove("field2");
        assertEquals(2, node.size());
        assertNull(node.get("field2"));
        assertNotNull(node.get("field1"));
        assertNotNull(node.get("field3"));
    }

    @Test
    @DisplayName("ArrayNode should support remove operations")
    void arrayNodeShouldSupportRemoveOperations() {
        ArrayNode array = FmkJacksonUtil.createArrayNode();
        array.add("one");
        array.add("two");
        array.add("three");

        assertEquals(3, array.size());

        // Remove by index
        array.remove(1);
        assertEquals(2, array.size());
        assertEquals("one", array.get(0).asText());
        assertEquals("three", array.get(1).asText());
    }

    @Test
    @DisplayName("ObjectNode should support has and hasNonNull")
    void objectNodeShouldSupportHasAndHasNonNull() {
        ObjectNode node = FmkJacksonUtil.createObjectNode();
        node.put("field1", "value");
        node.putNull("field2");

        assertTrue(node.has("field1"));
        assertTrue(node.has("field2"));
        assertFalse(node.has("field3"));

        assertTrue(node.hasNonNull("field1"));
        assertFalse(node.hasNonNull("field2"));
        assertFalse(node.hasNonNull("field3"));
    }

    @Test
    @DisplayName("JsonNode path traversal should work")
    void jsonNodePathTraversalShouldWork() {
        String json = "{\"user\":{\"profile\":{\"name\":\"Test User\",\"age\":30}}}";
        JsonNode node = FmkJacksonUtil.parseJsonNode(json);

        assertNotNull(node);

        // Using get() chain
        assertEquals("Test User", node.get("user").get("profile").get("name").asText());
        assertEquals(30, node.get("user").get("profile").get("age").asInt());

        // Using path() - returns MissingNode instead of null if not found
        assertEquals("Test User", node.path("user").path("profile").path("name").asText());
        assertTrue(node.path("nonexistent").isMissingNode());
    }
}
