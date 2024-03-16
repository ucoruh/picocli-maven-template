package com.ucoruh.picocli;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/**
 * Command-line interface for user registration.
 */
@CommandLine.Command(name = "registeruser", mixinStandardHelpOptions = true, version = "PicoCliApp 1.0", description = "@|bold,fg_yellow User Registration |@ Requires Username and Password.")
public class RegisterUserCommand implements Runnable {

  // Option for specifying the name
  @NotNull(message = "Name cannot be null")
  @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
  @Option(names = { "-n", "--name" }, description = "mandatory", required = true)
  private String name;

  // Option for specifying the age
  @Min(value = 18, message = "Age should not be less than 18")
  @Max(value = 150, message = "Age should not be greater than 150")
  @Option(names = { "-a", "--age" }, description = "between 18-150", required = true)
  private int age;

  // Option for specifying the email
  @Email(message = "Email should be valid")
  @Option(names = { "-e", "--email" }, description = "valid email", required = true)
  private String email;

  // Option for specifying the password
  @NotNull(message = "Password cannot be null")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
           message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long")
  @Option(names = { "-p",
                    "--password"
                  }, description = "valid password", arity = "0..1", interactive = true, required = true)
  private String password;

  // Option for specifying the country
  @NotNull(message = "Country cannot be null")
  @Option(names = { "-c", "--country" }, description = "default country", defaultValue = "Turkey", required = true)
  private String country;

  // Option for specifying the city
  @NotNull(message = "City cannot be null")
  @Size(min = 2, message = "City name must be at least 2 characters long")
  @Option(names = { "-ci", "--city" }, description = "default city ${DEFAULT-VALUE}")
  private String city = "Rize";

  // CommandSpec to access the command specification
  @Spec
  CommandSpec spec;

  public RegisterUserCommand() {
  }

  /**
   * Returns a string representation of the user details.
   *
   * @return a string representation of the user details
   */
  @Override
  public String toString() {
    return String.format("User{name='%s', age=%d, email='%s', password='%s', country='%s', city='%s'}", name, age,
                         email, password, country, city);
  }

  @Override
  public void run() {
    Set<ConstraintViolation<RegisterUserCommand>> violations = validate();

    if (!violations.isEmpty()) {
      // Handle validation errors
      printValidationErrors(violations);
      return; // Halt further execution
    }

    System.out.println("User registration successful.");
    // Continue with the rest of the registration logic here...
  }

  /**
   * Validates the command-line options and returns the set of constraint
   * violations.
   */
  private Set<ConstraintViolation<RegisterUserCommand>> validate() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    return validator.validate(this);
  }

  /**
   * Prints validation error messages.
   *
   * @param violations Set of constraint violations.
   */
  private void printValidationErrors(Set<ConstraintViolation<RegisterUserCommand>> violations) {
    System.err.println("Validation errors:");

    for (ConstraintViolation<RegisterUserCommand> violation : violations) {
      System.err.println("ERROR: " + violation.getMessage());
    }
  }
}
