package xyz.beingx.basespring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userDetailsServiceImpl: UserDetailsService

    /**
     * Spring Security自带密码加密器
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 过滤器
     */
    @Bean
    fun authenticationTokenFilterBean(): JwtAuthenticationTokenFilter {
        return JwtAuthenticationTokenFilter()
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }


    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsServiceImpl)?.passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity?) {
        http?.let { httpSecurity ->
            httpSecurity
                    .csrf().disable()
                    // 基于token，所以不需要session
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()

                    // swagger start
                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/configuration/ui").permitAll()
                    .antMatchers("/configuration/security").permitAll()
                    // swagger end

                    //test
                    .antMatchers("/test").permitAll()
                    .antMatchers("/test/**").permitAll()

                    // 对于获取token的rest api要允许匿名访问
                    .antMatchers(HttpMethod.POST, "/token").permitAll() //登录
                    .antMatchers(HttpMethod.POST, "/users").permitAll() //注册
                    // 除上面外的所有请求全部需要鉴权认证
                    .anyRequest().hasRole(Roles.user)
            httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)
            // 禁用缓存
            httpSecurity.headers().cacheControl()
        }
    }

}