package xyz.beingx.basespring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import xyz.beingx.basespring.dao.BaseRepositoryFactoryBean

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["xyz.beingx"], repositoryFactoryBeanClass = BaseRepositoryFactoryBean::class)
@SpringBootApplication
class BaseSpringApplication

fun main(args: Array<String>) {
	runApplication<BaseSpringApplication>(*args)
}
