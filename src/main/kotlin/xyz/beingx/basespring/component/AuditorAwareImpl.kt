package xyz.beingx.basespring.component

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import xyz.beingx.basespring.ext.currentUser
import java.util.*

@Configuration
class AuditorAwareImpl : AuditorAware<String> {

    /**
     * 给Entity中的 @CreatedBy  @LastModifiedBy 注入操作人
     */
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(currentUser()?.username)
    }

}