package com.ucoruh.picocli;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ucoruh.picocli.io.UserInput;
import com.ucoruh.picocli.io.UserOutput;

import static org.mockito.Mockito.*;

public class LoginUserCommandRetryTest {

  @Mock
  private UserInput userInputMock;
  @Mock
  private UserOutput userOutputMock;

  private LoginUserCommandRetry command;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    command = new LoginUserCommandRetry(userInputMock, userOutputMock);
  }

  @Test
  public void testMaxUserTriesExceededWithMock() {
    // This test case simulates 5 incorrect username inputs followed by a
    // termination response.
    when(userInputMock.readLine(anyString())).thenReturn("wronguser", "Y", "wronguser", "Y", "wronguser", "Y",
        "wronguser", "Y", "wronguser", "Y", "N");
    command.run();
    // Verify that the "Maximum tries for user reached" error message is printed
    // exactly once.
    verify(userOutputMock, times(1)).printErr("Maximum tries for user reached, Operation Terminated!");
  }

  @Test
  public void testMaxPasswordTriesExceededWithMock() {
    // Simulate correct username input first, followed by incorrect password inputs
    // and 'Y' responses to continue, then 'N' to stop after the 3rd attempt.
    when(userInputMock.readLine(anyString()))
    .thenReturn("admin", "Y", "Y", "Y", "N"); // Assuming the first prompt is for the username, followed by continuation prompts
    when(userInputMock.readPassword(anyString()))
    .thenReturn("wrongpass".toCharArray(), "wrongpass".toCharArray(), "wrongpass".toCharArray()); // Simulate 3 incorrect password attempts
    command.run();
    // Verify that the "Maximum tries for password reached" error message is printed exactly once.
    verify(userOutputMock, times(1)).printErr("Maximum tries for password reached, Operation Terminated!");
  }
}
