package com.cjlabs.web.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * FmkJacksonUtil 测试类
 */
@Slf4j
@DisplayName("FmkJacksonUtil Tests")
class FmkJacksonUtilTest {

    /**
     * 测试用实体类
     */
    static class TestUser {
        private Long id;
        private String name;
        private Integer age;
        private LocalDateTime createdAt;
        private Instant updatedAt;
        private boolean active;
        private List<String> roles;
        private Map<String, Object> attributes;

        public TestUser() {
        }

        public TestUser(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.active = true;
        }

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
    }

    /**
     * 测试用枚举类
     */
    enum TestStatus {
        ACTIVE, INACTIVE, PENDING
    }

    private TestUser testUser;
    private String expectedJson;

    @BeforeEach
    void setUp() {
        // 重置为默认配置，确保测试环境一致
        FmkJacksonUtil.resetToDefault();
        
        // 创建测试对象
        testUser = new TestUser(1L, "Test User", 30);
        testUser.setCreatedAt(LocalDateTime.of(2025, 10, 1, 12, 0, 0));
        testUser.setUpdatedAt(Instant.parse("2025-10-01T12:00:00Z"));
        testUser.setActive(true);
        testUser.setRoles(Arrays.asList("ADMIN", "USER"));
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("department", "IT");
        attributes.put("level", 5);
        testUser.setAttributes(attributes);
        
        // 预期的JSON字符串（不包含空格和换行，与toJson方法输出格式匹配）
        expectedJson = "{\"id\":1,\"name\":\"Test User\",\"age\":30,\"createdAt\":\"2025-10-01T12:00:00\",\"updatedAt\":\"2025-10-01T12:00:00Z\",\"active\":true,\"roles\":[\"ADMIN\",\"USER\"],\"attributes\":{\"department\":\"IT\",\"level\":5}}";
    }

    @AfterEach
    void tearDown() {
        // 测试结束后重置为默认配置
        FmkJacksonUtil.resetToDefault();
    }

    @Nested
    @DisplayName("Basic Serialization Tests")
    class SerializationTests {
        
        @Test
        @DisplayName("toJson should serialize object to JSON string")
        void toJsonShouldSerializeObjectToJsonString() {
            String json = FmkJacksonUtil.toJson(testUser);
            assertNotNull(json);
            assertTrue(json.contains("\"name\":\"Test User\""));
            assertTrue(json.contains("\"age\":30"));
        }
        
        @Test
        @DisplayName("toJson should handle null input")
        void toJsonShouldHandleNullInput() {
            String json = FmkJacksonUtil.toJson(null);
            assertNull(json);
        }
        
        @Test
        @DisplayName("toJsonPretty should produce formatted JSON")
        void toJsonPrettyShouldProduceFormattedJson() {
            String json = FmkJacksonUtil.toJsonPretty(testUser);
            assertNotNull(json);
            assertTrue(json.contains("  ")); // Should contain indentation
            assertTrue(json.contains("\n")); // Should contain line breaks
        }
    }
    
    @Nested
    @DisplayName("Basic Deserialization Tests")
    class DeserializationTests {
        
        @Test
        @DisplayName("parseObj should deserialize JSON to object")
        void parseObjShouldDeserializeJsonToObject() {
            TestUser user = FmkJacksonUtil.parseObj(expectedJson, TestUser.class);
            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("Test User", user.getName());
            assertEquals(30, user.getAge());
            assertTrue(user.isActive());
            assertEquals(2, user.getRoles().size());
            assertEquals("ADMIN", user.getRoles().get(0));
        }
        
        @Test
        @DisplayName("parseObj should handle null or blank input")
        void parseObjShouldHandleNullOrBlankInput() {
            TestUser user1 = FmkJacksonUtil.parseObj(null, TestUser.class);
            assertNull(user1);
            
            TestUser user2 = FmkJacksonUtil.parseObj("", TestUser.class);
            assertNull(user2);
        }
        
        @Test
        @DisplayName("parseObj should use TypeReference")
        void parseObjShouldUseTypeReference() {
            String jsonList = "[{\"id\":1,\"name\":\"User 1\"},{\"id\":2,\"name\":\"User 2\"}]";
            List<TestUser> users = FmkJacksonUtil.parseObj(jsonList, new TypeReference<List<TestUser>>() {});
            
            assertNotNull(users);
            assertEquals(2, users.size());
            assertEquals(1L, users.get(0).getId());
            assertEquals("User 1", users.get(0).getName());
            assertEquals(2L, users.get(1).getId());
            assertEquals("User 2", users.get(1).getName());
        }
    }
    
    @Nested
    @DisplayName("Collection Type Tests")
    class CollectionTypeTests {
        
        @Test
        @DisplayName("parseList should convert JSON to List")
        void parseListShouldConvertJsonToList() {
            String jsonArray = "[{\"id\":1,\"name\":\"User 1\"},{\"id\":2,\"name\":\"User 2\"}]";
            List<TestUser> users = FmkJacksonUtil.parseList(jsonArray, TestUser.class);
            
            assertNotNull(users);
            assertEquals(2, users.size());
        }
        
        @Test
        @DisplayName("parseMap should convert JSON to Map")
        void parseMapShouldConvertJsonToMap() {
            String jsonMap = "{\"key1\":\"value1\",\"key2\":123,\"key3\":true}";
            Map<String, Object> map = FmkJacksonUtil.parseMap(jsonMap);
            
            assertNotNull(map);
            assertEquals(3, map.size());
            assertEquals("value1", map.get("key1"));
            assertEquals(123, map.get("key2"));
            assertEquals(true, map.get("key3"));
        }
        
        @Test
        @DisplayName("parseMap with value type should convert JSON to typed Map")
        void parseMapWithValueTypeShouldConvertJsonToTypedMap() {
            String jsonMap = "{\"user1\":{\"id\":1,\"name\":\"User 1\"},\"user2\":{\"id\":2,\"name\":\"User 2\"}}";
            Map<String, TestUser> map = FmkJacksonUtil.parseMap(jsonMap, TestUser.class);
            
            assertNotNull(map);
            assertEquals(2, map.size());
            assertEquals(1L, map.get("user1").getId());
            assertEquals("User 1", map.get("user1").getName());
            assertEquals(2L, map.get("user2").getId());
        }
    }
    
    @Nested
    @DisplayName("JsonNode Tests")
    class JsonNodeTests {
        
        @Test
        @DisplayName("createJsonNode should create empty ObjectNode")
        void createJsonNodeShouldCreateEmptyObjectNode() {
            JsonNode node = FmkJacksonUtil.createJsonNode();
            assertNotNull(node);
            assertTrue(node.isObject());
            assertEquals(0, node.size());
        }
        
        @Test
        @DisplayName("createArrayNode should create empty ArrayNode")
        void createArrayNodeShouldCreateEmptyArrayNode() {
            ArrayNode node = FmkJacksonUtil.createArrayNode();
            assertNotNull(node);
            assertTrue(node.isArray());
            assertEquals(0, node.size());
        }
        
        @Test
        @DisplayName("parseJsonNode should convert JSON string to JsonNode")
        void parseJsonNodeShouldConvertJsonStringToJsonNode() {
            JsonNode node = FmkJacksonUtil.parseJsonNode(expectedJson);
            assertNotNull(node);
            assertEquals(1, node.get("id").asLong());
            assertEquals("Test User", node.get("name").asText());
            assertEquals(30, node.get("age").asInt());

            // 21:58:24.614 [main] INFO com.cjlabs.web.json.FmkJacksonUtilTest -- JsonNodeTests|parseJsonNodeShouldConvertJsonStringToJsonNode|={"id":1,"name":"Test User","age":30,"createdAt":"2025-10-01T12:00:00","updatedAt":"2025-10-01T12:00:00Z","active":true,"roles":["ADMIN","USER"],"attributes":{"department":"IT","level":5}}
            // 21:58:24.619 [main] INFO com.cjlabs.web.json.FmkJacksonUtilTest -- JsonNodeTests|parseJsonNodeShouldConvertJsonStringToJsonNode|={"id":1,"name":"Test User","age":30,"createdAt":"2025-10-01T12:00:00","updatedAt":"2025-10-01T12:00:00Z","active":true,"roles":["ADMIN","USER"],"attributes":{"department":"IT","level":5}}

            log.info("JsonNodeTests|parseJsonNodeShouldConvertJsonStringToJsonNode|={}", FmkJacksonUtil.toJson(node));
            log.info("JsonNodeTests|parseJsonNodeShouldConvertJsonStringToJsonNode|={}", node.toString());


            log.info("JsonNodeTests|parseJsonNodeShouldConvertJsonStringToJsonNode|={}", node.get("age"));

        }
        
        @Test
        @DisplayName("objToJsonNode should convert object to JsonNode")
        void objToJsonNodeShouldConvertObjectToJsonNode() {
            JsonNode node = FmkJacksonUtil.objToJsonNode(testUser);
            assertNotNull(node);
            assertEquals(1, node.get("id").asLong());
            assertEquals("Test User", node.get("name").asText());
            assertEquals(30, node.get("age").asInt());
        }
        
        @Test
        @DisplayName("jsonNodeToObj should convert JsonNode to object")
        void jsonNodeToObjShouldConvertJsonNodeToObject() {
            JsonNode node = FmkJacksonUtil.parseJsonNode(expectedJson);
            TestUser user = FmkJacksonUtil.jsonNodeToObj(node, TestUser.class);

            log.info("JsonNodeTests|jsonNodeToObjShouldConvertJsonNodeToObject|user={}", FmkJacksonUtil.toJson(user));

            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("Test User", user.getName());
            assertEquals(30, user.getAge());
        }
        
        @Test
        @DisplayName("jsonNodeToObj should convert JsonNode to object using TypeReference")
        void jsonNodeToObjShouldConvertJsonNodeToObjectUsingTypeReference() {
            String jsonArray = "[{\"id\":1,\"name\":\"User 1\"},{\"id\":2,\"name\":\"User 2\"}]";
            JsonNode node = FmkJacksonUtil.parseJsonNode(jsonArray);
            
            List<TestUser> users = FmkJacksonUtil.jsonNodeToObj(node, new TypeReference<List<TestUser>>() {});
            
            assertNotNull(users);
            assertEquals(2, users.size());
            assertEquals(1L, users.get(0).getId());
            assertEquals("User 1", users.get(0).getName());
        }
    }
    
    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {
        
        @Test
        @DisplayName("isValidJson should validate JSON strings")
        void isValidJsonShouldValidateJsonStrings() {
            assertTrue(FmkJacksonUtil.isValidJson(expectedJson));
            assertTrue(FmkJacksonUtil.isValidJson("{}"));
            assertTrue(FmkJacksonUtil.isValidJson("[]"));
            assertTrue(FmkJacksonUtil.isValidJson("[1,2,3]"));
            
            assertFalse(FmkJacksonUtil.isValidJson(null));
            assertFalse(FmkJacksonUtil.isValidJson(""));
            assertFalse(FmkJacksonUtil.isValidJson("not json"));
            assertFalse(FmkJacksonUtil.isValidJson("{key:value}"));  // Missing quotes
            assertFalse(FmkJacksonUtil.isValidJson("[1,2,]"));  // Trailing comma
        }
        
        @Test
        @DisplayName("deepClone should create a deep copy of an object")
        void deepCloneShouldCreateDeepCopyOfObject() {
            TestUser clone = FmkJacksonUtil.deepClone(testUser, TestUser.class);
            
            assertNotNull(clone);
            assertEquals(testUser.getId(), clone.getId());
            assertEquals(testUser.getName(), clone.getName());
            assertEquals(testUser.getAge(), clone.getAge());
            
            // Verify it's a deep copy by modifying the original
            testUser.setName("Modified Name");
            assertNotEquals(testUser.getName(), clone.getName());
        }
        
        @Test
        @DisplayName("convertValue should convert between object types")
        void convertValueShouldConvertBetweenObjectTypes() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("name", "Test User");
            map.put("age", 30);
            
            TestUser user = FmkJacksonUtil.convertValue(map, TestUser.class);
            
            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("Test User", user.getName());
            assertEquals(30, user.getAge());
        }
        
        @Test
        @DisplayName("convertValue should use TypeReference")
        void convertValueShouldUseTypeReference() {
            List<Map<String, Object>> mapList = Arrays.asList(
                    Map.of("id", 1, "name", "User 1"),
                    Map.of("id", 2, "name", "User 2")
            );
            
            List<TestUser> users = FmkJacksonUtil.convertValue(mapList, new TypeReference<List<TestUser>>() {});
            
            assertNotNull(users);
            assertEquals(2, users.size());
            assertEquals(1L, users.get(0).getId());
            assertEquals("User 1", users.get(0).getName());
        }
    }
    
    @Nested
    @DisplayName("ObjectMapper Configuration Tests")
    class ObjectMapperConfigurationTests {
        
        @Test
        @DisplayName("setMapper should replace the default ObjectMapper")
        void setMapperShouldReplaceDefaultObjectMapper() {
            // Create a custom ObjectMapper
            ObjectMapper customMapper = new ObjectMapper();
            customMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS); // Include null values
            
            // Replace the default mapper
            FmkJacksonUtil.setMapper(customMapper);
            
            // Create a test object with a null field
            TestUser user = new TestUser(1L, null, 30);
            
            // With the custom mapper, null values should be included
            String json = FmkJacksonUtil.toJson(user);
            assertTrue(json.contains("\"name\":null"));
        }
        
        @Test
        @DisplayName("resetToDefault should restore the default ObjectMapper")
        void resetToDefaultShouldRestoreDefaultObjectMapper() {
            // Create a custom ObjectMapper with different settings
            ObjectMapper customMapper = new ObjectMapper();
            customMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS); // Include null values
            FmkJacksonUtil.setMapper(customMapper);
            
            // Reset to default
            FmkJacksonUtil.resetToDefault();
            
            // Create a test object with a null field
            TestUser user = new TestUser(1L, null, 30);
            
            // With the default mapper, null values should be excluded
            String json = FmkJacksonUtil.toJson(user);
            assertFalse(json.contains("\"name\":null"));
        }
        
        @Test
        @DisplayName("getMapper should return the current ObjectMapper")
        void getMapperShouldReturnCurrentObjectMapper() {
            ObjectMapper mapper = FmkJacksonUtil.getMapper();
            assertNotNull(mapper);
            
            // Verify it's the same instance
            ObjectMapper customMapper = new ObjectMapper();
            FmkJacksonUtil.setMapper(customMapper);
            assertEquals(customMapper, FmkJacksonUtil.getMapper());
        }
        
        @Test
        @DisplayName("setMapper should throw exception for null mapper")
        void setMapperShouldThrowExceptionForNullMapper() {
            assertThrows(IllegalArgumentException.class, () -> FmkJacksonUtil.setMapper(null));
        }
    }
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("should handle complex nested structures")
        void shouldHandleComplexNestedStructures() {
            // Create a complex nested structure
            Map<String, Object> nestedMap = new HashMap<>();
            nestedMap.put("users", Arrays.asList(
                    new TestUser(1L, "User 1", 25),
                    new TestUser(2L, "User 2", 30)
            ));
            nestedMap.put("counts", Map.of(
                    "active", 10,
                    "inactive", 5
            ));
            nestedMap.put("dates", Arrays.asList(
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 2, 1)
            ));
            
            // Serialize and deserialize
            String json = FmkJacksonUtil.toJson(nestedMap);
            assertNotNull(json);
            
            Map<String, Object> result = FmkJacksonUtil.parseMap(json);
            assertNotNull(result);
            
            // Verify structure is preserved
            assertTrue(result.containsKey("users"));
            assertTrue(result.containsKey("counts"));
            assertTrue(result.containsKey("dates"));
        }
        
        @Test
        @DisplayName("should handle enum serialization and deserialization")
        void shouldHandleEnumSerializationAndDeserialization() {
            // Create object with enum
            Map<String, Object> map = new HashMap<>();
            map.put("status", TestStatus.ACTIVE);
            
            // Serialize
            String json = FmkJacksonUtil.toJson(map);
            assertNotNull(json);
            assertTrue(json.contains("\"status\":\"ACTIVE\""));
            
            // Deserialize
            Map<String, Object> result = FmkJacksonUtil.parseMap(json);
            assertEquals("ACTIVE", result.get("status"));
        }
    }
}
