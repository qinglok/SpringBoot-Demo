package xyz.beingx.basespring.entity.sys

import com.fasterxml.jackson.annotation.JsonIgnore
import xyz.beingx.basespring.entity.EntityBoolean
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.base.BaseEntity
import xyz.beingx.autoentitykeys.EntityAutoKey
import javax.persistence.*

/**
 * 用户
 */
@Entity
@Table(name = "users")
@EntityAutoKey
class User(
        @Column(nullable = false, unique = true)
        var name: String? = null,

        @JsonIgnore
        @Column(nullable = false)
        var password: String? = null,

        @Column
        @Enumerated(EnumType.ORDINAL)
        var isEnable: EntityBoolean? = null,

        @JsonIgnore
        @ManyToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
        @JoinTable(
                name = "users_roles",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
        var roleSet: MutableSet<Role> = mutableSetOf(),

        id: Long? = null,
        status: EntityStatus? = null
) : BaseEntity(id = id, status = status)