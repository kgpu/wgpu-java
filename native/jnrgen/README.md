# Jnr-gen
Jnr-gen is a program to generate java files from a c header file. It is designed to be used with 
the jnr library (hence the name).

To build the natives run the following gradle command:
```
gradlew native:jnrgen:generateBindings
```

The bindings will be located in the build/jnr-gen folder. 