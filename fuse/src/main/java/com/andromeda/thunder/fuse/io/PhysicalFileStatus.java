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

package com.andromeda.thunder.fuse.io;

public class PhysicalFileStatus {

  private final String path;
  private final String owner;
  private final String group;
  private final long size;
  private final boolean isDir;

  private PhysicalFileStatus(final String path, final String owner, final String group,
                             final long size, final boolean isDir) {
    this.path = path;
    this.owner = owner;
    this.group = group;
    this.size = size;
    this.isDir = isDir;
  }

  public String getPath() {
    return path;
  }

  public String getOwner() {
    return owner;
  }

  public String getGroup() {
    return group;
  }

  public long getSize() {
    return size;
  }

  public boolean isDir() {
    return isDir;
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public static class Builder {

    private String path;
    private String owner;
    private String group;
    private long size;
    private boolean isDir;

    public Builder setPath(final String path) {
      this.path = path;
      return this;
    }

    public Builder setOwner(final String owner) {
      this.owner = owner;
      return this;
    }

    public  Builder setGroup(final String group) {
      this.group = group;
      return this;
    }

    public Builder setSize(final long size) {
      this.size = size;
      return this;
    }

    public Builder setIsDir(final boolean isDir) {
      this.isDir = isDir;
      return this;
    }


    public PhysicalFileStatus build() {
      return new PhysicalFileStatus(path, owner, group, size, isDir);
    }

  }
}
