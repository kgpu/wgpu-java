# Structure :

### wgpu-java/core
Libraries that make wgpu easier to use in Java.
Includes a windowing library, rendering pipelines, swap chains, etc. Uses 
LWJGL to get Glfw bindings 

### wgpu-java/native: 
Actual bindings with wgpu-native. Bindings are created at 
runtime using [jnr-ffi](https://github.com/jnr/jnr-ffi)

### wgpu-java/native/wgpu-native
The [fork](https://github.com/DevOrc/wgpu-native/tree/jnr-compatible) of wgpu-native that this library uses.
A fork is needed to add some functions to get around some problems with jnr-ffi 
(the library that produces the bindings from java to rust)

### wgpu-java/native/wgpu-test
A cargo crate that has functions used for unit testing the FFI code.

### wgpu-java/native/jnr-gen
A tool to make java files out of the wgpu.h header file.
[Click Here for more info](https://github.com/DevOrc/wgpu-java/blob/master/native/jnrgen/README.md)

### wgpu-java/examples
Examples of how to use wgpu-java/core
