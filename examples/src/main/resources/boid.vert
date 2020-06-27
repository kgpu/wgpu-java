#version 450

out gl_PerVertex {
    vec4 gl_Position;
};

layout(set = 0, binding = 0) buffer InstanceModels {
   mat4 iModels[];
};

layout(location=0) in vec2 positions;

void main() {
    gl_Position =  iModels[gl_InstanceIndex] * vec4(positions, 0.0, 1.0);
}
