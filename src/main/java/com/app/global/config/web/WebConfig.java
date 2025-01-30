package com.app.global.config.web;

import com.app.global.filter.JakartaCompatibleXssEscapeServletFilter;
import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.resolver.memberInfo.MemberInfoArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;
    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("WebConfig - Interceptor 등록됨");
        registry.addInterceptor(authenticationInterceptor)
            .order(1) // 인증 인터셉터가 가장 먼저 동작하도록 설정
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/join", "/api/join/**",
                "/api/login", "/api/login/**",
                "/h2-console/**",
                "/api/access-token/issue", "/api/access-token/issue/**",
                "/api/health", "/api/card-benefits","/api/faces/**",
                    "/api/card-benefits/**",
                    "/api/qnaboard/test", "/api/user-data-test",
                    "/api/user-data-test/**");
        registry.addInterceptor(adminAuthorizationInterceptor)
            .order(2)
            .addPathPatterns("/api/admin/**");
        log.debug("등록된 인터셉터 경로: /api/**");
//        log.debug("Interceptor 호출: {}", request.getRequestURI());

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
//            .allowedOrigins("http://localhost:8082")
            .allowedOrigins("*")
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
            ).allowedHeaders("Content-Type", "Authorization", "Accept");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }

    @Bean
    public FilterRegistrationBean<JakartaCompatibleXssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<JakartaCompatibleXssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new JakartaCompatibleXssEscapeServletFilter(new XssEscapeServletFilter()));
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonEscapeConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        log.info("WebConfig - JSON Escape Converter 설정됨");
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}
