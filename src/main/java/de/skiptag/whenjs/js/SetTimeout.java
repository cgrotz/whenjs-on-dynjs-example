package de.skiptag.whenjs.js;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;

class SetTimeout extends AbstractNativeFunction {

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

	SetTimeout(GlobalObject globalObject) {
		super(globalObject, "callback", "timeout");
	}

	@Override
	public Object call(ExecutionContext context, Object self, Object... args) {
		Long timeout = (Long) args[1];
		JSFunction func = (JSFunction) args[0];
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
		return context.call(func, self, new Object[] {});
	}
}