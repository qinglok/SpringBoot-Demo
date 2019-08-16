package xyz.beingx.basespring.security

/**
 * 所有角色
 */
object Roles {
    const val master = "MASTER"
    const val manager = "MANAGER"
    const val user = "USER"

    val all = arrayOf(
            master,
            manager,
            user)
}