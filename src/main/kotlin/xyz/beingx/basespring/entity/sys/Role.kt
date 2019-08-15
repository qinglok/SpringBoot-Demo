package xyz.beingx.basespring.entity.sys

import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

/**
 * 角色
 */
@Entity
@Table(name = "roles")
class Role(
        @Column(nullable = false, unique = true)
        var name: String? = null,

        @ManyToMany(mappedBy = "roleSet")
        var userSet: Set<User>? = null,

        status: EntityStatus? = null
) : BaseEntity(status = status)