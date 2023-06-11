package com.bupt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    //接口文档构建配置
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                //通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现
                .select()
                //所有的接口
                .apis(RequestHandlerSelectors.any())
                //指定扫描的路径
//                .apis(RequestHandlerSelectors.basePackage("com.bupt.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    //接口文档信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("超级安全云盘Api接口服务列表")
                .description("")
                .termsOfServiceUrl("http://192.168.100.104:7080")
                .version("1.0")
                .build();
    }

}
