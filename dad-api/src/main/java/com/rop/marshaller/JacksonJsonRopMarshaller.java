package com.rop.marshaller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.dad.common.util.DateUtil;
import com.rop.RopException;
import com.rop.RopMarshaller;

public class JacksonJsonRopMarshaller implements RopMarshaller {

	 private static ObjectMapper objectMapper;

	    public void marshaller(Object object, OutputStream outputStream) {
	        try {
	            JsonGenerator jsonGenerator = getObjectMapper().getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);
	            getObjectMapper().writeValue(jsonGenerator, object);
	        } catch (IOException e) {
	            throw new RopException(e);
	        }
	    }

	    @SuppressWarnings("static-access")
		private ObjectMapper getObjectMapper() throws IOException {
	        if (this.objectMapper == null) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
	            SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
	            serializationConfig = serializationConfig.without(SerializationConfig.Feature.WRAP_ROOT_VALUE)
	                    .with(SerializationConfig.Feature.INDENT_OUTPUT)
	                    .withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL)
	                    .withSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY)
	                    .withAnnotationIntrospector(introspector);
	            objectMapper.setSerializationConfig(serializationConfig);
	            //这里指定日期输出格式
	            objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.simpleDatefmt));
	            this.objectMapper = objectMapper;
	        }
	        return this.objectMapper;
	    }

}
