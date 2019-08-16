package xyz.beingx.basespring

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.bind.annotation.RestController

@RunWith(SpringRunner::class)
@SpringBootTest
class BaseSpringApplicationTests {

	@Test
	fun contextLoads() {
		val set = mutableSetOf<String>()
		set.add("a")
		set.add("a")
		println(set)
	}

}
