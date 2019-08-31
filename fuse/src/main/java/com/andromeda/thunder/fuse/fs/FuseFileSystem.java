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
import com.andromeda.thunder.fuse.io.FuseFileStatus;
import com.andromeda.thunder.fuse.group.GroupMapping;
import com.andromeda.thunder.fuse.inode.INodeMapping;
import com.andromeda.thunder.fuse.io.PhysicalFileStatus;
import com.andromeda.thunder.fuse.user.UserMapping;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FuseFileSystem {

  private final PhysicalFileSystem fs;
  private final INodeMapping iNodeMapping;
  private final UserMapping userMapping;
  private final GroupMapping groupMapping;

  public FuseFileSystem(final String endPoint) throws Exception {
    this.iNodeMapping = INodeMapping.create();
    this.userMapping = UserMapping.create();
    this.groupMapping = GroupMapping.create();
    this.fs = FileSystemFactory.getFileSystem(new URI(endPoint), Properties.getInstance());
  }

  public long getDeviceId() {
    return fs.getDeviceId();
  }

  public FuseFileStatus getFileStatus(String path) throws IOException {
    final PhysicalFileStatus pfs = fs.getFileStatus(path);
    return FuseFileStatus.getBuilder()
        .setPath(pfs.getPath())
        .setOwner(userMapping.getUserInfo(pfs.getOwner()))
        .setGroup(groupMapping.getGroupInfo(pfs.getGroup()))
        .setSize(pfs.getSize())
        .setIsDir(pfs.isDir())
        .build();
  }

  public List<FuseFileStatus> listStatus(final String path) throws IOException {
    final List<FuseFileStatus> fileList = new ArrayList<>();
    fs.listStatus(path).forEach(file -> fileList.add(FuseFileStatus.getBuilder()
        .setPath(file.getPath())
        .setOwner(userMapping.getUserInfo(file.getOwner()))
        .setGroup(groupMapping.getGroupInfo(file.getGroup()))
        .setSize(file.getSize())
        .setIsDir(file.isDir())
        .build()));
    return fileList;
  }

  public void mkdir(final String path) throws IOException {
    fs.mkdir(path);
  }

  public int read(final String path, final byte[] buf, final long offset) throws IOException {
    return fs.read(path, buf, offset);
  }

}
