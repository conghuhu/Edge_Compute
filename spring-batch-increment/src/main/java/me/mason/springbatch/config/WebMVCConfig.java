package me.mason.springbatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author conghuhu
 * @create 2021-10-08 11:07
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {




    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域配置
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://101.42.243.67/")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }


}
