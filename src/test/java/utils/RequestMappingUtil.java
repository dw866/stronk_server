package utils;

import lombok.Getter;

@Getter
public class RequestMappingUtil {
    private RequestMappingUtil() {
        throw new UnsupportedOperationException("Class Instantiation not supported");
    }
    public static final String exerciseRequestMapping = "/api/v1/exercises";
    public static final String workoutTemplateRequestMapping = "/api/v1/workout-templates";
    public static final String workoutSessionRequestMapping = "/api/v1/workout-sessions";
}
