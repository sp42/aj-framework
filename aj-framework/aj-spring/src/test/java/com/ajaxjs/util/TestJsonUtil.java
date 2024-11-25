package com.ajaxjs.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonUtil {
    public static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    @InjectMocks
    private JsonUtil jsonUtil;

    private MockedStatic<JsonUtil> mockedStaticJsonUtil;

    @Test
    public void testToJson_ValidObject_ShouldReturnJsonString() {
        String expectedJson = "{\"name\":\"John\",\"age\":30}";
        Person person = new Person("John", 30);

        String actualJson = JsonUtil.toJson(person);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testToJsonPretty_ValidObject_ShouldReturnPrettyJsonString() {
        String expectedJson = "{\r\n  \"name\" : \"John\",\r\n  \"age\" : 30\r\n}";
        Person person = new Person("John", 30);

        String actualJson = JsonUtil.toJsonPretty(person);

        compareStrings(expectedJson, actualJson);
        assertEquals(expectedJson, actualJson);
    }

    public static void compareStrings(String str1, String str2) {
        StringBuilder differences = new StringBuilder();
        int maxLength = Math.max(str1.length(), str2.length());

        for (int i = 0; i < maxLength; i++) {
            char char1 = i < str1.length() ? str1.charAt(i) : '\0';
            char char2 = i < str2.length() ? str2.charAt(i) : '\0';

            if (char1 != char2) {
                differences.append("Index ").append(i).append(": '").append(char1).append("' != '").append(char2).append("'\n");
            }
        }

        if (differences.length() > 0) {
            System.out.println("Differences found:");
            System.out.println(differences.toString());
        } else {
            System.out.println("No differences found.");
        }
    }

    @Test
    public void testFromJson_ValidJson_ShouldReturnJavaObject() {
        String json = "{\"name\":\"John\", \"age\":30}";

        Person person = JsonUtil.fromJson(json, Person.class);

        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void testFromJson_InvalidJson_ShouldThrowException() {
        String json = "invalid json";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            JsonUtil.fromJson(json, Person.class);
        });

        assertEquals("Failed to convert JSON to object", exception.getMessage());
    }

    @Test
    public void testJson2map_ValidJson_ShouldReturnMap() {
        String json = "{\"name\":\"John\", \"age\":30}";

        Map<String, Object> map = JsonUtil.json2map(json);

        assertNotNull(map);
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testJson2list_ValidJson_ShouldReturnList() {
        String json = "[{\"name\":\"John\", \"age\":30}, {\"name\":\"Jane\", \"age\":25}]";

        List<Person> list = JsonUtil.json2list(json, Person.class);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("John", list.get(0).getName());
        assertEquals(30, list.get(0).getAge());
    }

    @Test
    public void testConvertValue_ValidObject_ShouldReturnConvertedObject() {
        Person person = new Person("John", 30);

        Map<String, Object> map = JsonUtil.convertValue(person, Map.class);

        assertNotNull(map);
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testPojo2map_ValidObject_ShouldReturnMap() {
        Person person = new Person("John", 30);

        Map<String, Object> map = JsonUtil.pojo2map(person);

        assertNotNull(map);
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testJson2Node_ValidJson_ShouldReturnJsonNode() {
        String json = "{\"name\":\"John\", \"age\":30}";

        JsonNode jsonNode = JsonUtil.json2Node(json);

        assertNotNull(jsonNode);
        assertEquals("John", jsonNode.get("name").asText());
        assertEquals(30, jsonNode.get("age").asInt());
    }

}
