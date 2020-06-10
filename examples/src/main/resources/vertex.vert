#version 450

out gl_PerVertex {
    vec4 gl_Position;
};
layout(location=0) in vec3 positions;

void main() {
    gl_Position = vec4(positions, 1.0);
}
