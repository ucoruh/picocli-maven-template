package com.ucoruh.picocli;

import java.io.IOException;

import com.ucoruh.communication.CommunicationMessage;
import com.ucoruh.communication.MessageEncoderDecoder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Command-line interface for sending and receiving a user message. Allows users
 * to specify their name, surname, age, security key, and residence for sending
 * a message.
 */
@Command(name = "sendusermessage", mixinStandardHelpOptions = true, version = "PicoCliApp 1.0", description = "Send and receive a user message.")
public class SendUserMessageCommand implements Runnable {

  /**
   * User's name.
   */
  @Option(names = { "-n", "--name" }, description = "Name", required = true)
  private String name;

  /**
   * User's surname.
   */
  @Option(names = { "-s", "--surname" }, description = "Surname", required = true)
  private String surname;

  /**
   * User's age.
   */
  @Option(names = { "-a", "--age" }, description = "Age", required = true)
  private int age;

  /**
   * Security key in hexadecimal format.
   */
  @Option(names = { "-k", "--key" }, description = "Security Key (in hexadecimal format)", required = true)
  private String securityKey;

  /**
   * User's residence.
   */
  @Option(names = { "-r", "--residence" }, description = "Residence", required = true)
  private String residence;

  @Override
  public void run() {
    try {
      // Encode the message
      CommunicationMessage message = new CommunicationMessage();
      message.name = name;
      message.surname = surname;
      message.age = age;
      message.securityKey = hexStringToByteArray(securityKey);
      message.residence = residence;
      byte[] encodedMessage = MessageEncoderDecoder.encodeMessage(message);
      // Emulate sending the encoded message (printing it as hexadecimal string)
      System.out.println("Sent message (hexadecimal): " + bytesToHex(encodedMessage));
      // Emulate receiving the message and decoding it
      CommunicationMessage receivedMessage = MessageEncoderDecoder.decodeMessage(encodedMessage);
      // Print the received message
      System.out.println("Received message:");
      System.out.println("Name: " + receivedMessage.name);
      System.out.println("Surname: " + receivedMessage.surname);
      System.out.println("Age: " + receivedMessage.age);
      System.out.println("Security Key: " + bytesToHex(receivedMessage.securityKey));
      System.out.println("Residence: " + receivedMessage.residence);
    } catch (IllegalAccessException | IOException e) {
      System.err.println("Error sending/receiving message: " + e.getMessage());
    }
  }

  // Helper method to convert hexadecimal string to byte array
  private byte[] hexStringToByteArray(String hexString) {
    int len = hexString.length();
    byte[] data = new byte[len / 2];

    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                            + Character.digit(hexString.charAt(i + 1), 16));
    }

    return data;
  }

  // Helper method to convert byte array to hexadecimal string
  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();

    for (byte b : bytes) {
      sb.append(String.format("%02X", b));
    }

    return sb.toString();
  }

  public static void main(String[] args) {
    new CommandLine(new SendUserMessageCommand()).execute(args);
  }
}
