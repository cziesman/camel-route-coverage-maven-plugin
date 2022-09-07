# The Camel Route Coverage Maven plugin

This plugin provides the ability to generate Camel route coverage reports as HTML and as Excel-compatible .xlsx files.

## Usage

The plugin works in conjunction with the [Camel Report Maven Plugin](https://camel.apache.org/manual/camel-report-maven-plugin.html). The Camel Report Maven Plugin generates the XML files that are then converted to HTML and .xlsx files by this plugin.

The following example shows how to add the plugin to your Camel project.

    <project>
      ...
      <build>
        <!-- To define the plugin version in your parent POM -->
        <pluginManagement>
          <plugins>
            <plugin>
              <groupId>com.redhat</groupId>
              <artifactId>camel-route-coverage-maven-plugin</artifactId>
              <version>1.0-SNAPSHOT</version>
            </plugin>
            ...
          </plugins>
        </pluginManagement>
        <!-- To use the plugin goals in your POM or parent POM -->
        <plugins>
          <plugin>
            <groupId>com.redhat</groupId>
            <artifactId>camel-route-coverage-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
          </plugin>
          ...
        </plugins>
      </build>
      ...
    </project>

The plugin will execute automatically as part of the `test` phase.

The plugin can be run manually using the following command:

    mvn camel-route-coverage:report

The report files will be generated in the folder `target/route-coverage-report`.