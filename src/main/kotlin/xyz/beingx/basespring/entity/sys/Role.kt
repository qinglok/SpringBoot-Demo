package xyz.beingx.basespring.entity.sys

import com.fasterxml.jackson.annotation.JsonIgnore
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.base.BaseEntity
import javax.persistence.*

/**
 * 角色
 */
@Entity
@Table(name = "roles")
class Role(
        @Column(nullable = false, unique = true)
        var name: String? = null,

        @JsonIgnore
        @ManyToMany(mappedBy = "roleSet", fetch = FetchType.LAZY)
        var userSet: MutableSet<User> = mutableSetOf(),

        id: Long? = null,
        status: EntityStatus? = null
) : BaseEntity(id = id, status = status)