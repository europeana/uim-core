/* OsgiResourceConfigurer.java - created on May 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since May 18, 2011
 */
public class OsgiResourceConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger log                    = Logger.getLogger(OsgiResourceConfigurer.class.getName());

    private String              osgiConfigurationName  = "eu.europeana.uim";
    private String              fallbackPropertiesName = "";

    /**
     * Creates a new instance of this class.
     */
    public OsgiResourceConfigurer() {
        setIgnoreResourceNotFound(true);
        setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException {
        props.putAll(getProperties());
        super.processProperties(beanFactory, props);
    }

    /**
     * @return the configuraiton name to lookup
     */
    public String getOsgiConfigurationName() {
        return osgiConfigurationName;
    }

    /**
     * @param osgiConfigurationName
     */
    public void setOsgiConfigurationName(String osgiConfigurationName) {
        this.osgiConfigurationName = osgiConfigurationName;
    }

    /**
     * @return the fallback properties name
     */
    public String getFallbackPropertiesName() {
        return fallbackPropertiesName;
    }

    /**
     * @param fallbackPropertiesName
     */
    public void setFallbackPropertiesName(String fallbackPropertiesName) {
        this.fallbackPropertiesName = fallbackPropertiesName;
    }

    @SuppressWarnings("unchecked")
    private Properties getProperties() {
        Properties properties = new Properties();

        Bundle bundle = FrameworkUtil.getBundle(OsgiResourceConfigurer.class);
        if (bundle != null) {
            ServiceReference caRef = bundle.getBundleContext().getServiceReference(
                    ConfigurationAdmin.class.getName());
            if (caRef != null) {
                ConfigurationAdmin configAdmin = (ConfigurationAdmin)bundle.getBundleContext().getService(
                        caRef);
                try {
//                    Configuration[] configurations = configAdmin.listConfigurations(null);
//                    if (configurations == null || configurations.length == 0) {
//                        Configuration config = configAdmin.getConfiguration(osgiConfigurationName);
//
//                        Properties defaultproperties = new Properties();
//                        InputStream stream = getClass().getResourceAsStream(fallbackPropertiesName);
//                        if (stream != null) {
//                            defaultproperties.load(stream);
//                            Hashtable<String, String> hashtable = new Hashtable<String, String>();
//                            for (Entry<Object, Object> entry : defaultproperties.entrySet()) {
//                                hashtable.put((String)entry.getKey(), (String)entry.getValue());
//                            }
//                            config.update(hashtable);
//                        }
//                    }

                    Configuration config = configAdmin.getConfiguration(osgiConfigurationName);

                    Dictionary<String, String> dictionary = config.getProperties();
                    if (dictionary != null) {
                        Enumeration<String> keys = dictionary.keys();
                        while (keys.hasMoreElements()) {
                            String key = keys.nextElement();
                            String value = dictionary.get(key);
                            if (key != null && value != null) {
                                properties.setProperty(key, value);
                            }
                        }
                    }
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to store config change with config service.", e);
                } 
//                catch (InvalidSyntaxException e) {
//                    throw new RuntimeException("Caused by InvalidSyntaxException", e);
//                }
            }
        } 
//        else if (fallbackPropertiesName != null && fallbackPropertiesName.length() > 0) {
//            try {
//                InputStream stream = getClass().getResourceAsStream(fallbackPropertiesName);
//                if (stream != null) {
//                    properties.load(stream);
//                } else {
//                    log.log(Level.INFO, "Failed to load properties filw in classpath <" +
//                                        fallbackPropertiesName + ">");
//                }
//            } catch (IOException e) {
//                log.log(Level.INFO, "Failed to load properties fiel in classpath <" +
//                                    fallbackPropertiesName + ">", e);
//            }
//        }
        return properties;
    }
}
