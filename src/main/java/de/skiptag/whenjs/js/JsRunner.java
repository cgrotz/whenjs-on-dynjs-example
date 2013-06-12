package de.skiptag.whenjs.js;

import java.io.InputStreamReader;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class JsRunner {
	private static ThreadLocal<JsRunner> instances = new ThreadLocal<>();
	private String scriptName;
	private DynJS runtime;

	public JsRunner(String scriptName) {
		this.scriptName = scriptName;
		instances.set(this);
	}

	public void start() throws Exception {
		Config config = new Config();
		config.setGlobalObjectFactory(getGlobalObjectFactory());
		runtime = new DynJS(config);

		String script = CharStreams.toString(new InputStreamReader(JsRunner.class.getClassLoader().getResourceAsStream(
				scriptName), Charsets.UTF_8));
		runtime.evaluate(script);
	}

	private GlobalObjectFactory getGlobalObjectFactory() {
		return new GlobalObjectFactory() {

			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				GlobalObject globalObject = new GlobalObject(runtime);
				globalObject.defineGlobalProperty("console", new Console());
				globalObject.defineGlobalProperty("setTimeout", new SetTimeout(globalObject));
				return globalObject;
			}
		};
	}
}