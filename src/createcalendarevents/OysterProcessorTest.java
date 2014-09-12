package createcalendarevents;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class OysterProcessorTest extends OysterProcessor {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() {
    List<String> lines = readLines("testFiles/source.csv");
    produceIcsFile(lines);
    assertEquals(mainString, readFromFile("testFiles/target.ics"));
  }

  protected static String readFromFile(String filename) {
    String input = "";
    try {
      Scanner inputScanner = new Scanner(new File(filename));
      if (inputScanner.hasNext()) {
        input = inputScanner.useDelimiter("\\A").next();
      }
      inputScanner.close();
    } catch (FileNotFoundException ex) {
      System.err.printf("Unable to open input file %s\n", filename);
      System.exit(1);
    }

    return input;
  }


}
