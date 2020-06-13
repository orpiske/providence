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


import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Wraps the configuration object
 */
public class ConfigurationWrapper {
	private static final CompositeConfiguration config = new CompositeConfiguration();
	
	/**
	 * Restricted constructor
	 */
	private ConfigurationWrapper() {}

	/**
	 * Initializes the configuration object
	 * 
	 * @param configDir
	 * 			  The configuration directory containing the configuration file
	 * @param fileName
	 *            The name of the configuration file
	 * @throws FileNotFoundException If the file does not exist
	 * @throws ConfigurationException if the configuration file contains errors
	 */
	public static void initConfiguration(final String configDir,
			final String fileName) throws IOException,
			ConfigurationException {
		if (configDir == null) {
			throw new FileNotFoundException(
					"The configuration dir was not found");
		}
		
    	// Appends an user config file, if exits ($HOME/.providence/<configuration.properties>)
		String userFilePath = RuntimeUtils.getAppDirectoryPath() + File.separator
				+ fileName;
		File userFile = new File(userFilePath);
		
		if (userFile.exists()) { 
			PropertiesConfiguration userConfiguration = 
					new PropertiesConfiguration(userFile);
			

			config.addConfiguration(userConfiguration);
		}
		else {
			if (!userFile.getParentFile().mkdirs()) {
				throw new IOException("Unable to create user directories");
			}
		}


        PropertiesConfiguration systemConfig = new PropertiesConfiguration(configDir + File.separator
                + fileName);
        config.addConfiguration(systemConfig);

    }

	/**
	 * Gets the configuration object
	 * 
	 * @return the instance of the configuration object
	 */
	public static AbstractConfiguration getConfig() {
		return config;
	}

}
