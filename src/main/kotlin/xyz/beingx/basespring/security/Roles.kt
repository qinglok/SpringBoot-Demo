package xyz.beingx.basespring.security

/**
 * 所有角色
 */
object Roles{
    const val MASTER = "MASTER"
    const val MANAGER = "MANAGER"
    const val USER = "USER"

    val allRoles = arrayOf(
            MASTER,
            MANAGER,
            USER
    )
}