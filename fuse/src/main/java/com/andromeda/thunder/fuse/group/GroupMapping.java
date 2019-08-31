/*
 * Copyright 2019 Andromeda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andromeda.thunder.fuse.group;

import java.util.HashMap;
import java.util.Map;

public class GroupMapping {

  private final Map<Integer, GroupInfo> cache = new HashMap<>();
  private final Map<String, GroupInfo> reverseCache = new HashMap<>();

  private GroupMapping() {
    /*
     * Private constructor.
     * Use static factory method to create an instance.
     *
     */
  }

  public static GroupMapping create() {
    return new GroupMapping();
  }

  public GroupInfo getGroupInfo(String gname) {
    if (!reverseCache.containsKey(gname)) {
      // TODO: implement!
      GroupInfo groupInfo = GroupInfo.create(-1, gname);
    }
    return reverseCache.get(gname);
  }
}
