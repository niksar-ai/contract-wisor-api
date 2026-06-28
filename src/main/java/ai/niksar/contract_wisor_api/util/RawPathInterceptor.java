package ai.niksar.contract_wisor_api.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.service.RoleApiUrlService;
import java.util.List;

/**
 * RawPathInterceptor is a class used as a HandlerInterceptor within the Spring Framework. This class provides a pre-handling interface for processing incoming HTTP requests and is used to apply certain operations, checkpoints, or validation mechanisms.
 * This mechanism is provided by the DispatcherServlet, which is part of Spring MVC, and is triggered during the processing of a request.
 * It ensures that a check is performed after the request passes through the filter, before the response is sent.
 */
@Component
public class RawPathInterceptor implements HandlerInterceptor {
    private final RoleApiUrlService roleApiUrlService;
    private final JwtTokenProvider jwtTokenProvider;
    private final boolean isPermissionEnable;

    public RawPathInterceptor(RoleApiUrlService roleApiUrlService, JwtTokenProvider jwtTokenProvider,@Value("${action.enable}") boolean permissionEnable) {
        this.roleApiUrlService  = roleApiUrlService;
        this.jwtTokenProvider   = jwtTokenProvider;
        this.isPermissionEnable = permissionEnable;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isPermissionEnable){
            String rawPath = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            if(rawPath != null){
                if (rawPath.matches("^/api/auth/.*") || rawPath.matches("^/api/app/.*")) {
                    return true;
                }
                String authorizationHeader = request.getHeader("Authorization");
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    String token = authorizationHeader.substring(7);
                    String username = jwtTokenProvider.extractUsername(token);
                    roleApiUrlService.checkRoleApiPermission(username, request.getMethod()+rawPath);
                } else {
                    throw new ContractWisorException.CustomException("Authorization header is missing.");
                }
            }
            else {
                throw new ContractWisorException.E029();
            }
        }
        return true;
    }
}