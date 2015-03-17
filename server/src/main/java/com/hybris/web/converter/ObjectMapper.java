package com.hybris.web.converter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {

    public ObjectMapper() {
    }

    public ObjectMapper(JsonFactory jf) {
        super(jf);
    }

    public ObjectMapper(com.fasterxml.jackson.databind.ObjectMapper src) {
        super(src);
    }

    public ObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        super(jf, sp, dc);
    }

    public void init() {
        super.registerModule(new JodaModule());
    }
}
