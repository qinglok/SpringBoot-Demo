package xyz.beingx.basespring.security

/**
 * 所有角色
 */
object Roles {
    const val master = "MASTER"
    const val manager = "MANAGER"

    val all = arrayOf(
            master,
            manager)
}