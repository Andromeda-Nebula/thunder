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

package com.andromeda.thunder.fuse.fs;

import com.andromeda.thunder.fuse.conf.Properties;

import java.lang.reflect.Method;
import java.net.URI;

public class FileSystemFactory {

  public static PhysicalFileSystem getFileSystem(URI uri, Properties properties) throws Exception {
    final String scheme = uri.getScheme();
    // User service loader interface
    final Class<?> implClazz = Class.forName(properties.getProperty(scheme));
    final Method getInstance = implClazz.getMethod("getInstance", URI.class, Properties.class);
    return (PhysicalFileSystem)getInstance.invoke(null, uri, properties);
  }
}
