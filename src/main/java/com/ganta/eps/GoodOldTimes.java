package com.ganta.eps;

import static com.ganta.eps.FileUtils.NEW_LINE;
import static com.ganta.eps.FileUtils.isAlphaNumeric;
import static com.ganta.eps.FileUtils.read;
import static com.ganta.eps.FileUtils.updateCount;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GoodOldTimes {

  public static Map<String, Integer> getWordFrequency(final String filename)
      throws URISyntaxException, IOException {
    List<Object> data = new ArrayList<>();
    // data[0] holds the stop words
    data.add(read("stop_words.txt").get(0).split(","));
    // data[1] is line (max 80 characters)
    data.add("");
    // data[2] is index of the start_char of word
    data.add(null);
    // data[3] is index on characters, i = 0
    data.add(0);
    // data[4] is flag indicating if word was found
    data.add(false);
    // data[5] is the word
    data.add("");
    // data[6] is word,NNNN
    data.add("");
    // data[7] is frequency
    data.add(0);

    var f = read(filename).iterator();
    var millis = System.currentTimeMillis();
    while (f.hasNext()) {
      data.add(1, f.next() + NEW_LINE);
      data.add(2, null);
      data.add(3, 0);
      while ((int) data.get(3) < ((String) data.get(1)).length()) {
        if (data.get(2) == null) {
          if (isAlphaNumeric(((String) data.get(1)).toCharArray()[(int) data.get(3)])) {
            data.set(2, data.get(3));
          }
        } else if (!isAlphaNumeric(((String) data.get(1)).toCharArray()[(int) data.get(3)])) {
          data.set(4, false);
          data.set(5,
              ((String) data.get(1)).substring((int) data.get(2), (int) data.get(3)).toLowerCase());

          if (((String) data.get(5)).length() >= 2 && ! Arrays.asList((String[]) data.get(0))
              .contains(data.get(5))) {
            final var g = read("word_freqs_" + millis).iterator();
            while (g.hasNext()) {
              data.set(6, g.next());
              data.set(7, Integer.valueOf(((String) data.get(6)).split(",")[1]));
              data.set(6, ((String) data.get(6)).split(",")[0]);
              if ((((String) data.get(5)).equalsIgnoreCase((String)data.get(6)))) {
                data.set(7, (int) data.get(7) + 1);
                data.set(4, true);
                break;
              }
            }

            if (!((boolean)data.get(4))) {
              updateCount("word_freqs_" + millis, (String) data.get(5));
            } else {
              updateCount("word_freqs_" + millis, (String) data.get(5), (int) data.get(7));
            }
          }
          data.set(2, null);
        }
        data.set(3, (int) data.get(3) + 1);
      }
    }
    data.clear();
    IntStream.range(0, 25).forEach(i -> data.add(new ArrayList<>()));

    // data[25] is word, freq from file
    data.add("");
    // data[26] is freq
    data.add(0);
    // data[27] is index
    data.add(0);
    final var h = read("word_freqs_" + millis).iterator();
    while (h.hasNext()) {
      data.set(25, h.next());
      data.set(26, Integer.valueOf(((String) data.get(25)).split(",")[1]));
      data.set(25, ((String) data.get(25)).split(",")[0]);
      data.set(27, 0);
      while ((int) data.get(27) < 25) {
        if (((ArrayList<Object>) (data.get((int) data.get(27)))).isEmpty()
            || (int)((ArrayList<Object>) data.get((int) data.get(27))).get(1) < (int) data
            .get(26)) {
          data.add((int)data.get(27), new ArrayList<>(Arrays.asList(data.get(25), data.get(26))));
          data.remove(25);
          break;
        }
        data.set(27, (int)data.get(27) + 1);
      }
    }

    return data.stream().limit(25)
        .map(it -> (ArrayList<Object>)it)
        .filter(it -> !it.isEmpty())
        .collect(Collectors.toMap(it -> (String)it.get(0), it -> (int)it.get(1)));

  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    Map<String, Integer> frequencyMap = getWordFrequency("input.txt");
    System.out.println(frequencyMap);
  }
}
