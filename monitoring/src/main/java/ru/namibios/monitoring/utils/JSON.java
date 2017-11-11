package ru.namibios.monitoring.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class JSON {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private JSON(){}
	
	public static ObjectMapper getInstance() {
		return MAPPER;
	}
	
	public static String getStringFromReader(BufferedReader reader) throws IOException {
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
			sb.append(line);
		
		return sb.toString();
		
	}
	
	public static List<Map<String, Object>> getList(String json) throws JsonParseException, JsonMappingException, IOException {
		
		List<Map<String, Object>> data = new ArrayList<>();
		
		if (json.startsWith("{")) {
			
			data.add(getInstance().readValue(json, new TypeReference<Map<String, Object>>() {}));
		} else if(json.startsWith("[")) {
			
			data = JSON.getInstance().readValue(json, new TypeReference<List<Map<String, Object>>>(){});
		} else {
			
			throw new UnsupportedOperationException("Unsupported format");
		}
		
		return data;
	}
	
}
