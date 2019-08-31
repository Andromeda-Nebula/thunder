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

package com.andromeda.thunder.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import com.andromeda.thunder.fuse.io.FuseFileStatus;
import com.andromeda.thunder.fuse.fs.FuseFileSystem;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class Fuse extends FuseStubFS {

  private final FuseFileSystem fs;

  public Fuse(final String endPoint) throws Exception {
    this.fs = new FuseFileSystem(endPoint);
  }

  @Override
  public int getattr(final String path, final FileStat stat) {
    try {
      final FuseFileStatus fileStatus = fs.getFileStatus(path);
      stat.st_dev.set(fs.getDeviceId());
      // Uid of the owner
      stat.st_uid.set(fileStatus.getOwner().getUid());
      // Gid of the owner
      // TODO: fix group id.
      stat.st_gid.set(fileStatus.getOwner().getUid());
      // Size of the file
      stat.st_size.set(fileStatus.getSize());

      // Time of last access
      // TODO: stat.st_atim

      // Time of last modification
      // TODO: st_mtim;

      // TODO: fix permissions!

      if (fileStatus.isDirectory()) {
        stat.st_mode.set(FileStat.S_IFDIR | 0777);
      } else {
        stat.st_mode.set(FileStat.S_IFREG | 0777);
      }

      return 0;
    } catch (FileNotFoundException ex) {
      return -ErrorCodes.ENOENT();
    } catch (Exception ex) {
      return -1;
    }
  }

  @Override
  public int readdir(final String path, final Pointer buf, final FuseFillDir filter,
                     final @off_t long offset, FuseFileInfo fi) {
    try {
      final FuseFileStatus fileStatus = fs.getFileStatus(path);
      if (!fileStatus.isDirectory()) {
        return -ErrorCodes.ENOTDIR();
      }
      filter.apply(buf, ".", null, 0);
      filter.apply(buf, "..", null, 0);
      for (FuseFileStatus file : fs.listStatus(path)) {
        filter.apply(buf, file.getName(), null, 0);
      }
      return 0;
    } catch (FileNotFoundException ex) {
      return -ErrorCodes.ENOENT();
    } catch (Exception ex) {
      return -1;
    }
  }

  @Override
  public int mkdir(final String path, final @mode_t long mode) {
    try {
      fs.mkdir(path);
      return 0;
    } catch (FileAlreadyExistsException ex) {
      return -ErrorCodes.EEXIST();
    } catch (IOException ex) {
      return -1;
    }
  }


  @Override
  public int read(final String path, final Pointer buf,
                  final @size_t long size, final @off_t long offset,
                  final FuseFileInfo fi) {
    try {
      FuseFileStatus fileStatus = fs.getFileStatus(path);
      if (fileStatus.isDirectory()) {
        return -ErrorCodes.EISDIR();
      }

      long toRead = size;
      long readOffset = offset;
      int bytesRead = 0;

      while (toRead > 0) {
        final int bufferSize = size > Integer.MAX_VALUE ?
            Integer.MAX_VALUE : Long.valueOf(toRead).intValue();
        final byte[] dataBuf = new byte[bufferSize];
        final int sizeRead = fs.read(path, dataBuf, readOffset);
        buf.put(bytesRead, dataBuf, 0, sizeRead);
        toRead = toRead - sizeRead;
        readOffset = offset + sizeRead;
        bytesRead = bytesRead + sizeRead;
        if (sizeRead < bufferSize) {
          break;
        }
      }
      return bytesRead;
    } catch (FileNotFoundException ex) {
      return -ErrorCodes.ENOENT();
    } catch (IOException ex) {
      // TODO: Log the exception properly!
      ex.printStackTrace();
      return -1;
    }
  }

}
