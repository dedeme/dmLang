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
package dmjava.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Class for synchronizing tasks.</p>
 * <p>
 * Tasks are subclasses of Runnable and they are executed as same order as are
 * send.</p>
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class Actor extends Thread {

  ArrayList<Runnable> poolBase = new ArrayList<>();
  List<Runnable> pool = Collections.synchronizedList(poolBase);
  boolean isKill = false;
  boolean isKilled = false;
  long timeOut = 0;
  Runnable timeOutAction = null;

  public Actor() {
  }

  /**
   *
   * @param timeOut Actor will be kill after 'timeOut' milliseconds without
   * activity. 'timeOut' negative or zero does not have effect.
   * @param action Action which will be executed when timeOut.
   */
  public Actor(long timeOut, Runnable action) {
    if (timeOut < 0) {
      this.timeOut = 0;
    } else {
      this.timeOut = timeOut / 5;
      timeOutAction = action;
    }
  }

  /**
   * Kills Actor
   */
  public void kill() {
    isKill = true;
  }

  /**
   * Kills Actor and wait to its dead
   */
  public void killAndWait() {
    try {
      isKill = true;
      while (!isKilled) {
        sleep(5);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Returns if 'this' has been killed
   *
   * @return
   */
  public boolean isDead() {
    return isKill;
  }

  @Override
  public void run() {
    int counter = 0;
    try {
      for (;;) {
        if (pool.size() > 0) {
          pool.get(0).run();
          pool.remove(0);
          counter = 0;
        } else if (isKill) {
          break;
        } else {
          if (timeOut > 0) {
            if (counter == timeOut) {
              isKill = true;
              timeOutAction.run();
            } else {
              ++counter;
              sleep(5);
            }
          } else {
            sleep(5);
          }
        }
      }
      isKilled = true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Sends a command and returns.
   *
   * @param command
   * @throws IllegalStateException if Actor was killed
   */
  public void send(Runnable command) {
    if (isKill) {
      try {
        throw new IllegalStateException("Actor is dead");
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
    } else {
      pool.add(command);
    }
  }

  /**
   * Sends a command and wait for its execution.
   *
   * @param command
   * @throws IllegalStateException if Actor was killed
   */
  public void waitFor(Runnable command) {
    if (isKill) {
      try {
        throw new IllegalStateException("Actor is dead");
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
    } else {
      Command com = new Command(command);
      pool.add(com);
      while (!com.finished) {
        try {
          sleep(5);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}

class Command implements Runnable {

  Runnable action;
  boolean finished;

  Command(Runnable rn) {
    action = rn;
    finished = false;
  }

  @Override
  public void run() {
    action.run();
    finished = true;
  }
}
