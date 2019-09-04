package com.ganta.eps;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CommonTest {

  private static Stream<Arguments> data() {
    return Stream.of(
        Arguments.of("test.txt", Map.of(
            "acquaintance", 1,
            "suppose", 1,
            "sure", 1,
            "know", 1
        )),
        Arguments.of("input.txt", Map.of(
            "white", 1,
            "tigers", 1,
            "live", 2,
            "mostly", 2,
            "india", 1,
            "wild", 1,
            "lions", 1,
            "africa", 1
        )),
        Arguments.of("pride-and-prejudice.txt", Map.ofEntries(
            Map.entry("mr", 786),
            Map.entry("elizabeth", 635),
            Map.entry("very", 488),
            Map.entry("darcy", 418),
            Map.entry("such", 395),
            Map.entry("mrs", 343),
            Map.entry("much", 329),
            Map.entry("more", 327),
            Map.entry("bennet", 323),
            Map.entry("bingley", 306),
            Map.entry("jane", 295),
            Map.entry("miss", 283),
            Map.entry("one", 275),
            Map.entry("know", 239),
            Map.entry("before", 229),
            Map.entry("herself", 227),
            Map.entry("though", 226),
            Map.entry("well", 224),
            Map.entry("never", 220),
            Map.entry("sister", 218),
            Map.entry("soon", 216),
            Map.entry("think", 211),
            Map.entry("now", 209),
            Map.entry("good", 201),
            Map.entry("time", 203
            ))
        ));
  }

  // GoodOldTimes 699.088s
  @ParameterizedTest
  @MethodSource("data")
  public void testWordFrequency(final String filename, final Map<String, Integer> frequencyMap)
      throws URISyntaxException, IOException {
    final Map<String, Integer> actualFrequencyMap = GoodOldTimes.getWordFrequency(filename);
    Assertions.assertThat(actualFrequencyMap).containsAllEntriesOf(frequencyMap);
  }

}
