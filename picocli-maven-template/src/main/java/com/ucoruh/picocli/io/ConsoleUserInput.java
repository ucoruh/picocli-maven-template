package com.ucoruh.picocli.io;

import java.io.Console;

public class ConsoleUserInput implements UserInput {

  @Override
  public String readLine(String prompt) {
    Console console = System.console();

    if (console != null) {
      return console.readLine(prompt);
    }

    System.out.print(prompt);
    return new java.util.Scanner(System.in).nextLine();
  }

  @Override
  public char[] readPassword(String prompt) {
    Console console = System.console();

    if (console != null) {
      return console.readPassword(prompt);
    }

    return readLine(prompt).toCharArray(); // Fallback, though not secure
  }
}
