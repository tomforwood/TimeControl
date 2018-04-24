# TimeControl

Take control over time itself for better testing of java

## Purpose

Real time enters into java in 4 ways
1. Calls to create a current date (e.g System.currentTimeMillis, new Date(), Instant.now() etc)
2. Timed execution calls (e.g. java.util.Timer, ScheduledExecutorService etc.)
3. Timeouts on calls (e.g. java.util.concurrent.CountDownLatch.await(long timeout, TimeUnit unit etc)
4. Thread.sleep()

The Java 8 java.time.Clock helps with 1. This library aims to build upon that by providing a Clock that can be progressed under the control of the test code, classes extending Timer and ScheduledExecutorService etc and wrappers for timed out methods that all respect the artificial time.

Your code will need to use the timeControl wrappers to do all its time related functionality (although there may be aspectJ methods to re-write existing classes) 

## Modules

Time control is split into two modules, the API and the testing library. The API provides wrappers for time functions and is designed to be used in production code. The Testing library is designed to be used in Test code to provide controlable implementations of various classes 


### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

Add the maven dependency

## Building testable code and testing it
*For more examples see junit tests in testing module*
###Code using timestamps
Application code
'''
public LocalDate getDate() {
	return LocalDate.now(TimeFactory.getClock());
}
'''
Testing code
'''
@Test
public void testGetInstant() {
	Instant i=hello.getInstant();
	assertThat(i.getEpochSecond()==0);
	mockClock.advance(10);
	i=hello.getInstant();
	assertThat(i.getEpochSecond()==10);
}
'''
###Code using Sleep
ApplicationCode
'''
public String delayedGreeting() throws InterruptedException {
	TimeFactory.sleep(1000L*60*60*24);
	return "Hello World!";
}
'''
Testing Code
'''
@Test
public void testSleepSimple() throws InterruptedException, ExecutionException {
	assertEquals(0, mockClock.millis());
	mockSleep.setFastTime(true);
	String s=hello.delayedGreeting();
	//normally this call would block for a day
	//but this test is over instantly and the virtual time has advanced by a day
	assertEquals(1000L*60*60*24, mockClock.millis());
}
'''

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Tom Forwood** 

See also the list of [contributors](https://github.com/tomforwood/TimeControl/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0  see the [LICENSE.md](LICENSE.md) file for details


