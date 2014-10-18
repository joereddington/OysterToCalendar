package createcalendarevents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * 
 * BEGIN:VCALENDAR VERSION:2.0 PRODID:-//hacksw/handcal//NONSGML v1.0//EN BEGIN:VEVENT
 * UID:uid2@example.com DTSTAMP:19970714T170000Z ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com
 * DTSTART:20140714T170000Z DTEND:20140715T035959Z SUMMARY:Bastille Day Party END:VEVENT
 * BEGIN:VEVENT UID:uid1@example.com DTSTAMP:19970714T170000Z ORGANIZER;CN=John
 * Doe:MAILTO:john.doe@example.com DTSTART:20140717T170000Z DTEND:20140718T035959Z SUMMARY:No Day
 * Party END:VEVENT END:VCALENDAR
 * 
 * 
 * 
 * 
 * 
 */

import java.util.Locale;

import javax.swing.JFileChooser;

public class OysterProcessor {

  protected static String mainString = "";
  private static int count = 1;

  public static void main(String[] args) {
    List<String> lines = readFromChosen();
    produceIcsFile(lines);
    System.out.println(mainString);
  }

  protected static void produceIcsFile(List<String> lines) {
    startVcalendar();
    parseOyster(lines);
    endVcalendar();
  }

  protected static void startVcalendar() {
    mainString +=
        "BEGIN:VCALENDAR\n" + "VERSION:2.0\n" + "PRODID:-//hacksw/handcal//NONSGML v1.0//EN\n";

  }

  protected static void endVcalendar() {
    mainString += "END:VCALENDAR\n";

  }

  protected static void parseOyster(List<String> lines) {
    for (String string : lines) {
      if ((!string.contains("Balance")) || (string.contains("top-up"))) {
        if (!string.equals("")) {
          String[] sections = string.split(",");
          mainString +=
              (createCalendarEntry(changeDateFormat(sections[0]), sections[1].replace(":", ""),
                  sections[2].replace(":", ""), sections[3]));
        }
      }
    }

  }

  private static String changeDateFormat(String orginal) {
    try {
      DateFormat originalFormat;
      if (orginal.contains("-")) {
         originalFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
      }else{
        originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);  
      }
      DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
      Date date;
      date = originalFormat.parse(orginal);
      String formattedDate = targetFormat.format(date); // 20120821
      return formattedDate;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return orginal;
  }

  private static String createCalendarEntry(String date, String startTime, String endTime,
      String message) {
    count++;
    return "BEGIN:VEVENT\n" + "UID:" + date + startTime + count + "\n"
        + "DTSTAMP:19970714T170000Z\n" + "ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com\n"
        + "DTSTART:" + date + "T" + startTime + "00\n" + "DTEND:" + date + "T" + endTime + "59\n"
        + "SUMMARY:" + message + "\n" + "END:VEVENT\n" + "BEGIN:VEVENT\n"
        + "UID:uid1@example.com\n" + "DTSTAMP:19970714T170000Z\n"
        + "ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com\n" + "DTSTART:20140717T170000Z\n"
        + "DTEND:20140718T035959Z\n" + "SUMMARY:No Day Party\n" + "END:VEVENT\n";

  }

  public static List<String> readFromChosen() {
    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      return readLines("" + file.getAbsolutePath());
    } else {
      System.out.println("Canceled");
      throw new AssertionError("File Dialog canceld");
    }
  }

  public static List<String> readLines(String filename) {
    FileReader fileReader;
    try {
      fileReader = new FileReader(filename);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      List<String> lines = new ArrayList<String>();
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        lines.add(line);
      }
      bufferedReader.close();
      return lines;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }
}
