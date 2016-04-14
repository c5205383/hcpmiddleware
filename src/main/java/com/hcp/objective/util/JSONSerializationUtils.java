package com.hcp.objective.util;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JSONSerializationUtils {

	/**
	 * The ObjectMapper instance for JSON Serialization.
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();
	//private static final Logger logger = LoggerFactory.getLogger(JSONSerializationUtils.class);

	/**
	 * Private constructor.
	 */
	private JSONSerializationUtils() {
	}

	/**
	 * Returns the JSON string converted from the passed in object.
	 * 
	 * @param object
	 *            object to serialize
	 * @return converted JSON string
	 * @throws SerializationException
	 *             if any JSON conversion happens or if it cannot read the
	 *             object.
	 */
	public static String toJSON(Object object){
		try {
			// configure the mapper to resolve exception
			MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			MAPPER.getSerializerProvider().setNullKeySerializer(new MyNullKeySerializer());
			MAPPER.getSerializerProvider().setNullValueSerializer(new MyNullValueSerializer());
			return MAPPER.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			//logger.error(e.getMessage(),e);
			return "";
		} catch (JsonMappingException e) {
			//logger.error(e.getMessage(),e);
			return "";
		} catch (IOException e) {
			//logger.error(e.getMessage(),e);
			return "";
		}
	}

}

class MyNullKeySerializer extends JsonSerializer<Object>
{
  @Override
  public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) 
      throws IOException, JsonProcessingException
  {
    jsonGenerator.writeString("Key");
  }
}

class MyNullValueSerializer extends JsonSerializer<Object>
{
  @Override
  public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) 
      throws IOException, JsonProcessingException
  {
    jsonGenerator.writeString("null");
  }
}
