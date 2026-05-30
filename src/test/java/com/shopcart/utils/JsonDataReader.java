package com.shopcart.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonDataReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode read(String relativePath) {
        try {
            return mapper.readTree(new File(relativePath));
        } catch (Exception e) {
            throw new RuntimeException("Failed reading " + relativePath, e);
        }
    }

    public static JsonNode users() { return read("testdata/users.json"); }
    public static JsonNode products() { return read("testdata/products.json"); }
}
