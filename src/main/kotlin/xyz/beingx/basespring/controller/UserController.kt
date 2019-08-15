package xyz.beingx.basespring.controller

import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import xyz.beingx.basespring.dao.Dao
import xyz.beingx.basespring.entity.EntityBoolean
import xyz.beingx.basespring.entity.EntityStatus
import xyz.beingx.basespring.entity.sys.User
import xyz.beingx.basespring.security.Roles


@Api(tags = ["用户"])
@RequestMapping("/users")
@RestController
class UserController {
    @Autowired
    private lateinit var dao: Dao

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long){

    }

    @ApiOperation("创建新用户（注册）")
    @ApiResponses(
            ApiResponse(code = 201, message = "成功", responseHeaders = [ResponseHeader(name = "Location", response = String::class)]),
            ApiResponse(code = 400, message = "账号已经被使用")
    )
    @PostMapping
    fun post(@RequestBody requestUser: RequestUser): ResponseEntity<*> {
        if (dao.userDao.exists(Example.of(User(name = requestUser.name)))) {
            return ResponseEntity.badRequest().body("该账号已经被使用！")
        }

        val user = User(
                name = requestUser.name,
                password = passwordEncoder.encode(requestUser.password),
                isEnable = EntityBoolean.TRUE,
                status = EntityStatus.NORMAL)

        dao.userDao.save(user)

        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id).toUri()
        return ResponseEntity.created(location).body(null)
//        return ResponseEntity.ok("注册成功")
    }

}