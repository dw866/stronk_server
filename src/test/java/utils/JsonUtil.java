package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
    private static volatile ObjectMapper mapper = null;

    private JsonUtil() {
        throw new UnsupportedOperationException("Class Instantiation not supported");
    }

    /**
     * Used for mapping JSON string content to DTO
     * @return
     */
    public static synchronized ObjectMapper getMapper() {
        if (mapper == null) {
            synchronized (JsonUtil.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper()
                            .registerModule(new Hibernate6Module())
                            .registerModule(new JavaTimeModule())
                            .registerModule(new Jdk8Module());
                }
            }
        }
        return mapper;
    }
}
