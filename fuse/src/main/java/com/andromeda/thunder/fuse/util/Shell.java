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

package com.andromeda.thunder.fuse.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Shell {

  public static class Result {

    private static final int ERROR_CODE = -1;
    private static final String NO_OUTPUT = "";

    private final int exitCode;
    private final String output;

    private Result(final int exitCode, final String output) {
      this.exitCode = exitCode;
      this.output = output;
    }

    private static Result build(final int exitCode, final String output) {
      return new Result(exitCode, output);
    }

    public int getExitCode() {
      return exitCode;
    }

    public String getOutput() {
      return output;
    }
  }

  public static Result run(final String command) {
    try {
      Process p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader buf = new BufferedReader(new InputStreamReader(
          p.getInputStream()));
      String line;
      StringBuilder output = new StringBuilder();

      while ((line = buf.readLine()) != null) {
        if (output.length() != 0) {
          output.append('\n');
        }
        output.append(line);
      }
      return Result.build(p.exitValue(), output.toString().trim());
    } catch (Exception e) {
      // LOG exception!
      e.printStackTrace();
    }
    return Result.build(Result.ERROR_CODE, Result.NO_OUTPUT);

  }
}
