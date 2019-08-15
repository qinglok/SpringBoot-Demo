package xyz.beingx.basespring.component

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * 配置自动API文档
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(apiKey())
    }

//    @Bean
//    fun security(): SecurityConfiguration {
//        return SecurityConfigurationBuilder.builder()
//                .clientId("test-app-client-id")
//                .clientSecret("test-app-client-secret")
//                .realm("test-app-realm")
//                .appName("test-app")
//                .scopeSeparator(",")
//                .build()
////
////		return SecurityConfiguration(
////				"test-app-client-id",
////				"test-app-client-secret",
////				"test-app-realm",
////				"test-app",
////				"",
////				ApiKeyVehicle.HEADER,
////				"Authorization",
////				"," /*scope separator*/)
//    }


    @Bean
    fun apiKey(): List<SecurityScheme> = listOf(
            ApiKey("令牌", "token", "header")
    )

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Spring REST Sample with Swagger")
                .description("Spring REST Sample with Swagger")
                .termsOfServiceUrl("https://github.com/qinglok")
                .version("1.0.0")
                .build()
    }


//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        super.addResourceHandlers(registry)
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }


}