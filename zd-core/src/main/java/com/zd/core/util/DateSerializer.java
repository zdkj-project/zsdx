package com.zd.core.util;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * ClassName: DateSerializer 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 日期序列化.
 * date: 2016年3月13日 上午11:40:53 
 *
 * @author luoyibo
 * @version 
 * @since JDK 1.8
 */
public class DateSerializer extends JsonSerializer<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(DateFormatUtils.format(value, DATE_FORMAT));
    }

}
