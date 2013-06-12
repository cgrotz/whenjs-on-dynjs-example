package de.skiptag.whenjs.js;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.dynjs.Config;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.modules.ModuleProvider;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class JsRunner {
	private static ThreadLocal<JsRunner> instances = new ThreadLocal<>();
	// Parallel running Threads(Executor) on System
	public static int corePoolSize = 2;

	// Maximum Threads allowed in Pool
	public static int maxPoolSize = 4;

	// Keep alive time for waiting threads for jobs(Runnable)
	public static long keepAliveTime = 10;

	public static ThreadPoolExecutor threadPool;
	static {
		// Working queue for jobs (Runnable). We add them finally here
		ArrayBlockingQueue workQueue = new ArrayBlockingQueue(5);

		threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
	}

	private String scriptName;
	private DynJS runtime;
	private ResourceLoader resourceLoader;

	public JsRunner(String scriptName) {
		this.scriptName = scriptName;
		this.resourceLoader = new ResourceLoader();
		instances.set(this);
	}

	public void start() throws Exception {
		Config config = new Config();
		config.setGlobalObjectFactory(getGlobalObjectFactory());
		runtime = new DynJS(config);

		String script = CharStreams.toString(new InputStreamReader(resourceLoader.loadResource(scriptName),
				Charsets.UTF_8));
		runtime.evaluate(script);
	}

	private GlobalObjectFactory getGlobalObjectFactory() {
		return new GlobalObjectFactory() {

			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				GlobalObject globalObject = new GlobalObject(runtime);

				globalObject.getModuleProviders().add(new ModuleProvider() {

					@Override
					public Object load(ExecutionContext context, String moduleName) {
						String script;
						try {
							script = CharStreams.toString(new InputStreamReader(
									resourceLoader.loadResource(moduleName), Charsets.UTF_8));
							return script;
						} catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
				globalObject.defineGlobalProperty("console", new Console());
				globalObject.defineGlobalProperty("setTimeout", new AbstractNativeFunction(globalObject) {
					@Override
					public Object call(ExecutionContext context, Object self, Object... args) {
						Long timeout = (Long) args[1];
						JSFunction func = (JSFunction) args[0];
						try {
							Thread.sleep(timeout);
						} catch (InterruptedException e) {
						}
						return func.call(context);
					}
				});
				return globalObject;
			}
		};
	}
}