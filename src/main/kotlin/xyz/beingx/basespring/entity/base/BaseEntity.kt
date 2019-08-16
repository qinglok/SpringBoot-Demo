@file:Suppress("unused")

package xyz.beingx.basespring.entity.base


import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import xyz.beingx.basespring.entity.EntityStatus
import java.util.*
import javax.persistence.*

/**
 * 继承自这个类的实体类，createdDate、createdBy、lastModifiedDate、lastModifiedBy会由JPA Auditing自动填充
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column
        var id: Long? = null,

        @Column(nullable = false)
        @Enumerated(EnumType.ORDINAL)
        var status: EntityStatus? = null,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @CreatedDate
        @Column
        var createdDate: Date? = null,

        @CreatedBy
        @Column
        var createdBy: String? = null,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @LastModifiedDate
        @Column
        var lastModifiedDate: Date? = null,

        @LastModifiedBy
        @Column
        var lastModifiedBy: String? = null

)