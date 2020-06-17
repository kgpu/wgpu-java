#version 450

layout(location = 0) out vec4 outColor;
layout(location = 0) in vec4 fColor;

void main() {
    outColor = vec4(fColor);
}
