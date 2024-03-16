package com.ucoruh.picocli;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Command-line interface for user login with password.
 */
@CommandLine.Command(name = "login", mixinStandardHelpOptions = true, version = "PicoCliApp 1.0", description = "@|bold,fg_yellow User Login with Password |@ Requires Username and Password.")
public class LoginUserCommand implements Callable<Integer> {

  // Option for specifying the username
  @Option(names = { "-u", "--user" }, description = "User name", arity = "0..1", interactive = true)
  private String user;

  // Option for specifying the password
  @Option(names = { "-p", "--password" }, description = "Passphrase", arity = "0..1", interactive = true)
  private char[] password;

  /**
   * Executes the login command.
   *
   * @return 0 for success, 1 for missing username or password, 2 for failure to
   *         hash password
   * @throws Exception if an error occurs during execution
   */
  @Override
  public Integer call() throws Exception {
    if (user == null || password == null || password.length == 0) {
      System.err.println("Username or password is missing.");
      return 1; // Indicate failure
    }

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(new String(password).getBytes()); // Convert char[] to String
      String hashedPassword = base64(md.digest());
      System.out.printf("Hi %s, your password is hashed to %s.%n", user, hashedPassword);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Error: Unable to hash the password.");
      e.printStackTrace();
      return 2; // Indicate failure
    }

    finally {
      // Clear sensitive data
      clearPassword();
    }

    return 0; // Indicate success
  }

  /**
   * Base64 encodes the given byte array.
   *
   * @param arr the byte array to encode
   * @return the base64-encoded string
   */
  private String base64(byte[] arr) {
    return Base64.getEncoder().encodeToString(arr);
  }

  /**
   * Clears the password array.
   */
  private void clearPassword() {
    if (password != null) {
      Arrays.fill(password, ' '); // Clear password array
    }
  }
}
