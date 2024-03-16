package com.ucoruh.picocli.io;

import com.ucoruh.picocli.LoginUserCommandRetry;

import picocli.CommandLine;

public class MyFactory implements CommandLine.IFactory {
  @Override
  public <K> K create(Class<K> cls) throws Exception {
    if (cls == LoginUserCommandRetry.class) {
      return cls.getConstructor(UserInput.class, UserOutput.class)
                .newInstance(new ConsoleUserInput(), new ConsoleUserOutput());
    }

    return CommandLine.defaultFactory().create(cls);
  }
}
