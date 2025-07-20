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
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;


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
	 * @throws ConfigurationException if the configuration file contains errors
	 */
	public static void initConfiguration(final String configDir, final String fileName) throws IOException,
			ConfigurationException {
		Objects.requireNonNull(configDir, "The configuration dir was not provided");

    	// Appends an user config file, if exits ($HOME/.providence/<configuration.properties>)
		String userFilePath = RuntimeUtils.getAppDirectoryPath() + File.separator
				+ fileName;
		File userFile = new File(userFilePath);
		
		if (userFile.exists()) {
			PropertiesBuilderParameters propertiesBuilderParameters = new Parameters().properties().setFileName(userFilePath)
					.setThrowExceptionOnMissing(true)
					.setListDelimiterHandler(new DefaultListDelimiterHandler(','))
					.setIncludesAllowed(false);
			FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
					new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
							.configure(propertiesBuilderParameters);

			PropertiesConfiguration userConfiguration = builder.getConfiguration();

			config.addConfiguration(userConfiguration);
		}
		else {
			File configDirFile = userFile.getParentFile();
			if (!configDirFile.exists()) {
				if (!configDirFile.mkdirs()) {
					throw new IOException("Unable to create user directories: " + userFile.getParentFile());
				}
			}
		}


		PropertiesBuilderParameters propertiesBuilderParameters = new Parameters().properties().setFileName(configDir + File.separator
						+ fileName)
				.setThrowExceptionOnMissing(true)
				.setListDelimiterHandler(new DefaultListDelimiterHandler(','))
				.setIncludesAllowed(false);
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
				new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
						.configure(propertiesBuilderParameters);

        PropertiesConfiguration systemConfig = builder.getConfiguration();
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
