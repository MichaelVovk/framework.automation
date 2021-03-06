package framework.utils.parsers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import framework.utils.log.FrameworkLogger;
import framework.utils.log.LogFactory;

/**
 * Class to read property files
 * @author Taras.Lytvyn
 *
 */
public class PropertyFileReader extends FileReader {
	
	private static final FrameworkLogger LOG = LogFactory.getLogger(PropertyFileReader.class); 

	private Properties props;

	public PropertyFileReader(String fileLocation) {
		super(fileLocation);
		props = new Properties();
		try {
			props.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeStream();
	}

	/**
	 * get property value
	 * @param propertyName	name of the property
	 * @return property value
	 * @throws Exception
	 */
	public String getPropertyValue(String propertyName) throws Exception {
		if (!props.isEmpty() && propertyName != null) {
			LOG.info("Reading property with property name: " + propertyName);
			return this.props.getProperty(propertyName);
		} else
			throw new Exception("Property file is empty or property " + propertyName + " is invalid property to get");
	}

	/**
	 * read properties to map
	 * @return	map of properties
	 * @throws Exception
	 */
	public Map<String, String> readPropertiesToMap() throws Exception {
		Map<String, String> propsMap = new HashMap<String, String>();
		for (String key : props.stringPropertyNames()) {
			propsMap.put(key, getPropertyValue(key));
		}
		return propsMap;
	}
}
