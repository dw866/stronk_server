package utils;

import lombok.Getter;

@Getter
public class RequestMappingUtil {
    private RequestMappingUtil() {
        throw new UnsupportedOperationException("Class Instantiation not supported");
    }
    public static final String userRegistrationRequestMapping = "/api/v1/users/register";
    public static final String userLoginRequestMapping = "/api/v1/users/login";
    public final static String userLogoutRequestMapping = "/api/v1/users/logout";
    public final static String userProfileRequestMapping = "/api/v1/users/profile";
    public static final String exerciseRequestMapping = "/api/v1/exercises";
    public static final String workoutTemplateRequestMapping = "/api/v1/workout-templates";
    public static final String workoutSessionRequestMapping = "/api/v1/workout-sessions";
}
