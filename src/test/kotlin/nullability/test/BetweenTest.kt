/*
 *    Copyright 2016-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package nullability.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BetweenTest {
    @Test
    fun `Test That First Null Causes Compile Error`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom

            fun testFunction() {
                countFrom(person) {
                    where { id isBetween null and 4 }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(ExpectedErrorLocation(9, 30)))
    }

    @Test
    fun `Test That Second Null Causes Compile Error`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom

            fun testFunction() {
                countFrom(person) {
                    where { id isBetween 4 and null }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(ExpectedErrorLocation(9, 36)))
    }

    @Test
    fun `Test That Both Null Causes Compile Errors`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom

            fun testFunction() {
                countFrom(person) {
                    where { id isBetween null and null }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(
            ExpectedErrorLocation(9, 30),
            ExpectedErrorLocation(9, 39)
        ))
    }

    @Test
    fun `Test That First Null In Elements Method Causes Compile Error`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom
            import org.mybatis.dynamic.sql.util.kotlin.elements.isBetween

            fun testFunction() {
                countFrom(person) {
                    where { id (isBetween<Int>(null).and(4)) }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(ExpectedErrorLocation(10, 36)))
    }

    @Test
    fun `Test That Second Null In Elements Method Causes Compile Error`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom
            import org.mybatis.dynamic.sql.util.kotlin.elements.isBetween

            fun testFunction() {
                countFrom(person) {
                    where { id (isBetween(4).and(null)) }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(ExpectedErrorLocation(10, 38)))
    }

    @Test
    fun `Test That Both Null In Elements Method Causes Compile Error`() {
        val sourceLines = """
            package temp.kotlin.test
            
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.id
            import examples.kotlin.mybatis3.canonical.PersonDynamicSqlSupport.person
            import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom
            import org.mybatis.dynamic.sql.util.kotlin.elements.isBetween

            fun testFunction() {
                countFrom(person) {
                    where { id (isBetween<Int>(null).and(null)) }
                }
            }
        """.trimIndent().lines()

        val compilerMessageCollector = compile(sourceLines)
        assertThat(compilerMessageCollector.errorLocations()).isEqualTo(listOf(
            ExpectedErrorLocation(10, 36),
            ExpectedErrorLocation(10, 46)
        ))
    }
}
