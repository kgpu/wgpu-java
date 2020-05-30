package com.noahcharlton.wgpuj.jni;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.Delegate;

public interface RequestAdapterCallback {

    @Delegate
    void request_adapter_callback(long received, Pointer userData);
}
