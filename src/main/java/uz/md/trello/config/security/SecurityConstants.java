package uz.md.trello.config.security;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/09:37 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */
public class SecurityConstants {
    public static final String[] WHITE_LIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/activate**",
            "/",
            "/js/sockjs-0.3.4.js",
            "/js/stomp.js",
            "/swagger-ui/**",
            "/api-docs/**"
    };
}
