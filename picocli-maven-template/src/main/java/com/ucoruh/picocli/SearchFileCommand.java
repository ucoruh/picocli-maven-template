package com.ucoruh.picocli;

import java.io.File;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

/**
 * Command-line interface for searching files with different formats. Allows
 * searching for XML, CSV, and JSON files using various options.
 */
@Command(name = "searchfile", mixinStandardHelpOptions = true, version = "PicoCliApp 1.0", description = "Search for files using various options.")
public class SearchFileCommand implements Runnable {

  /**
   * Option to specify searching for XML files.
   */
  @Option(names = "--xml", description = "Search for XML files")
  private File xmlFile;

  /**
   * Option to specify searching for CSV files.
   */
  @Option(names = "--csv", description = "Search for CSV files")
  private File csvFile;

  /**
   * Option to specify searching for JSON files.
   */
  @Option(names = "--json", description = "Search for JSON files")
  private File jsonFile;

  /**
   * Command specification.
   */
  @Spec
  private CommandSpec spec;

  /**
   * Executes the search operation based on the provided options.
   */
  @Override
  public void run() {
    validate();
    // Remaining business logic here
  }

  /**
   * Validates the provided options. Throws a ParameterException if no file types
   * are specified.
   */
  private void validate() {
    if (noFilesSpecified()) {
      throw new ParameterException(spec.commandLine(), "At least one file type option must be specified.");
    }
  }

  /**
   * Checks if no file types are specified for searching.
   *
   * @return true if no file types are specified, false otherwise
   */
  private boolean noFilesSpecified() {
    return xmlFile == null && csvFile == null && jsonFile == null;
  }
}
