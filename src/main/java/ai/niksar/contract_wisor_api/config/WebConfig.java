package ai.niksar.contract_wisor_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ai.niksar.contract_wisor_api.service.RoleApiUrlService;
import ai.niksar.contract_wisor_api.util.JwtTokenProvider;
import ai.niksar.contract_wisor_api.util.RawPathInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RoleApiUrlService roleApiUrlService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${action.enable}")
    private boolean enablePermission;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RawPathInterceptor(roleApiUrlService, jwtTokenProvider,enablePermission));
    }
}

