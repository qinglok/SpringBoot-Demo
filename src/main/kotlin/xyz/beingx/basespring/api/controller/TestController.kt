package xyz.beingx.basespring.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import java.text.SimpleDateFormat
import java.util.*

@ApiIgnore
@RequestMapping("/test")
@RestController
class TestController {

    @PostMapping
    fun test(@RequestBody date : In){
        println(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.data))
        println(date.data)
    }

    class In(
            var data : Date
    )

    @GetMapping
    fun test2() : ResponseEntity<Date>{
        return ResponseEntity.ok(Date())
    }
}