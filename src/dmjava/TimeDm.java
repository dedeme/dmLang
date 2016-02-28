/*
 * Copyright 05-abr-2014 ºDeme
 *
 * This file is part of 'dmLang-8.1.0'.
 *
 * 'dmLang-8.1.0' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * 'dmLang-8.1.0' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 'dmLang-8.1.0'.  If not, see <http://www.gnu.org/licenses/>.
 */
package dmjava;

import static dmjava.Translator.__;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @version 1.0
 * @since 05-Apr-2014
 * @author deme
 */
public class TimeDm {

  private TimeDm() {
  }

  /**
   * Constructor.
   *
   * @return
   */
  public static GregorianCalendar mk() {
    return new GregorianCalendar();
  }

  /**
   * Constructor
   *
   * @param gc
   * @return
   */
  public static GregorianCalendar mk(GregorianCalendar gc) {
    return (GregorianCalendar) gc.clone();
  }

  /**
   * Constructor
   *
   * @param hour
   * @param minute
   * @param second
   * @param millisecond
   * @return
   */
  public static GregorianCalendar mk(
    int hour, int minute, int second, int millisecond) {
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(Calendar.HOUR_OF_DAY, hour);
    gc.set(Calendar.MINUTE, minute);
    gc.set(Calendar.SECOND, second);
    gc.set(Calendar.MILLISECOND, millisecond);
    return gc;
  }

  /**
   * Constructor
   *
   * @param millisAll
   * @return
   */
  public static GregorianCalendar mk(int millisAll) {
    int h = millisAll / 3600000;
    millisAll = millisAll % 3600000;
    int m = millisAll / 60000;
    millisAll = millisAll % 60000;
    return mk(h, m, millisAll / 1000, millisAll % 1000);
  }

  /**
   * Constructor with millisecond to 0
   *
   * @param hour
   * @param minute
   * @param second
   * @return
   */
  public static GregorianCalendar mk(
    int hour, int minute, int second) {
    return mk(hour, minute, second, 0);
  }

  /**
   * Constructor.
   *
   * @param tm It can be like 'hh:mm:ss,lll' or 'hh:mm:ss'. Numbers can have any
   * number of digits. Hour > 23, mm > 59, ss > 59 and lll > 999 are not valid.
   * @return
   * @throws IllegalArgumentException
   */
  public static GregorianCalendar mk(String tm)
    throws IllegalArgumentException {
    IllegalArgumentException ex = new IllegalArgumentException(
      __("Time_'%s':_Format_is_not_valid.", tm));
    if (tm.length() < 5) {
      throw ex;
    }
    if (tm.charAt(tm.length() - 1) == ',') {
      throw ex;
    }

    try {
      String dv1[] = tm.split(",");
      if (dv1.length > 2) {
        throw ex;
      }
      int mll = 0;
      if (dv1.length > 1) {
        mll = new Integer(dv1[1]);
      }
      if (mll < 0 || mll > 999) {
        throw ex;
      }

      String dv2[] = dv1[0].split(":");
      if (dv2.length == 1 && dv1[0].length() == 6) {
        String tmp = dv1[0];
        dv2 = new String[3];
        dv2[0] = tmp.substring(0, 2);
        dv2[1] = tmp.substring(2, 4);
        dv2[2] = tmp.substring(4);
      }
      if (dv2.length != 3) {
        throw ex;
      }
      int h = new Integer(dv2[0]);
      if (h < 0 || h > 23) {
        throw ex;
      }
      int m = new Integer(dv2[1]);
      if (m < 0 || m > 59) {
        throw ex;
      }
      int s = new Integer(dv2[2]);
      if (s < 0 || s > 59) {
        throw ex;
      }

      return mk(h, m, s, mll);
    } catch (IllegalArgumentException e) {
      throw ex;
    }
  }

  /**
   * Only compare hours, minutes, seconds and milliseconds.
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static boolean eq(GregorianCalendar gc1, GregorianCalendar gc2) {
    return millisAll(gc1) == millisAll(gc2);
  }

  /**
   * Only compare hours, minutes, seconds and milliseconds. Sort must be done
   * with '[millisAll(gc)]', '[sqlFormat(gc)]' or '[longSqlFormat(gc)]'.
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static int compare(GregorianCalendar gc1, GregorianCalendar gc2) {
    return millisAll(gc1) - millisAll(gc2);
  }

  /**
   * Total milliseconds that represents the hour.
   *
   * @param gc
   * @return
   */
  public static int millisAll(GregorianCalendar gc) {
    return hours(gc) * 3600000 + minutes(gc) * 60000 + seconds(gc) * 1000
      + milliseconds(gc);
  }

  /**
   * Returns the hour part.
   *
   * @param gc
   * @return
   */
  public static int hours(GregorianCalendar gc) {
    return gc.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Returns the minute part.
   *
   * @param gc
   * @return
   */
  public static int minutes(GregorianCalendar gc) {
    return gc.get(Calendar.MINUTE);
  }

  /**
   * Returns the second part.
   *
   * @param gc
   * @return
   */
  public static int seconds(GregorianCalendar gc) {
    return gc.get(Calendar.SECOND);
  }

  /**
   * Returns the millisecond part.
   *
   * @param gc
   * @return
   */
  public static int milliseconds(GregorianCalendar gc) {
    return gc.get(Calendar.MILLISECOND);
  }

  /**
   * Returns a new time with hours changed.
   *
   * @param gc
   * @param hours
   * @return
   */
  public static GregorianCalendar setHours(GregorianCalendar gc, int hours) {
    return mk(hours, minutes(gc), seconds(gc), milliseconds(gc));
  }

  /**
   * Returns a new time with hours changed.
   *
   * @param gc
   * @param hours
   * @return
   */
  public static GregorianCalendar addHours(GregorianCalendar gc, int hours) {
    return mk(hours + hours(gc), minutes(gc), seconds(gc), milliseconds(gc));
  }

  /**
   * Returns a new time with minutes changed.
   *
   * @param gc
   * @param minutes
   * @return
   */
  public static GregorianCalendar setMinutes(
    GregorianCalendar gc, int minutes) {
    return mk(hours(gc), minutes, seconds(gc), milliseconds(gc));
  }

  /**
   * Returns a new time with minutes changed.
   *
   * @param gc
   * @param minutes
   * @return
   */
  public static GregorianCalendar addMinutes(
    GregorianCalendar gc, int minutes) {
    return mk(hours(gc), minutes + minutes(gc), seconds(gc), milliseconds(gc));
  }

  /**
   * Returns a new time with seconds changed.
   *
   * @param gc
   * @param seconds
   * @return
   */
  public static GregorianCalendar setSeconds(
    GregorianCalendar gc, int seconds) {
    return mk(hours(gc), minutes(gc), seconds, milliseconds(gc));
  }

  /**
   * Returns a new time with seconds changed.
   *
   * @param gc
   * @param seconds
   * @return
   */
  public static GregorianCalendar addSeconds(
    GregorianCalendar gc, int seconds) {
    return mk(hours(gc), minutes(gc), seconds + seconds(gc), milliseconds(gc));
  }

  /**
   * Returns a new time with milliseconds changed.
   *
   * @param gc
   * @param milliseconds
   * @return
   */
  public static GregorianCalendar setMilliseconds(
    GregorianCalendar gc, int milliseconds) {
    return mk(hours(gc), minutes(gc), seconds(gc), milliseconds);
  }

  /**
   * Returns a new time with milliseconds changed.
   *
   * @param gc
   * @param milliseconds
   * @return
   */
  public static GregorianCalendar addMilliseconds(
    GregorianCalendar gc, int milliseconds) {
    return mk(hours(gc),
      minutes(gc),
      seconds(gc),
      milliseconds + milliseconds(gc));
  }

  /**
   * Returns gc1 - gc2 (round floor)
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static int dfHours(
    GregorianCalendar gc1, GregorianCalendar gc2) {
    return (millisAll(gc1) - millisAll(gc2)) / 3600000;
  }

  /**
   * Returns gc1 - gc2 round floor)
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static int dfMinutes(
    GregorianCalendar gc1, GregorianCalendar gc2) {
    return (millisAll(gc1) - millisAll(gc2)) / 60000;
  }

  /**
   * Returns gc1 - gc2 round floor)
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static int dfSeconds(
    GregorianCalendar gc1, GregorianCalendar gc2) {
    return (millisAll(gc1) - millisAll(gc2)) / 1000;
  }

  /**
   * Returns gc1 - gc2 round floor)
   *
   * @param gc1
   * @param gc2
   * @return
   */
  public static int dfMilliseconds(
    GregorianCalendar gc1, GregorianCalendar gc2) {
    return (millisAll(gc1) - millisAll(gc2));
  }

  // Format a number
  static String fN(int n) {
    return (n < 10) ? ("0" + String.valueOf(n)) : String.valueOf(n);
  }

  /**
   * Returns hh:mm:ss format
   *
   * @param gc
   * @return
   */
  public static String format(GregorianCalendar gc) {
    return String.format("%s:%s:%s",
      fN(hours(gc)),
      fN(minutes(gc)),
      fN(seconds(gc)));
  }

  /**
   * Returns hh:mm:ss,millis format
   *
   * @param gc
   * @return
   */
  public static String formatLong(GregorianCalendar gc) {
    return String.format("%s:%s:%s,%s",
      fN(hours(gc)),
      fN(minutes(gc)),
      fN(seconds(gc)),
      String.valueOf(milliseconds(gc)));
  }

  /**
   * Returns hhmmss format
   *
   * @param gc
   * @return
   */
  public static String formatSql(GregorianCalendar gc) {
    return String.format("%s%s%s",
      fN(hours(gc)),
      fN(minutes(gc)),
      fN(seconds(gc)));
  }

  /**
   * Returns hhmmss,millis format
   *
   * @param gc
   * @return
   */
  public static String formatLongSql(GregorianCalendar gc) {
    return String.format("%s%s%s,%s",
      fN(hours(gc)),
      fN(minutes(gc)),
      fN(seconds(gc)),
      String.valueOf(milliseconds(gc)));
  }
}
