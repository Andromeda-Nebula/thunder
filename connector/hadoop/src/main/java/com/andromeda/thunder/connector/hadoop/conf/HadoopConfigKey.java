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

package com.andromeda.thunder.connector.hadoop.conf;

public class HadoopConfigKey {

  public static final String O3FS_SCHEME = "o3fs";
  public static final String O3FS_IMPL = "fs.o3fs.impl";
  public static final String O3FS_AFS_IMPL = "fs.AbstractFileSystem.o3fs.impl";


  private HadoopConfigKey() {
    // Private constructor.
  }

}
