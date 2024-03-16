package com.ucoruh.picocli;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import picocli.CommandLine;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SearchFileCommandTest {

  private CommandLine commandLine;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() {
    SearchFileCommand command = new SearchFileCommand();
    commandLine = new CommandLine(command);
  }

  @Test
  public void testNoFileTypeSpecified() {
    //thrown.expect(ParameterException.class);
    //thrown.expectMessage("At least one file type option must be specified.");
    commandLine.execute();
  }

  @Test
  public void testXmlFileSearch() throws Exception {
    File tempFile = folder.newFile("test.xml");
    int exitCode = commandLine.execute("--xml", tempFile.getAbsolutePath());
    assertTrue("Search should be successful for XML file", exitCode == 0);
  }


}
