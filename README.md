# Wgpu-Java
![CI](https://github.com/DevOrc/wgpu-java/workflows/CI/badge.svg)

Java bindings for [Wgpu](https://github.com/gfx-rs/wgpu) based on
[Wgpu-native](https://github.com/gfx-rs/wgpu-native). 

##### Requirements:
- Rust 
- Java JDK 11+

![Boid Example](docs/src/images/boid_simulation.png)
![Cube Example](docs/src/images/cube.png)
![Earth Example](docs/src/images/earth.png)

### Structure :
###### wgpu-java/core
Libraries that make wgpu easier to use in Java.
Includes a windowing library, rendering pipelines, swap chains, etc. Uses 
LWJGL to get Glfw bindings 

###### wgpu-java/native: 
Actual bindings with wgpu-native. Bindings are created at 
runtime using [jnr-ffi](https://github.com/jnr/jnr-ffi)

###### wgpu-java/native/wgpu-native
The [fork](https://github.com/DevOrc/wgpu-native/tree/jnr-compatible) of wgpu-native that this library uses.
A fork is needed to add some functions to get around some problems with jnr-ffi 
(the library that produces the bindings from java to rust)

###### wgpu-java/native/wgpu-test
A cargo crate that has functions used for unit testing the FFI code.

###### wgpu-java/native/jnr-gen
A tool to make java files out of the wgpu.h header file.
[Click Here for more info](https://github.com/DevOrc/wgpu-java/blob/master/native/jnrgen/README.md)

###### wgpu-java/examples
Examples of how to use wgpu-java/core

### How to run:
Gradle will automatically build the required dll/.so/.dylib when wgpu-java/natives 
is compiled. To run the default example, execute the following command: 
```bash
git clone https://github.com/DevOrc/wgpu-java
gradlew runExample
```

To run a specific example:
```bash
gradlew runExample -Pexample={EXAMPLE_NAME_HERE}
gradlew runExample -Pexample=TriangleExample
gradlew runExample -Pexample=ComputeExample
```

 Example Name | Command |
 -------------|---------|
 Triangle | gradlew runExample -Pexample=TriangleExample |
 Compute | gradlew runExample -Pexample=ComputeExample |
 Cube | gradlew runExample -Pexample=CubeExample |
 Vertex | gradlew runExample -Pexample=VertexExample |
 Hex World | gradlew runExample -Pexample=HexWorldExample |
 Window Events | gradlew runExample -Pexample=WindowEventExample |
 Boid Simulation | gradlew runExample -Pexample=BoidExample |

### Supported Platforms
__Windows 7/10__: Vulkan, DX12, DX11

__MacOS__: Metal (See [Issue #4](https://github.com/DevOrc/wgpu-java/issues/4))

__Linux__:  Vulkan 
 
### Contributing
I only have a windows/linux machine, so the best way to contribute is to write
and test the platform specific code (i.e. window handling for rendering pipelines). For more details, see 
[issue #4](https://github.com/DevOrc/wgpu-java/issues/4)