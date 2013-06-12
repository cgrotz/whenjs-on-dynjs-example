package de.skiptag.whenjs.js;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ResourceLoader {
	public URL getURL(String fileName) throws MalformedURLException {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		URL resource = contextClassLoader.getResource(fileName);
		if (resource == null) {
			resource = contextClassLoader.getResource("META-INF/" + fileName);
		}

		return checkNotNull(resource, "File not found " + fileName);
	}

	public InputStream loadResource(String fileName) throws IOException {
		URL resource = getURL(fileName);
		return resource.openStream();
	}
}
