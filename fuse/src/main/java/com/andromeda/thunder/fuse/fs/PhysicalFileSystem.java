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

import com.andromeda.thunder.fuse.io.PhysicalFileStatus;

import java.io.IOException;
import java.util.List;

public interface PhysicalFileSystem {

  long getDeviceId();

  PhysicalFileStatus getFileStatus(String path) throws IOException;

  List<PhysicalFileStatus> listStatus(final String path) throws IOException;

  // TODO: Implement permission.
  void mkdir(final String path) throws IOException;

  int read(final String path, final byte[] buf, final long offset) throws IOException;
}
