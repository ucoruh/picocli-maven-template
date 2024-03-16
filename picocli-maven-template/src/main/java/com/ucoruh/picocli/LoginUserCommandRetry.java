package com.ucoruh.picocli;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.ucoruh.picocli.io.UserInput;
import com.ucoruh.picocli.io.UserOutput;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Command-line interface for user login with password retry mechanism.
 */
@CommandLine.Command(name = "logintry", mixinStandardHelpOptions = true, version = "PicoCliApp 1.0", description = "@|bold,fg_yellow User Login with Password |@ Requires Username and Password.")
public class LoginUserCommandRetry implements Runnable {

  private static final String DEFAULT_PASSWORD = "Test@1234"; // for demonstration purpose...
  private static final String DEFAULT_USER = "admin"; // for demonstration purpose...

  // Option for specifying the username
  @Option(names = { "-u", "--user" }, description = "User name", arity = "0..1", interactive = true)
  private String user;

  // Option for specifying the password
  @Option(names = { "-p", "--password" }, description = "Passphrase", arity = "0..1", interactive = true)
  private String password;

  private final UserInput userInput;
  private final UserOutput userOutput;

  // Constructor accepting UserInput and UserOutput
  public LoginUserCommandRetry(UserInput userInput, UserOutput userOutput) {
    this.userInput = userInput;
    this.userOutput = userOutput;
  }

  /**
   * Runs the login command.
   */
  @Override
  public void run() {
    if (!authenticateUser()) {
      return;
    }

    if (!authenticatePassword()) {
      return;
    }

    userOutput.println("Login successful.");
    String hashedPassword = hashPassword(DEFAULT_PASSWORD); // Hash the default password
    userOutput.println("Hi " + hashedPassword + ", your password is hashed to " + user);
  }

  /**
   * Authenticates the user.
   *
   * @return true if authentication is successful, false otherwise
   */
  private boolean authenticateUser() {
    int attempts = 0;
    // Prompt for the user at the start of the loop to ensure 'user' is not null
    // when checked.
    user = userInput.readLine("Enter value for --user: ");

    while (true) {
      if (!user.equals(DEFAULT_USER)) {
        if (attempts >= 5) {
          userOutput.printErr("Maximum tries for user reached, Operation Terminated!");
          return false;
        }

        userOutput.printErr("Provided user '" + user + "' not found!");
        String continueInput = userInput.readLine("Do you want to continue? (Y/N): ");

        if (!continueInput.equalsIgnoreCase("Y")) {
          userOutput.printErr("Operation Terminated!");
          return false;
        }

        // Reprompt for the user after providing the option to continue.
        user = userInput.readLine("Enter value for --user: ");
        attempts++;
      } else {
        userOutput.println("You provided user '" + user + "'");
        return true;
      }
    }
  }

  /**
   * Authenticates the password.
   *
   * @return true if authentication is successful, false otherwise
   */
  private boolean authenticatePassword() {
    int attempts = 0;

    while (true) {
      if (password == null || !password.equals(DEFAULT_PASSWORD)) {
        if (attempts >= 3) {
          userOutput.printErr("Maximum tries for password reached, Operation Terminated!");
          return false;
        }

        userOutput.printErr("Provided password wrong!");
        String continueInput = userInput.readLine("Do you want to continue? (Y/N): ");

        if (!continueInput.equalsIgnoreCase("Y")) {
          userOutput.printErr("Operation Terminated!");
          return false;
        }

        password = new String(userInput.readPassword("Enter value for --password: "));
        attempts++;
      } else {
        // Clear the password after successful authentication
        clearPassword();
        return true;
      }
    }
  }

  /**
   * Hashes the given password using SHA-256 algorithm.
   *
   * @param password the password to hash
   * @return the hashed password encoded in Base64
   */
  private String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(password.getBytes());
      byte[] hashedBytes = md.digest();
      return Base64.getEncoder().encodeToString(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Clears the password from memory.
   */
  private void clearPassword() {
    if (password != null) {
      password = null;
    }
  }
}
