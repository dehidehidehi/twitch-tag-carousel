package com.dehidehidehi.twitchtagcarousel.dao.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

abstract class PropertiesDaoUtil {

	/**
	 * Writes the properties object into the specified file.
	 */
	protected void updatePropertiesFile(final Properties properties, final String filePath) {
		final URL resource = getClass().getResource(filePath);
		final URI uri;
		try {
			uri = resource.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		final File file = new File(uri);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			properties.store(fileOutputStream, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
