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

package com.andromeda.thunder.fuse.user;

import com.andromeda.thunder.fuse.util.Shell;

import java.util.HashMap;
import java.util.Map;

public class UserMapping {

  private final Map<Integer, UserInfo> cache = new HashMap<>();
  private final Map<String, UserInfo> reverseCache = new HashMap<>();

  private UserMapping() {
    /*
     * Private constructor.
     * Use static factory method to create an instance.
     *
     */
  }

  public static UserMapping create() {
    return new UserMapping();
  }

  public UserInfo getUserInfo(String uname) {
    if (!reverseCache.containsKey(uname)) {
      final Shell.Result result = Shell.run("id -u " + uname);
      final int uid = result.getExitCode() == 0 ?
          Integer.parseInt(result.getOutput()) : uname.hashCode();
      final UserInfo userInfo = UserInfo.create(uid, uname);
      cache.put(uid, userInfo);
      reverseCache.put(uname, userInfo);
    }
    return reverseCache.get(uname);
  }
}
