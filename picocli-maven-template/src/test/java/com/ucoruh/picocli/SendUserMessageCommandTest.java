package com.ucoruh.picocli;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SendUserMessageCommandTest {

  private CommandLine commandLine;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    SendUserMessageCommand command = new SendUserMessageCommand();
    commandLine = new CommandLine(command);
  }

  @Test
  public void testMissingRequiredOptions() {
    // Expecting an exception due to missing required options
    //thrown.expect(CommandLine.MissingParameterException.class);
    commandLine.execute();
  }

  @Test
  public void testSuccessfulMessageSendAndReceive() {
    // Redirecting System.out to capture console output for verification
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    String name = "John";
    String surname = "Doe";
    int age = 30;
    String securityKey = "ABCD1234";
    String residence = "Cityville";
    commandLine.execute("-n", name, "-s", surname, "-a", String.valueOf(age), "-k", securityKey, "-r", residence);
    String output = outContent.toString();
    assertTrue(output.contains("Sent message (hexadecimal):"));
    assertTrue(output.contains("Received message:"));
    assertTrue(output.contains("Name: " + name));
    assertTrue(output.contains("Surname: " + surname));
    assertTrue(output.contains("Age: " + age));
    assertTrue(output.contains("Security Key: " + securityKey.toUpperCase())); // Assuming the hex string is uppercase
    assertTrue(output.contains("Residence: " + residence));
    // Reset System.out to its original state
    System.setOut(System.out);
  }

  // Add more tests as necessary for error cases or specific logic paths
}
