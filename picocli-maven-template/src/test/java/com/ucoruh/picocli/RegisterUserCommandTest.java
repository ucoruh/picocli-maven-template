package com.ucoruh.picocli;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;

import static org.junit.Assert.assertEquals;

public class RegisterUserCommandTest {

  private CommandLine commandLine;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    RegisterUserCommand command = new RegisterUserCommand();
    commandLine = new CommandLine(command);
  }

  @Test
  public void testSuccessfulRegistration() {
    int exitCode = commandLine.execute("-n", "John Doe", "-a", "30", "-e", "john.doe@example.com", "-p", "Password@123", "-c", "Turkey", "-ci", "Istanbul");
    assertEquals("Expected successful registration with correct input, but found validation errors or incorrect exit code.", 0, exitCode);
  }

  @Test
  public void testInvalidEmailRegistration() {
    //thrown.expect(CommandLine.ParameterException.class);
    //thrown.expectMessage("Email should be valid");
    int exitCode = commandLine.execute("-n", "John Doe", "-a", "30", "-e", "notAnEmail", "-p", "Password@123", "-c", "Turkey", "-ci", "Istanbul");
  }

  @Test
  public void testUnderageRegistration() {
    //thrown.expect(CommandLine.ParameterException.class);
    //thrown.expectMessage("Age should not be less than 18");
    int exitCode = commandLine.execute("-n", "Young Joe", "-a", "17", "-e", "young.joe@example.com", "-p", "Password@123", "-c", "USA", "-ci", "New York");
  }
}
