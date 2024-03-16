package com.ucoruh.picocli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import static org.junit.Assert.assertEquals;

public class LoginUserCommandTest {

  private LoginUserCommand loginUserCommand;
  private CommandLine commandLine;

  @Before
  public void setUp() {
    loginUserCommand = new LoginUserCommand();
    commandLine = new CommandLine(loginUserCommand);
  }

  @After
  public void tearDown() {
    // Cleanup resources if necessary
  }

  @Test
  public void testSuccessLogin() {
    String[] args = {"-u", "testUser", "-p", "testPass"};
    int exitCode = commandLine.execute(args);
    assertEquals("Exit code should be 0 for successful login", 0, exitCode);
  }

  @Test
  public void testMissingUsername() {
    String[] args = {"-p", "testPass"};
    int exitCode = commandLine.execute(args);
    assertEquals("Exit code should be 1 for missing username", 1, exitCode);
  }

  @Test
  public void testMissingPassword() {
    String[] args = {"-u", "testUser"};
    int exitCode = commandLine.execute(args);
    assertEquals("Exit code should be 1 for missing password", 1, exitCode);
  }

  // Additional tests can be written for other failure conditions or specific requirements.
}
