package com.project.utils;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
public class JsonUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	public static <T> String pojoToJson(T pojo) {
		if (pojo == null)
			return null;
		try {
			String json = getMapper().writeValueAsString(pojo);
			return json;
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> T jsonToPojo(String json, Class<T> pojoClass) {
		if (!(StringUtils.hasText(json)))
			return null;
		try {
			return getMapper().readValue(json, pojoClass);
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> List<T> jsonToPojoList(String json,
			TypeReference<List<T>> valueTypeRef) {
		if (!(StringUtils.hasText(json)))
			return null;
		try {
			return ((List) getMapper().readValue(json, valueTypeRef));
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> T jsonToPojo(Reader src, Class<T> pojoClass)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper().readValue(src, pojoClass);
	}

	public static <T> T jsonToPojo(String json, TypeReference<T> valueTypeRef) {
		if (!(StringUtils.hasText(json)))
			return null;
		try {
			return getMapper().readValue(json, valueTypeRef);
		} catch (IOException e) {
			return null;
		}
	}

	public static Map<String, Object> jsonToMap(String json) {
		if (!(StringUtils.hasText(json)))
			return null;
		try {
			return ((Map<String, Object>) getMapper().readValue(json, Map.class));
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> T pojoToPojo(T sourceObject, Class<T> targetType) {
		if (sourceObject == null) {
			return null;
		}
		return getMapper().convertValue(sourceObject, targetType);
	}

	public static <T> Map<String, Object> pojoToMap(T pojo) {
		if (pojo == null) {
			return null;
		}
		return ((Map) getMapper().convertValue(pojo, Map.class));
	}


	private static ObjectMapper getMapper() {
		if (mapper != null) {
			return mapper;
		}
		synchronized (JsonUtil.class) {
			if (mapper != null) {
				return mapper;
			}
			mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			return mapper;
		}
	}
	
	/**
	 * Map转url参数形式
	 * @param mapRes
	 * @return
	 * @throws Exception
	 */
	public static String parseURLPairByMap(Map<String, Object> mapRes) throws Exception{
		Map<String, Object> map=CollectionUtil.sortMap(mapRes);
		Set<String> keys=map.keySet();
		StringBuffer sb=new StringBuffer();
		for (String item : keys) {
			if(null!=map.get(item)) {
				sb.append(item+"="+map.get(item)+"&");
			}
			
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}

	public static String parseURLPair(Object o) throws Exception{
		Class<? extends Object> c = o.getClass();
		Field[] fields = c.getDeclaredFields();
		Map<String, Object> map = new TreeMap<String, Object>();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = field.get(o);
			if(value != null)
				map.put(name, value);
		}
		Set<Map.Entry<String, Object>> set = map.entrySet();
		Iterator<Map.Entry<String, Object>> it = set.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			Map.Entry<String, Object> e = it.next();
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}


	static {
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		mapper.setVisibility(mapper.getSerializationConfig()
				.getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
	}
}
