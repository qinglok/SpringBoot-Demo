package xyz.beingx.basespring.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * User的包装类，只用于Spring Security，某些不用到的功能直接返回true
 */
data class JwtUser(
        var userName: String? = null,
        var userPassword: String? = null,
        var userIsEnabled: Boolean? = null,
        var userAuthorities: List<GrantedAuthority>? = null
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority>? = userAuthorities

    override fun isEnabled(): Boolean = userIsEnabled ?: false

    override fun getUsername(): String? = userName

    override fun getPassword(): String? = userPassword

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

}