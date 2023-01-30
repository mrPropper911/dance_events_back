//package by.belyahovich.dance_events.config;
//
//import by.belyahovich.dance_events.security.JwtTokenRepository;
//import lombok.Getter;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.session.SessionAuthenticationException;
//import org.springframework.security.web.csrf.InvalidCsrfTokenException;
//import org.springframework.security.web.csrf.MissingCsrfTokenException;
//import org.springframework.security.web.util.UrlUtils;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private final JwtTokenRepository jwtTokenRepository;
//
//    public GlobalExceptionHandler(JwtTokenRepository jwtTokenRepository) {
//        this.jwtTokenRepository = jwtTokenRepository;
//    }
//
//    @ExceptionHandler({AuthenticationException.class, MissingCsrfTokenException.class, InvalidCsrfTokenException.class, SessionAuthenticationException.class})
//    public ErrorInfo handleAuthenticationException(HttpServletRequest request,
//                                                   HttpServletResponse response) {
//        this.jwtTokenRepository.clearToken(response);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), "error.authorization");
//    }
//
//    @Getter
//    public static class ErrorInfo {
//        private final String url;
//        private final String info;
//
//        ErrorInfo(String url, String info) {
//            this.url = url;
//            this.info = info;
//        }
//    }
//}