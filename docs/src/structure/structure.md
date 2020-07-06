# Structure :

## Modules

__wgpu-java/core__:
Libraries that make wgpu easier to use in Java.
Includes a windowing library, rendering pipelines, swap chains, etc. Uses 
LWJGL to get Glfw bindings 

__wgpu-java/native__: 
Actual bindings with wgpu-native. Bindings are created at 
runtime using [jnr-ffi](https://github.com/jnr/jnr-ffi)

__wgpu-java/native/wgpu-native__:
The [fork](https://github.com/DevOrc/wgpu-native/tree/jnr-compatible) of wgpu-native that this library uses.
A fork is needed to add some functions to get around some problems with jnr-ffi 
(the library that produces the bindings from java to rust)

__wgpu-java/native/wgpu-test__:
A cargo crate that has functions used for unit testing the FFI code.

__wgpu-java/native/jnr-gen__:
A tool to make java files out of the wgpu.h header file.

__wgpu-java/examples__:
Examples of how to use wgpu-java/core

## Class Naming
- if a class ends with `Descriptor`, it is a direct binding to the Wgpu type. 
- if a class ends with `Config` that is a class from Wgpuj/core (and is usually a wrapper around a descriptor)

## Notable Classes
- __[Wgpu:](../javadoc/com/noahcharlton/wgpuj/jni/Wgpu.html)__ Constants from the Wgpu Library 
- __[WgpuJavaStruct:](../javadoc/com/noahcharlton/wgpuj/util/WgpuJavaStruct.html)__ The base class for all structs in wgpuj