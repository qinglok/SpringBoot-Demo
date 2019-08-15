package xyz.beingx.basespring.controller

import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import xyz.beingx.basespring.security.TokenUtils

@Api(tags = ["令牌"])
@RequestMapping("/token")
@RestController
class TokenController {

    @Autowired
    private lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var userDetailsServiceImpl: UserDetailsService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @ApiOperation("创建Token（登录）")
    @ApiResponses(
            ApiResponse(code = 200, message = "成功", response = String::class),
            ApiResponse(code = 403, message = "鉴权失败")
    )
    @PostMapping
    fun post(@RequestBody requestUser: RequestUser): ResponseEntity<*> {
        // Perform the security
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        requestUser.name,
                        requestUser.password
                )
        )
        SecurityContextHolder.getContext()?.authentication = authentication

        val userDetails = userDetailsServiceImpl.loadUserByUsername(requestUser.name)
        val token = tokenUtils.createNewToken(userDetails)

        return ResponseEntity.ok(token ?: "")
    }

    @ApiOperation("删除Token（退出登录）", authorizations = [Authorization("token")])
    @ApiResponses(
            ApiResponse(code = 200, message = "成功"),
            ApiResponse(code = 403, message = "鉴权失败")
    )
    @DeleteMapping
    fun delete(): ResponseEntity<*> {
        tokenUtils.deleteToken()
        return ResponseEntity.ok().body(null)
    }

}