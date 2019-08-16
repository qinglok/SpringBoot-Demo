package xyz.beingx.basespring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.NORMAL
import xyz.beingx.basespring.entity.TRUE
import xyz.beingx.basespring.entity.sys.User
import java.util.stream.Collectors

@Transactional
@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private lateinit var dao: Dao

    override fun loadUserByUsername(username: String): UserDetails {
        val findOne = dao.userDao.findOne(
                Example.of(User(name = username, isEnable = TRUE, status = NORMAL))
        )
        if (findOne.isPresent) {
            val user = findOne.get()
            return JwtUser(
                    user.name,
                    user.password,
                    user.isEnable == TRUE,
                    user.roleSet.stream().map { role ->
                        SimpleGrantedAuthority("ROLE_" + role.name)
                    }?.collect(Collectors.toList())
            )
        } else {
            throw UsernameNotFoundException("No user found with username '$username'.")
        }
    }

}