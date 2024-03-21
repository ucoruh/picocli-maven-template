package com.ucoruh.picocli;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ucoruh.picocli.io.MyFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Help.ColorScheme;
import picocli.jansi.graalvm.AnsiConsole;

/**
 * \class PicoCliApp
 * \brief CORUH R&D - Handles Demo operations.
 *
 * Detailed description of the class.
 *
 * Below is the WBS diagram for the PicoCliApp command hierarchy:
 *
 * \startuml
 * !define COMMAND <color:RoyalBlue>
 * !define SUBCOMMAND <color:DarkOrange>
 *
 * skinparam backgroundColor transparent
 * skinparam defaultFontName Courier
 *
 * rectangle "<b>COMMAND</b>\nPicoCliApp" as PicoCliApp {
 *   rectangle "<b>SUBCOMMAND</b>\nRegisterUserCommand" as RegisterUser
 *   rectangle "<b>SUBCOMMAND</b>\nLoginUserCommand" as LoginUser
 *   rectangle "<b>SUBCOMMAND</b>\nLoginUserCommandRetry" as LoginRetry
 *   rectangle "<b>SUBCOMMAND</b>\nSearchFileCommand" as SearchFile
 *   rectangle "<b>SUBCOMMAND</b>\nSendUserMessageCommand" as SendMessage
 * }
 *
 * PicoCliApp --> RegisterUser
 * PicoCliApp --> LoginUser
 * PicoCliApp --> LoginRetry
 * PicoCliApp --> SearchFile
 * PicoCliApp --> SendMessage
 * \enduml
 *
 */
@Command(name = "PicoCliApp", mixinStandardHelpOptions = true, version = "1.0", description = "CORUH R&D - Handles Demo operations.", subcommands = {
  RegisterUserCommand.class, LoginUserCommand.class, LoginUserCommandRetry.class, SearchFileCommand.class,
  SendUserMessageCommand.class
})
public class PicoCliApp {

  private static final String LOGO_IMAGE = "/coruh_logo_logoname_whitebg.png";

  private static final Logger logger = LoggerFactory.getLogger(PicoCliApp.class);

  public static void main(String... args) {
    logger.info("PicoCliApp started...");

    try {
      AsciiArtGenerator.generateAsciiArtColorized(getFullPath(LOGO_IMAGE), 60, 20);
    } catch (IOException | URISyntaxException e) {
      logger.error("Cannot Find Logo " + LOGO_IMAGE + " continue operations...", e);
    }

    int exitCode;

    try (AnsiConsole ansi = AnsiConsole.windowsInstall()) {
      ColorScheme colorScheme = new ColorScheme.Builder().commands(Style.bold, Style.underline)
      .options(Style.fg_yellow).parameters(Style.fg_yellow).optionParams(Style.italic)
      .errors(Style.fg_red, Style.bold).stackTraces(Style.italic).applySystemProperties().build();
      CommandLine.IFactory factory = new MyFactory();
      exitCode = new CommandLine(new PicoCliApp(), factory).setColorScheme(colorScheme).execute(args);
    }

    System.exit(exitCode);
  }

  public static String getFullPath(String filePath) throws URISyntaxException {
    Path path = Paths.get(filePath);

    if (path.isAbsolute()) {
      return filePath;
    } else {
      String jarFilePath = new File(PicoCliApp.class.getProtectionDomain().getCodeSource().getLocation().toURI())
      .getPath();
      File binDir = new File(jarFilePath);
      return new File(binDir.getParent(), filePath).getPath();
    }
  }

  //    public static String getFullPath(String filePath) throws URISyntaxException {
  //        Path path = Paths.get(filePath);
  //        if (path.isAbsolute()) {
  //            return filePath;
  //        } else {
  //            return combinePaths(getWorkingDirectory(), filePath);
  //        }
  //    }
  //
  //    public static String combinePaths(String... paths) {
  //        if (paths.length == 0) {
  //            return "";
  //        }
  //
  //        File combined = new File(paths[0]);
  //
  //        int i = 1;
  //        while (i < paths.length) {
  //            combined = new File(combined, paths[i]);
  //            ++i;
  //        }
  //
  //        return combined.getPath();
  //    }
  //
  //    public static String getWorkingDirectory() throws URISyntaxException {
  //        String jarFilePath = new File(PicoCliApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
  //        File binDir = new File(jarFilePath);
  //        return ifNullThenUse(binDir.getParent(), "");
  //    }
  //
  //    public static String ifNullThenUse(String fiIfNullCheckValue, String fiDefaultValue) {
  //        if (fiIfNullCheckValue == null) {
  //            return fiDefaultValue;
  //        }
  //        return fiIfNullCheckValue;
  //    }
}
