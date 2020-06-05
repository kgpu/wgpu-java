package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.fail.RustFailCallback;
import com.noahcharlton.wgpuj.util.SharedLibraryLoader;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Runtime;

public class WgpuNativeTest {

    protected static WgpuTest wgpuTest;

    static {
        var file = new SharedLibraryLoader().load("wgpu_test");

        wgpuTest = LibraryLoader.create(WgpuTest.class).load(file.getAbsolutePath());
        WgpuJava.setRuntime(Runtime.getRuntime(wgpuTest));

        wgpuTest.set_fail_callback(new RustFailCallback.RustFailCallbackImpl());
    }
}
