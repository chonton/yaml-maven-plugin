package org.honton.chas.properties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/** Write Properties */
@Mojo(name = "write", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class WriteYaml extends AbstractMojo {

  @Parameter(property = "project", readonly = true)
  protected MavenProject project;

  /** Yaml filename */
  @Parameter(property = "yaml.filename", required = true)
  String filename;

  /** Path prefix */
  @Parameter(property = "yaml.path", required = true)
  String path;

  /** Value map to save */
  @Parameter Map<String, String> values;

  /** Skip writing properties */
  @Parameter(property = "yaml.skip")
  boolean skip;

  public void execute() throws MojoExecutionException {

    if (skip) {
      getLog().info("Skipping");
      return;
    }

    Path buildDirectory = Path.of(project.getBuild().getDirectory());
    Path location = buildDirectory.resolve(filename);
    try {
      Files.createDirectories(location.getParent());
    } catch (IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }

    writeYaml(location, getPrefixedValues());
  }

  private Map<String, ?> getPrefixedValues() {
    Map<String, ?> prefixedValues = values;
    if (path != null) {
      // a.b.c ==>
      // 3) map = c,map
      // 2) map = b,c,map
      // 1) map = a,b,c,map
      String[] segments = path.split("\\.");
      for (int i = segments.length; --i >= 0; ) {
        prefixedValues = Map.of(segments[i], prefixedValues);
      }
    }
    return prefixedValues;
  }

  private void writeYaml(Path location, Map<String, ?> prefixedValues)
      throws MojoExecutionException {
    getLog().debug("Saving " + location);

    try (BufferedWriter bw = Files.newBufferedWriter(location)) {
      DumperOptions dumperOptions = new DumperOptions();
      dumperOptions.setPrettyFlow(true);
      dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

      new Yaml(dumperOptions).dump(prefixedValues, bw);
      getLog().debug("Saved " + location);
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to write " + location, e);
    }
  }
}
