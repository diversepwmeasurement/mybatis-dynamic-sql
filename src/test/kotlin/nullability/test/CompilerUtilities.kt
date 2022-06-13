package nullability.test

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File

/**
 * Compiles the source lines and returns any error reports generated by the compiler.
 */
fun compileIt(sourceLines: List<String>): List<CompilerErrorReport> {
    val file: File = File.createTempFile("KotlinTest", ".kt")
    file.deleteOnExit()
    file.writeText(sourceLines.joinToString(separator = System.lineSeparator()))

    val compilerArgs = K2JVMCompilerArguments().apply {
        freeArgs = listOf(file.path)
        destination = System.getProperty("java.io.tmpdir")
        classpath = System.getProperty("java.class.path")
        noStdlib = true
    }

    return with(CompilerErrorMessageCollector()) {
        K2JVMCompiler().exec(this, Services.EMPTY, compilerArgs)
        this.errors
    }
}

class CompilerErrorMessageCollector: MessageCollector {
    val errors = mutableListOf<CompilerErrorReport>()
     override fun clear() = errors.clear()

    override fun hasErrors() = errors.size > 0

    override fun report(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) {
        if (severity.isError) {
            errors.add(CompilerErrorReport(severity, message, location))
        }
    }
}

fun CompilerErrorReport.matchesExpected(expectedErrorLocations: Array<out ExpectedErrorLocation>) =
    expectedErrorLocations.firstOrNull { location.matchesExpected(it) } != null

fun CompilerMessageSourceLocation?.matchesExpected(expectedErrorLocation: ExpectedErrorLocation) =
    if (this == null) {
        false
    } else {
        line == expectedErrorLocation.line && column == expectedErrorLocation.column
    }

fun List<CompilerErrorReport>.matchCount(vararg expectedErrorLocations: ExpectedErrorLocation) =
    this.filter {
        it.matchesExpected(expectedErrorLocations)
    }.size

data class CompilerErrorReport(
    val severity: CompilerMessageSeverity,
    val message: String,
    val location: CompilerMessageSourceLocation?
)
data class ExpectedErrorLocation(val line: Int, val column: Int)
