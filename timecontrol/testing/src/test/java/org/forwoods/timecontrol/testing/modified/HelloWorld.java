package org.forwoods.timecontrol.testing.modified;

import org.forwoods.timecontrol.timecontrolapi.TimeFactory;

public class HelloWorld {
	
	public String delayedGreeting() throws InterruptedException {
		TimeFactory.sleep(1);
		return "Hello World!";
	}

}
