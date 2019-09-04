package com.ganta.eps;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

  public static final String NEW_LINE = "\n";

  public static List<String> read(final String filename) throws URISyntaxException, IOException {
    var path = path(filename);
    if (Files.notExists(path)) {
      return new ArrayList<>();
    }
    return Files.readAllLines(path);
  }

  public static void updateCount(final String filename, final String word)
      throws URISyntaxException, IOException {
    var path = path(filename);
    if (Files.notExists(path)) {
      Files.write(path, new byte[0], StandardOpenOption.WRITE, StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    }
    Files.write(path, (word + ",1" + NEW_LINE).getBytes(), StandardOpenOption.APPEND);

  }

  public static void updateCount(final String filename, final String word, final int count)
      throws URISyntaxException, IOException {
    final var path = path(filename);

    try (final RandomAccessFile randomAccessFile =
        new RandomAccessFile(path.toAbsolutePath().toFile(), "rw")) {

      var line = "$";
      while (line != null && !line.isBlank()) {
        line = randomAccessFile.readLine();
        if (line.matches(word + ",\\d*")) {
          break;
        }
      }
      var position = randomAccessFile.getFilePointer();
      if (hasMoreCharacters(count)) {
        byte[] buffer = new byte[(int) (randomAccessFile.length() - randomAccessFile
            .getFilePointer())];

        randomAccessFile.read(buffer);
        randomAccessFile.seek(position - line.length() - 1);
        randomAccessFile.write((word + "," + count + NEW_LINE).getBytes());
        randomAccessFile.write(buffer);
      } else {
        randomAccessFile.seek(position - line.length() - 1);
        byte[] buffer = (word + "," + count).getBytes();
        randomAccessFile.write(buffer, 0, buffer.length);
      }
    }
  }


  public static boolean isAlphaNumeric(Character c) {
    return Character.isDigit(c) || Character.isLetter(c);
  }

  private static boolean hasMoreCharacters(final int count) {
    final var log10 = Math.log10((double) count);
    return Math.floor(log10) == log10;
  }

  private static Path path(final String filename) throws URISyntaxException {
    var root = FileUtils.class.getResource("/");
    var parent = Paths.get(root.toURI());
    return Paths.get(parent.toAbsolutePath().toString(), filename);
  }
}
