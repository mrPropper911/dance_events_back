package by.belyahovich.dance_events.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtCsrfFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository tokenRepository;
    private final HandlerExceptionResolver resolver;

    public JwtCsrfFilter(CsrfTokenRepository tokenRepository, HandlerExceptionResolver resolver) {
        this.tokenRepository = tokenRepository;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute(HttpServletResponse.class.getName(), response);
        CsrfToken csrfToken = this.tokenRepository.loadToken(request);
        boolean missingToken = csrfToken == null;
        if (missingToken) {
            csrfToken = this.tokenRepository.generateToken(request);
            this.tokenRepository.saveToken(csrfToken, request, response);
        }

        request.setAttribute(CsrfToken.class.getName(), csrfToken);
        request.setAttribute(csrfToken.getParameterName(), csrfToken);

        //todo need signin controller
        if (request.getServletPath().equals("/signup")) {
            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                resolver.resolveException(request, response, null,
                        new MissingCsrfTokenException(csrfToken.getToken()));
            }
        } else {
            String actualToken = request.getHeader(csrfToken.getHeaderName());
            if (actualToken == null) {
                actualToken = request.getParameter(csrfToken.getParameterName());
            }
            try {
                if (!StringUtils.isEmpty(actualToken)) {
                    Jwts.parser()
                            .setSigningKey(((JwtTokenRepository) tokenRepository).getSecret())
                            .parseClaimsJws(actualToken);

                    filterChain.doFilter(request, response);
                } else {
                    resolver.resolveException(request, response, null,
                            new InvalidCsrfTokenException(csrfToken, actualToken));
                }
            } catch (JwtException e) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("INVALID CSRF TOKEN FOUND FOR " + UrlUtils.buildFullRequestUrl(request));
                }

                if (missingToken) {
                    resolver.resolveException(request, response, null,
                            new MissingCsrfTokenException(actualToken));
                } else {
                    resolver.resolveException(request, response, null,
                            new InvalidCsrfTokenException(csrfToken, actualToken));
                }
            }
        }
    }
}
