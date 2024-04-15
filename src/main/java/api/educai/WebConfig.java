package api.educai;

import api.educai.middlewares.RoleInterceptor;
import api.educai.middlewares.TokenInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan({ "api.educai" })
public class WebConfig implements WebMvcConfigurer {
    private static final String[] INTERCEPTOR_EXCLUDED_URLS = { "/swagger-ui/**" };
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new TokenInterceptor())
                .excludePathPatterns(INTERCEPTOR_EXCLUDED_URLS);
        registry
                .addInterceptor(new RoleInterceptor())
                .excludePathPatterns(INTERCEPTOR_EXCLUDED_URLS);
    }
}