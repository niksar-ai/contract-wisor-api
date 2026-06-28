package ai.niksar.contract_wisor_api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ai.niksar.contract_wisor_api.model.ApiLog;
import ai.niksar.contract_wisor_api.service.ApiLogService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private final ApiLogService apiLogService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ApiLoggingFilter(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        LocalDateTime requestTime = LocalDateTime.now();

        String method = request.getMethod();
        String url = request.getRequestURI();

        if (url.startsWith("/api/administration/logs/search")) {
            filterChain.doFilter(request, response);
            return;
        }

        RequestWrapper requestWrapper = new RequestWrapper(request);

        String requestBody = new String(requestWrapper.getInputStream().readAllBytes());
        String cleanedRequestBody = "";
        if (!requestBody.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(requestBody, Object.class);
            cleanedRequestBody = objectMapper.writeValueAsString(jsonObject);
        }

        Map<String, String> queryParameters = extractQueryParameters(requestWrapper);

        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String responseBody = new String(responseWrapper.toByteArray());
        int statusCode = responseWrapper.getStatus();
        LocalDateTime responseTime = LocalDateTime.now();


        response.getOutputStream().write(responseBody.getBytes());
        response.getOutputStream().flush();
        String authorizationHeader = request.getHeader("Authorization");
        UUID sessionId=null;
        String username=null;
        if(url.equals("/api/auth/logout")){
            JsonObject jsonObject= JsonParser.parseString(cleanedRequestBody).getAsJsonObject();
            if(jsonObject.get("accessToken") != null){
                String token=jsonObject.get("accessToken").getAsString();
                sessionId=UUID.fromString(jwtTokenProvider.getSessionIdFromAccessToken(token));
                username=jwtTokenProvider.extractUsername(token);
            }
        }
        else if(url.equals("/api/auth/refresh")){
            JsonObject jsonObject= JsonParser.parseString(cleanedRequestBody).getAsJsonObject();
            if(jsonObject.get("refreshToken") != null){
                String token=jsonObject.get("refreshToken").getAsString();
                sessionId=UUID.fromString(jwtTokenProvider.getSessionIdFromRefreshToken(token));
                username=jwtTokenProvider.extractUsernameFromRefreshToken(token);
            }
        }
        else if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() != null ) {
            username=SecurityContextHolder.getContext().getAuthentication().getName();
            String token = authorizationHeader.substring(7);
             sessionId=UUID.fromString(jwtTokenProvider.getSessionIdFromAccessToken(token));
        }
        ApiLog apiLog = new ApiLog();
        apiLog.setApiUrl(url);
        apiLog.setMethod(method);
        apiLog.setRequestBody(Util.encode(cleanedRequestBody));
        apiLog.setResponseBody(Util.encode(responseBody));
        apiLog.setStatusCode(String.valueOf(statusCode));
        apiLog.setRequestTime(requestTime);
        apiLog.setResponseTime(responseTime);
        apiLog.setIp(Util.getClientIp(request));
        apiLog.setQueryParams(queryParameters.toString());
        apiLog.setUsername(username);
        apiLog.setSessionId(sessionId);
        apiLog.setDeviceInfo(request.getHeader("User-Agent"));
        apiLogService.saveLog(apiLog);
    }

    private Map<String, String> extractQueryParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            parameters.put(paramName, request.getParameter(paramName));
        }
        return parameters;
    }
}
