package xyz.beingx.basespring.api.controller

import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import xyz.beingx.basespring.api.model.RequestUser
import xyz.beingx.basespring.api.model.ResponseLocation
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.EntityBoolean
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.NORMAL
import xyz.beingx.basespring.entity.TRUE
import xyz.beingx.basespring.entity.sys.Role
import xyz.beingx.basespring.entity.sys.User
import xyz.beingx.basespring.ext.currentUser
import xyz.beingx.basespring.security.Roles


@Api(tags = ["用户"])
@RequestMapping("/users")
@RestController
class UserController {
    @Autowired
    private lateinit var dao: Dao

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @ApiOperation("查询指定ID用户", authorizations = [Authorization(Roles.user)])
    @ApiResponses(ApiResponse(code = 200, message = "成功"), ApiResponse(code = 404, message = "找不到指定的用户"))
    @GetMapping("/{id}")
    fun user(@PathVariable id: Long): ResponseEntity<User> {
        val optional = dao.userDao.findOne(Example.of(User(id = id)))
        if (optional.isPresent) {
            return ResponseEntity.ok(optional.get())
        }
        return ResponseEntity.notFound().build()
    }

    @ApiOperation("查询所有用户", authorizations = [Authorization(Roles.user)])
    @ApiResponses(ApiResponse(code = 200, message = "成功"))
    @ApiImplicitParams(ApiImplicitParam(name = "page", value = "页码，从0开始", dataType = "Int", example = "1"),
            ApiImplicitParam(name = "size", value = "每页数量", dataType = "Int", example = "15"),
            ApiImplicitParam(name = "excludeSelf", value = "排除当前登录用户", dataType = "Boolean", example = "true"))
    @GetMapping
    fun users(page: Int?, size: Int?, excludeSelf: Boolean?): ResponseEntity<Page<User>> {
        val where = Specification.where<User> { root, query, criteriaBuilder ->
            val list = arrayListOf(
                    criteriaBuilder.equal(root.get<User>("status").`as`(EntityStatus::class.java), NORMAL),
                    criteriaBuilder.equal(root.get<User>("isEnable").`as`(EntityBoolean::class.java), TRUE)
            )
            if (excludeSelf != false) {
                list.add(criteriaBuilder.notEqual(root.get<User>("name").`as`(String::class.java), currentUser()?.username))
            }
            criteriaBuilder.and(*list.toArray(Array(list.size) { i -> list[i] }))
        }

        val list = dao.userDao.findAll(where, PageRequest.of(page ?: 0, size
                ?: Int.MAX_VALUE, Sort(Sort.Direction.ASC, "id")))
        return ResponseEntity.ok().body(list)
    }

    @ApiOperation("创建新用户（注册）")
    @ApiResponses(ApiResponse(code = 200, message = "成功"), ApiResponse(code = 409, message = "失败，账号已被注册"))
    @PostMapping
    fun post(@RequestBody requestUser: RequestUser): ResponseEntity<ResponseLocation> {
        if (dao.userDao.exists(Example.of(User(name = requestUser.name)))) {
            ResponseEntity.unprocessableEntity()
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseLocation("账号已被注册"))
        }

        val user = User(
                name = requestUser.name,
                password = passwordEncoder.encode(requestUser.password),
                isEnable = TRUE,
                status = NORMAL)

        val optional = dao.roleDao.findOne(Example.of(Role(name = Roles.user)))
        if (optional.isPresent) {
            user.roleSet.add(optional.get())
        }

        dao.userDao.save(user)

        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id).toUri()
        return ResponseEntity.ok().body(ResponseLocation(location = location.toString()))
    }


}