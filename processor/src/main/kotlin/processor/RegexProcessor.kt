package processor

import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("annotations.Extract")
class RegexProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val classMethodMap = mutableMapOf<TypeElement, MutableList<ExecutableElement>>()

        for (element in roundEnv.getElementsAnnotatedWith(Extract::class.java)) {
            if (element is ExecutableElement) {
                val enclosingClass = element.enclosingElement as TypeElement
                classMethodMap.computeIfAbsent(enclosingClass) { mutableListOf() }.add(element)
            }
        }

        for ((classElement, methods) in classMethodMap) {
            generateExtractorClass(classElement, methods)
        }

        return true
    }

    private fun generateExtractorClass(classElement: TypeElement, methods: List<ExecutableElement>) {
        val packageName = processingEnv.elementUtils.getPackageOf(classElement).toString()
        val originalClassName = classElement.simpleName.toString()
        val extractorClassName = "${originalClassName}Extractor"

        val classBuilder = TypeSpec.classBuilder(extractorClassName)
            .superclass(ClassName(packageName, originalClassName))
            .addSuperclassConstructorParameter("input")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("input", String::class)
                    .build()
            )

        for (method in methods) {
            val methodName = method.simpleName.toString()
            val regex = method.getAnnotation(Extract::class.java)?.regex ?: ""

            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addStatement("val match = Regex(%S).find(input)", regex)
                .addStatement("return match?.groupValues?.get(1)")

            classBuilder.addFunction(methodBuilder.build())
        }

        val file = FileSpec.builder(packageName, extractorClassName)
            .addType(classBuilder.build())
            .build()

        try {
            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
            if (kaptKotlinGeneratedDir != null) {
                file.writeTo(File(kaptKotlinGeneratedDir))
            } else {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "kapt.kotlin.generated not found")
            }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Error generating Kotlin file: ${e.message}")
        }
    }
}