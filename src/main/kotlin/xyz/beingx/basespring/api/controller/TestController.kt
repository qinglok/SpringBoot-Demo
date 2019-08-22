package xyz.beingx.basespring.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.LongAdder

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

    private val addr = LongAdder()
    @GetMapping
    fun test2() : ResponseEntity<Date>{
        addr.increment()
        println("time : " + addr.toLong())
        return ResponseEntity.ok(Date())
    }
}