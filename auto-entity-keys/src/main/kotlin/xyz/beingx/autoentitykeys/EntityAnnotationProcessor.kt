package xyz.beingx.autoentitykeys

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic.Kind.ERROR


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class EntityAutoKey

@SupportedAnnotationTypes("xyz.beingx.autoentitykeys.EntityAutoKey")
@SupportedOptions(EntityAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityAnnotationProcessor : AbstractProcessor() {
    private var elementUtils: Elements? = null

    private lateinit var kaptKotlinGeneratedDir: String

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        elementUtils = processingEnv?.elementUtils
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {


        roundEnv?.getElementsAnnotatedWith(EntityAutoKey::class.java)?.let { annotatedElements ->
            if (annotatedElements.isEmpty()) return false

            kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
                processingEnv.messager.printMessage(
                        ERROR,
                        "Can't find the target directory for generated Kotlin files."
                )
                return false
            }

            val stringBuilder = StringBuilder()
            stringBuilder.appendln("package xyz.beingx.autoentitykeys")
            stringBuilder.appendln()
            stringBuilder.appendln("object E {")
            stringBuilder.appendln()
            annotatedElements.forEach { element ->
                stringBuilder.appendln(createDataModel(element))
            }
            stringBuilder.appendln("}")
            write(kaptKotlinGeneratedDir + File.separator + "E.kt", stringBuilder.toString())
            return true
        }

        return false
    }

    private fun createDataModel(element: Element): StringBuilder {
        // 获取声明element的全限定类名
        val typeElement = element as TypeElement
        val sb = StringBuilder()

        // 获取包名
        val packageName = elementUtils?.getPackageOf(typeElement)?.qualifiedName.toString()
        // 根据旧Java类名创建新的Java文件
        val className = typeElement.qualifiedName.toString().substring(packageName.length + 1)
        val newClassName = "E_$className"

        sb.appendln("    object $newClassName {")

        typeElement.enclosedElements.forEach { field ->
            if (field.kind.isField) {
                sb.appendln("        const val ${field.simpleName} = \"${field.simpleName}\"")
            }
        }

        sb.appendln("    }")

        return sb
    }

    private fun write(name: String, content: String) {
        val writer = OutputStreamWriter(
                FileOutputStream(name),
                "UTF-8"
        )
        // 写入
        writer.write(content)
        writer.close()
    }

}