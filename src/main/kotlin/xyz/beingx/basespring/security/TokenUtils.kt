package xyz.beingx.basespring.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Example
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.NORMAL
import xyz.beingx.basespring.entity.sys.TokenRecord
import xyz.beingx.basespring.ext.currentUser
import java.util.*

@Component
class TokenUtils {
    //Token到期天数
    @Value("\${jwt.expireDays}")
    private lateinit var expireDays: String

    //Token私钥
    @Value("\${jwt.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.claims.username}")
    private lateinit var claimsUserName: String

    @Bean
    private fun algorithm() = Algorithm.HMAC512(jwtSecret)

    @Autowired
    private lateinit var dao: Dao

    fun createNewToken(userDetails: UserDetails): String? {
        val instance = Calendar.getInstance()
        instance.add(Calendar.DAY_OF_YEAR, expireDays.toInt())
        val extTime = instance.time

        // 生成token
        val header = HashMap<String, Any>(2)
        header["typ"] = "JWT"
        header["alg"] = "HMAC512"

        val token = JWT.create()
                .withHeader(header)
                .withClaim(claimsUserName, userDetails.username)
                .withExpiresAt(extTime)
                .sign(algorithm())

        deleteToken()

        dao.tokenRecordDao.save(TokenRecord(userName = userDetails.username, token = token, status = NORMAL))

        return token
    }

    fun getUserNameFromToken(token: String): String? {
        return try {
            val verifier = JWT.require(algorithm()).build()
            val jwt = verifier.verify(token)
            val userName = jwt.claims[claimsUserName]?.asString()
            if (dao.tokenRecordDao.exists(Example.of(TokenRecord(userName = userName, token = token)))) {
                userName
            } else {
                null
            }
        } catch (e: Exception) {
//            logger().error(e.message, e)
            null
        }
    }

    fun deleteToken() {
        val name = currentUser()?.username
        if(name.isNullOrEmpty()) return
        dao.tokenRecordDao.findAll(Example.of(
                TokenRecord(userName = name)
        )).also {
            dao.tokenRecordDao.deleteAll(it)
        }
    }


}