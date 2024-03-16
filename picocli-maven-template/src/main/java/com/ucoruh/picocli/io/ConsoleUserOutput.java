package com.ucoruh.picocli.io;

public class ConsoleUserOutput implements UserOutput {

  @Override
  public void println(String message) {
    System.out.println(message);
  }

  @Override
  public void printErr(String message) {
    System.err.println(message);
  }
}
