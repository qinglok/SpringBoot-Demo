package xyz.beingx.basespring

import com.google.common.collect.Lists
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import xyz.beingx.basespring.dao.BaseRepositoryFactoryBean

@ServletComponentScan  // 扫描使用注解方式的servlet
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["xyz.beingx"], repositoryFactoryBeanClass = BaseRepositoryFactoryBean::class)
@SpringBootApplication
class BaseSpringApplication

fun main(args: Array<String>) {
	runApplication<BaseSpringApplication>(*args)
}
