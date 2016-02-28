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

import dmjava.It;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Utility thread-safe for killing Actors groups.</p>
 * <p>
 * Example:</p>
 * <pre>
 * ActorGroup group = new ActorGroup();
 * group.start();
 * ...
 * Actor actor1 = new Actor();
 * group.add(actor1);
 * actor1.start();
 * ...
 * Actor actor2 = new Actor();
 * group.add(actor2);
 * actor2.start();
 * ...
 * ...
 * ...
 * group.kill();  // It kills every actor and kills itself
 * </pre>
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class ActorGroup extends Actor {

  ArrayList<Actor> actorsPoolBase = new ArrayList<>();
  List<Actor> actorPool = Collections.synchronizedList(actorsPoolBase);

  /**
   * Adds an Actor to group
   *
   * @param actor
   */
  public void add(final Actor actor) {
    send(() -> actorPool.add(actor));
  }

  /**
   * Removes an Actor to group
   *
   * @param actor
   */
  public void remove(final Actor actor) {
    send(() -> actorPool.remove(actor));
  }

  /**
   * Kills an Actor and removes it from group
   *
   * @param actor
   */
  public void kill(final Actor actor) {
    send(() -> {
      actor.kill();
      actorPool.remove(actor);
    });
  }

  /**
   * Kills an Actor with killAndWait and removes it from group
   *
   * @param actor
   */
  public void killAndWait(final Actor actor) {
    send(() -> {
      actor.kill();
      actorPool.remove(actor);
    });
  }

  /**
   * ActorGroup kills every Actor and kill itself
   */
  @Override
  public void kill() {
    It.from(actorPool).each((ac) -> {
      kill(ac);
    });

    super.kill();
  }

  /**
   * ActorGroup kills every Actor with killAndWait and kill itself
   */
  @Override
  public void killAndWait() {
    It.from(actorPool).each((ac) -> {
      killAndWait(ac);
    });

    super.kill();
  }

  /**
   * Returns actor pool
   *
   * @return
   */
  public List<Actor> getActorPool() {
    return actorPool;
  }
}
