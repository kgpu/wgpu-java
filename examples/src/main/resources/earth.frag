#version 450

layout(location=0) in vec2 tex_coords;
layout(location=1) in vec3 normal;
layout(location=2) in vec3 position;
layout(location=0) out vec4 f_color;

layout(set = 0, binding = 1) uniform texture2D u_texture;
layout(set = 0, binding = 2) uniform sampler u_sampler;
layout(set = 0, binding = 3) uniform Locals {
   vec3 u_light_src;
};

void main() {
    vec4 ambient = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 light_color = vec4(1.0, 1.0, 1.0, 1.0);
    vec3 light = normalize(u_light_src - position);
    vec4 directional = max(dot(normal, light), 0.0) * light_color;

    f_color = (ambient + directional) * texture(sampler2D(u_texture, u_sampler), tex_coords);
}