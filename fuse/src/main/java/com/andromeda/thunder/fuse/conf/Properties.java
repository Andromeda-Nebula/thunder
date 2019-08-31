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

package com.andromeda.thunder.fuse.conf;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Properties extends java.util.Properties {

  private static Properties instance;

  private Properties() {
    final String homePropFile = System.getProperty(ConfigKey.USER_HOME) +
        File.separator + ConfigKey.PROP_FILE;
    final String jvmArgPropFile = System.getProperty(ConfigKey.PROP_FILE_OPTION);
    try {
      if (Paths.get(homePropFile).toFile().isFile()) {
        load(new FileInputStream(homePropFile));
      }
      if (jvmArgPropFile != null && Paths.get(jvmArgPropFile).toFile().isFile()) {
        load(new FileInputStream(jvmArgPropFile));
      }
    } catch (IOException e) {
      //TODO: log error;
    }
  }

  public static synchronized Properties getInstance() {
    if (instance == null) {
      instance = new Properties();
    }
    return instance;
  }

}
