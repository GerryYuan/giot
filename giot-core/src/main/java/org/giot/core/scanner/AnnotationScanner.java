/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.giot.core.scanner;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import java.util.List;
import org.giot.core.container.Service;

/**
 * @author yuanguohua on 2021/3/5 18:48
 */
public abstract class AnnotationScanner implements Service {

    protected String packageName = "org.giot";

    private List<AnnotationScannerListener> listeners;

    public AnnotationScanner(final List<AnnotationScannerListener> listeners) {
        this.listeners = listeners;
    }

    public void scanner() throws Exception {
        ClassPath classpath = ClassPath.from(this.getClass().getClassLoader());
        ImmutableSet<ClassPath.ClassInfo> classes = classpath.getTopLevelClassesRecursive(packageName);
        for (ClassPath.ClassInfo classInfo : classes) {
            Class<?> aClass = classInfo.load();
            for (AnnotationScannerListener listener : listeners) {
                if (aClass.isAnnotationPresent(listener.match())) {
                    listener.addClass(aClass);
                }
            }

        }
        for (AnnotationScannerListener listener : listeners) {
            listener.listener();
        }
    }

}
