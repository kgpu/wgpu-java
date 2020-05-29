package com.noahcharlton.wgpuj.util;

import jnr.ffi.Pointer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class RustCString {

    public static String fromPointer(Pointer pointer){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        for(long i = 0;; i++){
            byte nextChar = pointer.getByte(i);

            if(nextChar == 0x00){
                break;
            }

            stream.write(nextChar);
        }

        return stream.toString(StandardCharsets.UTF_8);
    }
}
