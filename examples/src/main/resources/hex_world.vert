#version 450

out gl_PerVertex {
    vec4 gl_Position;
};
layout(location=0) in vec2 positions;
layout(set = 0, binding = 0) uniform Locals {
   mat4 u_Transform;
};

layout(set = 0, binding = 1) buffer InstanceModels {
   mat4 iModels[];
};

layout(set = 0, binding = 2) buffer InstanceColors {
   vec4 iColors[];
};

layout(location=0) out vec4 fColors;

void main() {
    fColors = iColors[gl_InstanceIndex];
    gl_Position = u_Transform * iModels[gl_InstanceIndex] * vec4(positions, 1.0, 1.0);
}
