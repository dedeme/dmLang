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
package dmjava.net;

import dmjava.It;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Builds a HashMap&lt;String, Json> and adds values. The HashMap is converted
 * to Json with toJson().
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class JsonMaker {

  HashMap<String, Json> data;

  public JsonMaker() {
    data = new HashMap<>();
  }

  public JsonMaker add(String key, boolean value) {
    data.put(key, new Json(value));
    return this;
  }

  public JsonMaker add(String key, int value) {
    data.put(key, new Json(value));
    return this;
  }

  public JsonMaker add(String key, double value) {
    data.put(key, new Json(value));
    return this;
  }

  public JsonMaker add(String key, String value) {
    data.put(key, new Json(value));
    return this;
  }

  public JsonMaker add(String key, Json value) {
    data.put(key, value);
    return this;
  }

  public JsonMaker add(JsonMaker jmk) {
    HashMap<String, Json> d = jmk.data;
    It.from(d.keySet()).each((k) -> data.put(k, d.get(k)));
    return this;
  }

  static It<Json> it2it(Iterable<String> it) {
    return It.from(it).map((String p) -> new Json(p));
  }

  public JsonMaker add(String key, Iterable<String> value) {
    data.put(key, new Json(it2it(value)));
    return this;
  }

  public JsonMaker addIt(String key, Iterable<It<String>> value) {
    data.put(key, new Json(It.from(value).map(
      (It<String> p) -> new Json(it2it(p)))));
    return this;
  }

  public JsonMaker addList(String key, Iterable<ArrayList<String>> value) {
    data.put(key, new Json(It.from(value).map(
      (ArrayList<String> p) -> new Json(it2it(p)))));
    return this;
  }

  public HashMap<String, Json> getMap() {
    return data;
  }

  public Json toJson() {
    return new Json(data);
  }

}
