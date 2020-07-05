package com.noahcharlton.wgpuj.core.util;

import org.lwjgl.util.shaderc.Shaderc;

import java.nio.ByteBuffer;

public class ShaderCompiler {

    public static final int VERTEX = Shaderc.shaderc_vertex_shader;
    public static final int FRAGMENT = Shaderc.shaderc_fragment_shader;
    public static final int COMPUTE = Shaderc.shaderc_compute_shader;

    public static byte[] compile(String fileName, String sourceText, int stage){
        long compiler = Shaderc.shaderc_compiler_initialize();
        long options = Shaderc.shaderc_compile_options_initialize();

        long result = Shaderc.shaderc_compile_into_spv(
                compiler,
                sourceText,
                stage,
                fileName,
                "main",
                options);

        if(Shaderc.shaderc_result_get_compilation_status(result) != Shaderc.shaderc_compilation_status_success){
            String message = Shaderc.shaderc_result_get_error_message(result);

            if(message == null || message.isBlank())
                message = "error code " + Shaderc.shaderc_result_get_compilation_status(result);

            throw new RuntimeException("Failed to compile shader: " + message);
        }

        ByteBuffer output = Shaderc.shaderc_result_get_bytes(result);
        byte[] outputArray = new byte[output.remaining()];
        output.get(outputArray);

        Shaderc.shaderc_result_release(result);
        Shaderc.shaderc_compile_options_release(options);
        Shaderc.shaderc_compiler_release(compiler);

        return outputArray;
    }
}
