# All killing tests to CSV Plugin 

Adds support to pitest for exporting detailed CSV files. Transform last column (first killing test) into a variable size column (killing tests)

## Forked from [JUnit 5 plugin](https://github.com/pitest/pitest-junit5-plugin)

## Usage

The plugin has been built against JUnit platform 1.5.0 - you may encounter issues if you use it with a different version. 

To activate the plugin it must be placed on the classpath of the pitest tool (**not** on the classpath of the project being mutated).

### Maven

```xml
    <plugins>
      <plugin>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-maven</artifactId>
        <version>1.4.9</version>
        <configuration>
            <fullMutationMatrix>true</fullMutationMatrix>
            <outputFormats>
             <param>CSV</param>
            </outputFormats>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-detailed-csv-plugin</artifactId>
            <version>0.3-SNAPSHOT</version>
          </dependency>
        </dependencies>
      </plugin>
   </plugins>
```
For Pitest configuration options, have a look at http://pitest.org/quickstart/maven/.

### Gradle

```
buildscript {
   repositories {
       mavenCentral()
   }
   configurations.maybeCreate("pitest")
   dependencies {
       classpath 'info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.4.5'
       pitest 'org.pitest:pitest-detailed-csv-plugin:0.3'
   }
}

apply plugin: "info.solidsoft.pitest"

pitest {
    pitestVersion = "1.4.9"
    targetClasses = ['our.base.package.*']  // by default "${project.group}.*"
    fullMutationMatrix = true
    outputFormats = ['CSV']
}
```
See [gradle-pitest-plugin documentation](http://gradle-pitest-plugin.solidsoft.info/) for more configuration options.

## About

Plugin originally created by @victorgv.
