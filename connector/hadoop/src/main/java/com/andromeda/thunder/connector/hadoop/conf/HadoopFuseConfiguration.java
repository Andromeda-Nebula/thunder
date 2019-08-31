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

import org.apache.hadoop.fs.ozone.OzFs;
import org.apache.hadoop.fs.ozone.OzoneFileSystem;
import com.andromeda.thunder.fuse.conf.Properties;
import org.apache.hadoop.hdds.conf.OzoneConfiguration;

public final class HadoopFuseConfiguration {

  private static OzoneConfiguration configuration;

  private HadoopFuseConfiguration() {}

  private static void initializeConfig(Properties properties) {
    configuration = new OzoneConfiguration();
    // Set HadoopFileSystemConnector related configurations
    configuration.set(HadoopConfigKey.O3FS_IMPL, OzoneFileSystem.class.getName());
    configuration.set(HadoopConfigKey.O3FS_AFS_IMPL, OzFs.class.getName());
  }


  public static synchronized OzoneConfiguration getOzConfig(Properties properties) {
    if (configuration == null) {
      initializeConfig(properties);
    }
    return configuration;
  }

}
