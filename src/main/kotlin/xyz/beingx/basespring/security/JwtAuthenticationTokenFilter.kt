package xyz.beingx.basespring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userDetailsServiceImpl: UserDetailsService

    @Autowired
    private lateinit var tokenUtils: TokenUtils

    /**
     * 配置Spring Security验证规则
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (SecurityContextHolder.getContext().authentication == null) {

            //从请求的header中拿到令牌，业界标准的字段名是“Authorization”，token前面要加“Bearer ”
            //字段名可以随便定制，token前面加“Bearer ”也不是必须，但建议遵循标准
//            val authToken = request.getHeader("Authorization") ?: ""
//            if (authToken.startsWith("Bearer ")) {
//                val token = authToken.substring(7)
//                val userName = tokenUtils.getUserNameFromToken(token) //从token中解析出userName
//                val userDetails = userDetailsServiceImpl.loadUserByUsername(userName) //从数据库查找相关用户
//                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//                SecurityContextHolder.getContext()?.authentication = authentication //将鉴权后的信息放置到Security全局上下文
//            }
            val token = request.getHeader("token")
            if (!token.isNullOrEmpty()) {
                val userName = tokenUtils.getUserNameFromToken(token) //从token中解析出userName
                val userDetails = userDetailsServiceImpl.loadUserByUsername(userName) //从数据库查找相关用户
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext()?.authentication = authentication //将鉴权后的信息放置到Security全局上下文
            }
        }

        chain.doFilter(request, response)
    }
}