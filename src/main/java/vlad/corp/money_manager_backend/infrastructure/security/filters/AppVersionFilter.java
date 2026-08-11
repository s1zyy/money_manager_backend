package vlad.corp.money_manager_backend.infrastructure.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vlad.corp.money_manager_backend.infrastructure.config.ApiVersionConfig;

import java.io.IOException;

@Component
public class AppVersionFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/.well-known/") || uri.equals("/join") || uri.equals("/invite") || uri.equals("/reset") || uri.equals("/privacy") || uri.equals("/delete-account");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String version = request.getHeader(ApiVersionConfig.HEADER_APP_VERSION);

        if (version == null || !ApiVersionConfig.SUPPORTED_VERSIONS.contains(version)) {
            response.setStatus(426);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Unsupported app version. Please update the app.\"," +
                    "\"currentVersion\":\"" + ApiVersionConfig.CURRENT_VERSION + "\"}"
            );
            return;
        }

        request.setAttribute(ApiVersionConfig.REQUEST_ATTR_APP_VERSION, version);
        filterChain.doFilter(request, response);
    }
}
