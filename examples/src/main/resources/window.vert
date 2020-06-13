#version 450

out gl_PerVertex {
    vec4 gl_Position;
};

layout(set = 0, binding = 0) uniform Locals {
   mat4 u_Transform;
};

layout(location=0) in vec2 positions;
layout(location=1) in vec3 vColors;

layout(location=0) out vec3 fColors;

void main() {
    fColors = vColors;
    gl_Position = u_Transform * vec4(positions, 0.0, 1.0);
}
