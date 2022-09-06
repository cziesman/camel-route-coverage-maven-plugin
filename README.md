# The Camel Route Coverage Maven plugin

This plugin provides the ability to generate Camel route coverage reports as HTML and as Excel-compatible .xlsx files.

## Usage

To use this plugin, simply add the following to your `pom.xml`.

    <plugin>
        <groupId>com.redhat</groupId>
        <artifactId>camel-route-coverage-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
    </plugin>

The plugin will execute automatically as part of the `test` phase.