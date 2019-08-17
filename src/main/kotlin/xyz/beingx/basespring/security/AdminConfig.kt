package xyz.beingx.basespring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Example
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.NORMAL
import xyz.beingx.basespring.entity.TRUE
import xyz.beingx.basespring.entity.sys.Role
import xyz.beingx.basespring.entity.sys.User

@Configuration
class AdminConfig{

    @Autowired
    private lateinit var dao: Dao

    @Value("\${server.admin.name}")
    private lateinit var adminName: String

    @Value("\${server.admin.password}")
    private lateinit var adminPassword: String

    @Autowired
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun initConfig() {
        initRoles()
        initAdmin()
        setAdminAllRoles()
    }

    fun initRoles() {
        for (value in Roles.allRoles) {
            val t = Role(name = value, status = NORMAL)
            if (!dao.roleDao.exists(Example.of(t))) {
                dao.roleDao.save(t)
            }
        }
    }

    fun initAdmin() {
        val admin = User(name = adminName, status = NORMAL, isEnable = TRUE)
        if (!dao.userDao.exists(Example.of(admin))) {
            dao.userDao.save(admin.apply { password = passwordEncoder().encode(adminPassword) })
        }
    }

    fun setAdminAllRoles() {
        val example = User(name = adminName, status = NORMAL, isEnable = TRUE)
        val optional = dao.userDao.findOne(Example.of(example))
        if (optional.isPresent) {
            val admin = optional.get()
            val set = dao.roleDao.findAll().toMutableSet()
            admin.roleSet = set
            dao.userDao.save(admin)
        }
    }
}