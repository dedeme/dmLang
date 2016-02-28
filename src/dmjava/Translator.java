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

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>
 * Utilities for translating messages.</p>
 * <p>
 * Files with translations will be in a directory named i18n and their names
 * will be of kind of 'i18n_es.properties'</p>
 * <p>
 * In the main function we will be add the paths of the directories with
 * translations.</p>
 * <p>
 * In the java files we will indicate the text for translating with functions
 * '__ ()'.</p>
 *
 * @version 1.0
 * @since 05-Apr-2014
 * @author deme
 */
public class Translator {

  private Translator() {
  }

  static ArrayList<ResourceBundle> rbs = new ArrayList<>();

  /**
   * Add the path of 'dmjava.resources.i18n'
   */
  public static void addPath() {
    addPath("dmjava/resources/i18n");
  }

  /**
   * Add paths with translations.
   *
   * @param path The path relative to default class loader. e.g.
   * "myprogram/resources/i18n"
   */
  public static void addPath(String path) {
    rbs.add(ResourceBundle.getBundle(
      path + "/" + "i18n", Locale.getDefault(), Util.classLoader()
    ));
  }

  /**
   * Add paths with translations relative to indicate ClassLoader.
   *
   * @param classLoader
   * @param path
   */
  public static void addPath(ClassLoader classLoader, String path) {
    rbs.add(ResourceBundle.getBundle(
      path + "/" + "i18n", Locale.getDefault(), classLoader
    ));
  }

  /**
   * Returns value of 'key'
   *
   * @param key
   * @return
   */
  public static String __(String key) {
    for (ResourceBundle rb : rbs) {
      try {
        String r = rb.getString(key);
        if (!r.equals("")) {
          return r;
        }
      } catch (MissingResourceException e) {
      }
    }
    return key;
  }

  /**
   * Returns value of 'key' of similar way that 'String.format'
   *
   * @param key
   * @param args
   * @return
   */
  public static String __(String key, Object... args) {
    return String.format(__(key.replace(" ", "\\ ")), args);
  }

}
