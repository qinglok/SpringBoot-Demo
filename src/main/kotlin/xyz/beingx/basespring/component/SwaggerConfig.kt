package xyz.beingx.basespring.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import xyz.beingx.basespring.security.Roles


/**
 * 配置自动API文档
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value("\${project.version}")
    private lateinit var version: String

    @Bean
    fun petApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("xyz.beingx"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
//                .directModelSubstitute(LocalDate::class.java, String::class.java)
                .genericModelSubstitutes(ResponseEntity::class.java)
//                .alternateTypeRules(
//                        newRule(typeResolver.resolve(DeferredResult::class.java,
//                                typeResolver.resolve(ResponseEntity::class.java, WildcardType::class.java)),
//                                typeResolver.resolve(WildcardType::class.java)))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.DELETE,
                        listOf(ResponseMessageBuilder()
                                .code(403)
                                .message("鉴权失败")
                                .build()))
                .globalResponseMessage(RequestMethod.GET,
                        listOf(ResponseMessageBuilder()
                                .code(403)
                                .message("鉴权失败")
                                .build()))
                .securitySchemes(apiKey())
//                .securityContexts(listOf(securityContext()))
//                .enableUrlTemplating(true)
//                .globalOperationParameters(
//                        listOf(ParameterBuilder()
//                                .name("someGlobalParameter")
//                                .description("Description of someGlobalParameter")
//                                .modelRef(ModelRef("string"))
//                                .parameterType("query")
//                                .required(true)
//                                .build()))
//                .tags(Tag("Pet Service", "All apis relating to pets"))
//                .additionalModels(typeResolver!!.resolve(AdditionalModel::class.java))
                .apiInfo(apiInfo())
    }

    fun apiInfo(): ApiInfo = ApiInfoBuilder()
            .title("Api Documentation")
            .description("Api Documentation")
            .version(version)
            .contact(Contact("linx", "http://beingx.xyz/", "linx2568@gmail.com"))
            .build()

//    @Autowired
//    private lateinit var typeResolver: TypeResolver

    private fun apiKey(): List<ApiKey> {
        return List(Roles.all.size) { i ->
            ApiKey(Roles.all[i], "token", "header")
        }
    }

//    private fun securityContext(): SecurityContext {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//
//                .forPaths(RequestHandlerSelectors.)
//                .build()
//    }

//    fun defaultAuth(): List<SecurityReference> {
//        val authorizationScope = AuthorizationScope("global", "accessEverything")
//        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
//        authorizationScopes[0] = authorizationScope
//        return listOf(
//                SecurityReference("token", authorizationScopes))
//    }

//    @Bean
//    fun security(): SecurityConfiguration {
//        return SecurityConfigurationBuilder.builder()
//                .clientId("test-app-client-id")
//                .clientSecret("test-app-client-secret")
//                .realm("test-app-realm")
//                .appName("test-app")
//                .scopeSeparator(",")
//                .additionalQueryStringParams(null)
//                .useBasicAuthenticationWithAccessCodeGrant(false)
//                .build()
//    }

//    @Bean
//    fun uiConfig(): UiConfiguration {
//        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
//                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
//                .validatorUrl(null)
//                .build()
//    }


}