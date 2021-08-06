# All killing tests to CSV Plugin 

Adds support to pitest for exporting detailed CSV files. Transform last column (first killing test) into a variable size column (killing tests)

## Based on [JUnit 5 plugin](https://github.com/pitest/pitest-junit5-plugin)

## Compatibility

DetailedCSV 4.1 works on PIT from 1.4.1 to 1.6.1
DetailedCSV 5.0 works on PIT from 1.6.2 and above

## Usage

In a terminal run:
```shell script
git clone https://github.com/victorgveloso/Detailed-CSV-Report-PITest.git
cd Detailed-CSV-Report-PITest
mvn clean build install
```
After it you should have our plugin installed on your `.m2` directory.

### Maven

```xml
  <plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.5.2</version>                                # Use the latest pitest maven plugin version
    <configuration>
      <targetClasses>
        <param>your.package.*</param>
      </targetClasses>
      <targetTests>
        <param>your.package.*</param>
      </targetTests>
      <fullMutationMatrix>true</fullMutationMatrix>         # Required for DetailedCSV outputFormat config
      <outputFormats>
        <param>DetailedCSV</param>                          # This is how pitest identifies our plugin
        <param>XML</param>                                  # Required for fullMutationMatrix
      </outputFormats>
      <threads>8</threads>                                  # Recommended: how many cores your cpu have
      <testPlugin>junit5</testPlugin>                       # Available values: junit, testng, junit5
    </configuration>
    <dependencies>
      <dependency>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-junit5-plugin</artifactId>       # Required for junit5 testPlugin config
        <version>0.12</version>
      </dependency>
      <dependency>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-detailed-csv-plugin</artifactId> # Add DetailedCSV as Pitest's dependency
        <version>0.4.1-SNAPSHOT</version>
      </dependency>
    </dependencies>
  </plugin>
```
For Pitest configuration options, have a look at [Pitest maven plugin Quickstart](http://pitest.org/quickstart/maven/).

### Gradle

```
plugins {
	id "info.solidsoft.pitest" version '1.5.1'          // Use the latest pitest gradle plugin version
}

pitest {
	testPlugin = 'junit5'                               // Available values: junit, testng, junit5
	targetClasses = ['your.project.package.*']			//Include all repo's packages
	targetTests = ['your.project.package.*']
    fullMutationMatrix = true                           // Required for DetailedCSV outputFormat config
    outputFormats = ['XML','DetailedCSV']               // XML is required for fullMutationMatrix, this is how pitest identifies our plugin
    mutators = ['DEFAULTS']                             // See more in https://pitest.org/quickstart/mutators/
    threads = 8                                         // Recommended: how many cores your cpu have
}

repositories {
    mavenCentral()
    mavenLocal()                                        // Required to allow gradle fetch installed packages on .m2 dir                                                                                                                    //Enable local repo (cache and DetailedCSV location)
    gradlePluginPortal()
    maven { url "https://repo.spring.io/release" }
}
dependencies {
    pitest 'org.pitest:pitest-detailed-csv-plugin:0.4.1-SNAPSHOT'    // Add DetailedCSV as Pitest's dependency
    pitest 'org.pitest:pitest-junit5-plugin:0.12'         // Required for junit5 testPlugin config
}
```
See [gradle-pitest-plugin documentation](http://gradle-pitest-plugin.solidsoft.info/) for more configuration options.

## About

Plugin originally created by @victorgv.
