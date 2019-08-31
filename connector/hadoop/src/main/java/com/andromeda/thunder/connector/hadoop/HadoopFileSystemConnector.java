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

package com.andromeda.thunder.connector.hadoop;

import com.andromeda.thunder.connector.hadoop.conf.HadoopFuseConfiguration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import com.andromeda.thunder.fuse.conf.Properties;
import com.andromeda.thunder.fuse.io.PhysicalFileStatus;
import com.andromeda.thunder.fuse.fs.PhysicalFileSystem;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HadoopFileSystemConnector implements PhysicalFileSystem {

  private static final Pattern URL_SCHEMA_PATTERN =
      Pattern.compile("([^\\.]+)\\.([^\\.]+)\\.?(.*)");
  private static final Map<URI, HadoopFileSystemConnector> INSTANCES = new HashMap<>();

  private final String volume;
  private final String bucket;

  private final FileContext fileContext;
  private final long deviceId;

  /**
   *
   * @param ozoneUri o3fs://<bucket>.<volume>.<om-host>:<om-port>
   * @throws Exception in case of error
   */
  private HadoopFileSystemConnector(URI ozoneUri, Properties properties) throws Exception {

    final Matcher matcher = URL_SCHEMA_PATTERN.matcher(ozoneUri.getAuthority());
    if (!matcher.matches() || matcher.groupCount() != 3) {
      throw new IllegalArgumentException("Cannot parse URI: " + ozoneUri);
    }

    this.volume = matcher.group(2);
    this.bucket = matcher.group(1);
    this.fileContext = FileContext.getFileContext(ozoneUri,
        HadoopFuseConfiguration.getOzConfig(properties));
    this.deviceId = generateDeviceId();
  }

  public static synchronized PhysicalFileSystem getInstance(URI ozoneUri, Properties properties) {
    return INSTANCES.computeIfAbsent(ozoneUri, key -> {
      try {
        return new HadoopFileSystemConnector(key, properties);
      } catch (Exception e) {
        throw new RuntimeException("Cannot create HadoopFileSystemConnector.", e);
      }
    });
  }

  private int generateDeviceId() {
    return 2^32 * ~volume.hashCode() + bucket.hashCode();
  }

  @Override
  public long getDeviceId() {
    return deviceId;
  }

  @Override
  public PhysicalFileStatus getFileStatus(final String path) throws IOException {
    final FileStatus status = fileContext.getFileStatus(new Path(path));
    final PhysicalFileStatus.Builder builder = PhysicalFileStatus.getBuilder();
    builder.setPath(path)
        .setOwner(status.getOwner())
        .setGroup(status.getGroup())
        .setSize(status.getLen())
        .setIsDir(status.isDirectory());
    return builder.build();
  }

  @Override
  public List<PhysicalFileStatus> listStatus(final String path) throws IOException {
    final List<PhysicalFileStatus> result = new ArrayList<>();
    final RemoteIterator<FileStatus> fileStatusItr = fileContext.listStatus(new Path(path));
    while (fileStatusItr.hasNext()) {
      final FileStatus status = fileStatusItr.next();
      final PhysicalFileStatus.Builder builder = PhysicalFileStatus.getBuilder();
      builder.setPath(status.getPath().toUri().getPath())
          .setOwner(status.getOwner())
          .setGroup(status.getGroup())
          .setSize(status.getLen())
          .setIsDir(status.isDirectory());
      result.add(builder.build());
    }
    return result;
  }

  @Override
  public void mkdir(final String path) throws IOException {
    try {
      final FsPermission fp = new FsPermission(FsAction.ALL,
          FsAction.READ_EXECUTE, FsAction.READ_EXECUTE);
      fileContext.mkdir(new Path(path), fp, true);
    } catch (FileAlreadyExistsException ex) {
      throw new java.nio.file.FileAlreadyExistsException(ex.getMessage());
    }
  }

  @Override
  public int read(final String path, final byte[] buf, final long offset) throws IOException {
    try {
      final FSDataInputStream inputStream = fileContext.open(new Path(path));
      inputStream.seek(offset);
      return inputStream.read(buf);
    } catch (FileAlreadyExistsException ex) {
      throw new java.nio.file.FileAlreadyExistsException(ex.getMessage());
    }
  }

}
