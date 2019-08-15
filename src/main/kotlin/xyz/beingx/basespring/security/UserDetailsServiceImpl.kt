package xyz.beingx.basespring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.EntityBoolean
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.sys.User

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private lateinit var dao: Dao

    override fun loadUserByUsername(username: String?): UserDetails {
        if(username.isNullOrEmpty()) return JwtUser(User())

        val findOne = dao.userDao.findOne(
                Example.of(User(name = username, isEnable = EntityBoolean.TRUE, status = EntityStatus.NORMAL))
        )
        if (findOne.isPresent) {
            return JwtUser(findOne.get())
        } else {
            throw UsernameNotFoundException("No user found with username '$username'.")
        }
    }

}