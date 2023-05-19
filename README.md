# yaml-maven-plugin

Write yaml file.

# Plugin

Plugin reports available at [plugin info](https://chonton.github.io/yaml-maven-plugin/plugin-info.html).

There is a single goal: [write](https://chonton.github.io/yaml-maven-plugin/write-mojo.html),
which by default binds to the **generate-resources** phase. This goal writes the values specified in the configuration
into a yaml file at the filename location.

## Configuration

| Parameter | CmdLine  Property | Description                         |
|----------:|:-----------------:|:------------------------------------|
|  filename |   yaml.filename   | Filename to write yaml              |
|      path |         -         | Dot separated path to prefix values |
|      yaml |         -         | Map of values to write              |                                                                                          |
|      skip |     yaml.skip     | Skip writing yaml                   |

# Examples

## Typical Use

```xml

<build>
  <pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.honton.chas</groupId>
        <artifactId>yaml-maven-plugin</artifactId>
        <version>0.0.1</version>
      </plugin>
    </plugins>
  </pluginManagement>

  <plugins>
    <plugin>
      <groupId>org.honton.chas</groupId>
      <artifactId>yaml-maven-plugin</artifactId>
      <executions>
        <execution>
          <goals>
            <goal>write</goal>
          </goals>
        </execution>
      </executions>
      <configuration>
        <filename>helm/env.yaml</filename>
        <prefix>pipeline</prefix>
        <values>
          <appVersion>${project.version}</appVersion>
          <environment>development</environment>
          <namespace>${group.namespace}</namespace>
          <region>us-east-2</region>
          <theatre>us</theatre>
        </values>
      </configuration>
    </plugin>
  </plugins>
</build>
```
