package com.dehidehidehi.twitchtagcarousel.dao.impl;
import com.dehidehidehi.twitchtagcarousel.annotation.impl.ApplicationPropertyProducer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

abstract class PropertiesDaoUtil {

    protected void updatePropertiesFileOutsideOfJar(final Properties properties, final String propertiesFile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesFile)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Convenience method for getting the absolute path of where the .jar file will be deployed.
     */
    protected File getDirPathOfThisJar() {
        final String jarLocation;
        try {
            jarLocation = new File(ApplicationPropertyProducer.class
                                           .getProtectionDomain()
                                           .getCodeSource()
                                           .getLocation()
                                           .toURI()
                                           .getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new File(jarLocation);
    }
}
