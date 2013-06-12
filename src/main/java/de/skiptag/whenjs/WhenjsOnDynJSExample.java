package de.skiptag.whenjs;

import de.skiptag.whenjs.js.JsRunner;

public class WhenjsOnDynJSExample {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JsRunner runner = new JsRunner("main.js");
		runner.start();
		while (true) {

		}
	}
}
