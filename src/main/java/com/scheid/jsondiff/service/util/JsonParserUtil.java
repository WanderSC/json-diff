package com.scheid.jsondiff.service.util;

import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheid.jsondiff.exception.JsonDiffException;

/**
 * Utilitary class used to parse JSON data 
 */
public class JsonParserUtil {

	
	/**
	 * Encode the provided JSON's binary data to Base64.
	 *   
	 * @param json The JSON to be encoded.
	 * @return Base64 encoded JSON.
	 */
	public static String encodeBinaryBase64(String json) {
		byte[] bytes = json.getBytes();
        return DatatypeConverter.printBase64Binary(bytes);
	}
	
	/**
	 * Decode the provided Base64 to a JSON string.
	 *   
	 * @param base64Binary Base64 encoded binary data.
	 * @return Decoded JSON string.
	 */
	public static String decodeBinaryBase64(String base64Binary) {
		 byte[] base64Decoded = getBinaryFromBase64(base64Binary);
		 return new String(base64Decoded);
	}
	
	/**
	 * Get a Base64 string's binary data.
	 *   
	 * @param base64Binary Base64 encoded string.
	 * @return The provided base64 string's binary data.
	 */
	public static byte[] getBinaryFromBase64(String base64Binary) {
		try {	
			return DatatypeConverter.parseBase64Binary(base64Binary);
		}catch(Exception e) {
			throw new JsonDiffException("error.invalid.json", e);
		}
	}
	
	/**
	 * Get a Map from a provided JSON string
	 *   
	 * @param json JSON string.
	 * @return A Map with the JSON Nodes and its values.
	 */
	public static Map<String, Object> getJsonMap(String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			throw new JsonDiffException("error.invalid.json", e);
		}
	}
}
