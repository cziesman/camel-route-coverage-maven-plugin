# The Camel Route Coverage Maven plugin

This plugin provides the ability to generate Camel route coverage reports as HTML and as Excel-compatible .xlsx files.

## Usage

To use this plugin, simply add the following to the `pom.xml` file in your Camel project.

    <plugin>
        <groupId>com.redhat</groupId>
        <artifactId>camel-route-coverage-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
    </plugin>

The plugin will execute automatically as part of the `test` phase.

The report files will be generated in the folder `target/route-coverage-report`.