This is the code for the Java Instrumentation Agent Logger.

It contains an ant "build.xml" which per default compiles and build the 
dist/loggingagent.jar and runs HelloWorld.class both with and without
the agent active.

For Eclipse users, this is also a Eclipse 3.3 project.  There is two 
launchers included, and a jardesc file for recreating the loggingagent.jar
file.  (Note:  The absolute path in here should be adjusted to your 
workspace location).


Important: The included javassist.jar library is dual licensed under MPL and LGPL.

The author can be contacted at thorbjoern@gmail.com