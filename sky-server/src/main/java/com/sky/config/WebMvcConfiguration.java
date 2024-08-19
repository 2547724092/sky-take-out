package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean//创建 ApiInfo 对象，设置 API 文档的标题、版本和描述信息。
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("接口文档")
                .version("2.0")
                .description("项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)//创建 Docket 对象并配置 Swagger 文档
                .apiInfo(apiInfo)//设置文档类型为 Swagger 2 设置文档信息 apiInfo
                .select()//
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller"))//选择哪些 Controller 类参与文档生成，这里选择了包 com.sky.controller 下的所有 Controlle
                .paths(PathSelectors.any())//选择所有路径
                .build();
        return docket;//返回配置好的 Docket 实例
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
