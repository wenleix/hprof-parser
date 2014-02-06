/*
 * Copyright 2014 Edward Aftandilian. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2014 Edward Aftandilian. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.tufts.eaftan.hprofparser;

import edu.tufts.eaftan.hprofparser.handler.RecordHandler;
import edu.tufts.eaftan.hprofparser.handler.PrintHandler;
import edu.tufts.eaftan.hprofparser.parser.HprofParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Parse {
  
  private static final Class<? extends RecordHandler> DEFAULT_HANDLER = PrintHandler.class; 

  public static void main(String[] args) {

    if (args.length < 1) {
      System.out.println("Usage: java Parse [--handler=<handler class>] inputfile");
      System.exit(1);
    }
    
    Class<? extends RecordHandler> handlerClass = DEFAULT_HANDLER;
    if (args[0].startsWith("--handler=")) {
      String handlerClassName = args[0].substring("--handler=".length());
      try {
        handlerClass = (Class<? extends RecordHandler>) Class.forName(handlerClassName);
      } catch (ClassNotFoundException e) {
        System.err.println("Could not find class " + handlerClassName);
        System.exit(1);
      }
    }

    RecordHandler handler = null;
    try {
      handler = handlerClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      System.err.println("Could not instantiate " + handlerClass);
      System.exit(1);
    }
    HprofParser parser = new HprofParser(handler);

    try {
      FileInputStream fs = new FileInputStream(args[0]);
      DataInputStream in = new DataInputStream(new BufferedInputStream(fs));

      parser.parse(in);

      in.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }

  }

}
