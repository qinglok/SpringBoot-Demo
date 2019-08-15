package xyz.beingx.basespring.entity.sys

import xyz.beingx.basespring.entity.EntityBoolean
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.base.BaseEntity
import javax.persistence.*

/**
 * 用户
 */
@Entity
@Table(name = "users")
class User(
        @Column(nullable = false, unique = true)
        var name: String? = null,

        @Column(nullable = false)
        var password: String? = null,

        @Column
        @Enumerated(EnumType.ORDINAL)
        var isEnable: EntityBoolean? = null,

        @ManyToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
        @JoinTable(
                name = "users_roles",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
        var roleSet: Set<Role>? = null,

        status: EntityStatus? = null
) : BaseEntity(status = status)