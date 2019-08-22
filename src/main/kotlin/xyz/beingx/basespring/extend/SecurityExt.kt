package xyz.beingx.basespring.extend

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

fun currentUser() : UserDetails?{
    val usernamePasswordAuthenticationToken = SecurityContextHolder.getContext()?.authentication as? UsernamePasswordAuthenticationToken
    return usernamePasswordAuthenticationToken?.principal as? UserDetails
}