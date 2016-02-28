/*
 * Copyright 07-abr-2014 ºDeme
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

import static dmjava.Func.*;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class App {
  private App() {
  }

  static File appName = null;

  /**
   * <p>
   * Initializes an application. It must be called before the rest of
   * functions.</p>
   * <p>
   * It only can be called once.</p>
   *
   * @param appName Identifier for application. It is used for creating the
   * return of 'homeDir'. It will be created below user home. (e.g.
   * ".dmJavaApp/test"
   */
  public static void init(File appName) {
    if (App.appName != null) {
      throw new IllegalStateException("app.init only can be called once");
    }
    App.appName = appName;
  }

  /**
   * Returns home directory of application
   *
   * @return
   */
  public static File home() {
    return Io.file(System.getProperty("user.home"), appName.toString());
  }

  /**
   * Write a file named 'fileList" in a directory. This file has information of
   * files and subdirectories.
   * @param directory
   */
  public static void filesList(final File directory) {
    try (Io.Out out = new Io.Out(Io.file(directory, "fileList.txt"))) {
      out.write(It.from(directory.list()).filter(
        Fn.negate(Fn.eq("fileList.txt"))).map(
          (String p) -> (Io.file(directory, p).isDirectory())
            ? "D" + p
            : "F" + p));
    }
  }

  /**
   * Write <b>recursively</b> a file named 'fileList" in a directory and its
   * subdirectories. This file has information of files and subdirectories.
   * @param directory
   */
  public static void filesLists(File directory) {
    for (File f : directory.listFiles()) {
      if (f.isDirectory()) {
        filesLists(f);
      }
    }
    filesList(directory);
  }

  /**
   * <p>
   * Returns a resource list from a resource directory.</p>
   * <p>
   * List has next format:</p>
   * <p>
   * D<i>directory</i><br>or<br>F<i>file</i>.</p>
   * <p>
   * Is necessary to have saved data using filesList or filesLists.</p>
   * @param resourcesDir
   * @return
   */
  public static ArrayList<String> resourcesList(String resourcesDir) {
    try (Io.In in = new Io.In(ClassLoader.getSystemResource(
      resourcesDir + "/fileList.txt"))) {
      return in.readLines().toList();
    }
  }

  /**
   * Uses resourcesList to copy all files and directories in 'resources' dir to
   * 'targetDir'.
   * @param resourcesDir
   * @param targetDir
   */
  public static void copyResources(String resourcesDir, File targetDir) {
    It.from(resourcesList(resourcesDir)).each((rs) -> {
      String name = rs.substring(1);
      if (rs.charAt(0) == 'D') {
        File newDir = Io.file(targetDir, name);
        newDir.mkdir();
        copyResources(resourcesDir + "/" + name, newDir);
      } else {
        Io.copy(ClassLoader.getSystemResource(resourcesDir + "/" + name),
          Io.file(targetDir, name));
      }
    });
  }

}
