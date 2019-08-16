package xyz.beingx.basespring.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.stereotype.Component
import xyz.beingx.basespring.entity.sys.Role
import xyz.beingx.basespring.entity.sys.TokenRecord
import xyz.beingx.basespring.entity.sys.User
import javax.persistence.EntityManager

@Component
class Dao {

    @Autowired
    lateinit var roleDao: RoleDao

    @Autowired
    lateinit var tokenRecordDao: TokenRecordDao

    @Autowired
    lateinit var userDao: UserDao

}

interface RoleDao : BaseRepository<Role, Long>
interface TokenRecordDao : BaseRepository<TokenRecord, Long>
interface UserDao : BaseRepository<User, Long>

@NoRepositoryBean
interface BaseRepository<T, ID> : JpaRepository<T, ID>, JpaSpecificationExecutor<T>

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class BaseRepositoryImpl<T, ID>(domainClass: Class<T>, em: EntityManager)
    : SimpleJpaRepository<T, ID>(domainClass, em), BaseRepository<T, ID>

class BaseRepositoryFactoryBean<R : JpaRepository<T, ID>, T, ID>(c: Class<R>) : JpaRepositoryFactoryBean<R, T, ID>(c) {

    override fun createRepositoryFactory(em: EntityManager): RepositoryFactorySupport {
        return object : JpaRepositoryFactory(em) {
            //设置具体的实现类
            override fun getTargetRepository(information: RepositoryInformation, entityManager: EntityManager) =
                    @Suppress("UNCHECKED_CAST")
                    (BaseRepositoryImpl<T, ID>(information.domainType as Class<T>, entityManager))

            //设置具体的实现类的class
            override fun getRepositoryBaseClass(metadata: RepositoryMetadata) =
                    BaseRepositoryImpl::class.java
        }
    }
}
