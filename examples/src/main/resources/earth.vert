#version 450

out gl_PerVertex {
    vec4 gl_Position;
};
layout(location=0) in vec3 v_position;
layout(location=1) in vec2 v_tex_coords;
layout(location=2) in vec3 v_normal;
layout(location=0) out vec2 f_tex_coords;
layout(location=1) out vec3 f_normal;
layout(location=2) out vec3 f_position;

layout(set = 0, binding = 0) uniform TransformationMatrix {
    mat4 u_Transform;
};

layout(set = 0, binding = 4) uniform ModelMatrix {
    mat4 u_Model;
};

layout(set = 0, binding = 5) uniform NormalMatrix {
    mat4 u_NormalMatrix;
};

void main() {
    f_tex_coords = v_tex_coords;
    f_normal = vec3(vec4(v_normal, 1.0) * u_NormalMatrix);
    f_position = vec3(vec4(v_position, 1.0) * u_Model);
    gl_Position = u_Transform * vec4(f_position, 1.0);
}
