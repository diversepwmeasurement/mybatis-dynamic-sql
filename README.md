# MyBatis Dynamic SQL

[![Build Status](https://github.com/mybatis/mybatis-dynamic-sql/workflows/Java%20CI/badge.svg?branch=master)](https://github.com/mybatis/mybatis-dynamic-sql/actions?query=workflow%3A%22Java+CI%22)
[![Coverage Status](https://coveralls.io/repos/github/mybatis/mybatis-dynamic-sql/badge.svg?branch=master)](https://coveralls.io/github/mybatis/mybatis-dynamic-sql?branch=master)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis.dynamic-sql/mybatis-dynamic-sql/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis.dynamic-sql/mybatis-dynamic-sql)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.mybatis.dynamic-sql/mybatis-dynamic-sql.svg)](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/dynamic-sql/mybatis-dynamic-sql/)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mybatis_mybatis-dynamic-sql&metric=alert_status)](https://sonarcloud.io/dashboard?id=mybatis_mybatis-dynamic-sql)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mybatis_mybatis-dynamic-sql&metric=security_rating)](https://sonarcloud.io/dashboard?id=mybatis_mybatis-dynamic-sql)

## What Is This?
This library is a general purpose SQL generator.  Think of it as a typesafe and expressive SQL DSL (domain specific language),
with support for rendering SQL formatted properly for MyBatis3 and Spring's NamedParameterJDBCTemplate.

The library also contains extensions for Kotlin that enable an idiomatic Kotlin DSL for SQL.

The library will generate full DELETE, INSERT, SELECT, and UPDATE statements. The DSL implemented by the
library is very similar to native SQL but it includes many functions that allow for very dynamic SQL statements.
For example, a typical search can be coded with a query like this (the following code is Kotlin, but Java code is very similar):

```kotlin
   fun search(id: String?, firstName: String?, lastName: String?) =
        select(Customer.id, Customer.firstName, Customer.lastName) {
            from(Customer)
            where(Customer.active, isEqualTo(true))
            and(Customer.id, isEqualToWhenPresent(id).then{ it?.padStart(5, '0') })
            and(Customer.firstName, isLikeCaseInsensitiveWhenPresent(firstName)
                .then{ "%" + it.trim() + "%" })
            and(Customer.lastName, isLikeCaseInsensitiveWhenPresent(lastName)
                .then{ "%" + it.trim() + "%" })
            orderBy(Customer.lastName, Customer.firstName)
            limit(500)
        }
```

This query does quite a lot...

1. It is a search with three search criteria - any combination of search criteria can be used
1. Only records with an active status will be returned
1. If `id` is specified, it will be padded to length 5 with '0' at the beginning of the string
1. If `firstName` is specified, it will be used in a case-insensitive search and SQL wildcards will be appended
1. If `lastName` is specified, it will be used in a case-insensitive search and SQL wildcards will be appended
1. The query results are limited to 500 rows

Using the dynamic SQL features of the library eliminates a lot of code that would be required for checking nulls, adding wild cards, etc. This query clearly expresses the intent of the search in just a few lines.

See the following pages for detailed information:

| Page | Comments|
|------|---------|
|[Quick Start](src/site/markdown/docs/quickStart.md) | Shows a complete example of building code for this library |
|[MyBatis3 Support](src/site/markdown/docs/mybatis3.md) | Information about specialized support for [MyBatis3](https://github.com/mybatis/mybatis-3). The examples on this page are similar to the code generated by [MyBatis Generator](https://github.com/mybatis/generator) |
|[Kotlin Support with MyBatis3](src/site/markdown/docs/kotlinMyBatis3.md) | Information about the Kotlin extensions and Kotlin DSL when using MyBatis3 as the runtime |
|[Spring Support](src/site/markdown/docs/spring.md) | Information about specialized support for Spring JDBC Templates |
|[Kotlin Support with Spring](src/site/markdown/docs/kotlinSpring.md) | Information about the Kotlin extensions and Kotlin DSL when using Spring JDBC Template as the runtime |
|[Spring Batch Support](src/site/markdown/docs/springBatch.md) | Information about specialized support for Spring Batch using the [MyBatis Spring Integration](https://github.com/mybatis/spring) |

The library test cases provide several complete examples of using the library in various different styles:

| Language | Runtime | Comments | Code Directory |
|---|---|---|---|
| Java | MyBatis3 | Example using Java utility classes for MyBatis in the style of MyBatis Generator | [src/test/java/examples/simple](src/test/java/examples/simple) |
| Java | MyBatis3 | Example using Java utility classes for the MyBatis integration with Spring Batch | [src/test/java/examples/springbatch](src/test/java/examples/springbatch) |
| Java | Spring JDBC | Example using Java utility classes for Spring JDBC Template | [src/test/java/examples/spring](src/test/java/examples/spring) |
| Kotlin | MyBatis3 | Example using Kotlin utility classes for MyBatis in the style of MyBatis Generator | [src/test/kotlin/examples/kotlin/mybatis3/canonical](src/test/kotlin/examples/kotlin/mybatis3/canonical) |
| Kotlin | Spring JDBC | Example using Kotlin utility classes for Spring JDBC Template | [src/test/kotlin/examples/kotlin/spring/canonical](src/test/kotlin/examples/kotlin/spring/canonical) |


## Requirements

The library has no dependencies.  Java 8 or higher is required.
