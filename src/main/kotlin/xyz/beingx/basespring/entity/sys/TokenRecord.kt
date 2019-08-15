package xyz.beingx.basespring.entity.sys

import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "token_records")
class TokenRecord(
    @Column
    var userName: String? = null,

    @Column
    var token: String? = null,

    status: EntityStatus? = null
) : BaseEntity(status = status)
