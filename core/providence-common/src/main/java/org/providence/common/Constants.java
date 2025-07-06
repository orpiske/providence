/*
 * Copyright 2018 Otavio R. Piske <angusyoung@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.providence.common;

import java.io.File;

/**
 * Application constants
 */
public final class Constants {

    public static final String VERSION = "0.0.6-SNAPSHOT";

    public static final int VERSION_NUMERIC;

    public static final String BIN_NAME = "providence";

    public static final String HOME_PROPERTY = "org.providence.home";

    public static final String HOME_DIR;

    public static final String PROVIDENCE_CONFIG_DIR;

    public static final String PROVIDENCE_LOG_DIR;

    static {
        HOME_DIR = System.getProperty(HOME_PROPERTY);

        PROVIDENCE_CONFIG_DIR = System.getProperty(HOME_PROPERTY) + File.separator + "config";

        PROVIDENCE_LOG_DIR = System.getProperty(HOME_PROPERTY) + File.separator + "logs";

        VERSION_NUMERIC = Integer.parseInt(VERSION.replace(".", "").replaceAll("[a-zA-Z-]",""));
    }


    /**
     * Restricted constructor
     */
    private Constants() {
    }


}
