# LLVM Library

This is a WIP (Work-In-Progress) library to make the usage of the LLVM-JavaBindings. Here is an example file.

**Thix example is not compiling, because it dont have any source**

````java
import de.cacheoverflow.llvmlib.LLVMContextBuilder;
import de.cacheoverflow.llvmlib.LLVMModuleBuilder;
import de.cacheoverflow.llvmlib.functions.LLVMFunctionBuilder;
import org.bytedeco.javacpp.LLVM;

import java.io.IOException;
import java.nio.file.Path;

public class Test {

    public static void main(String[] args) throws IOException {
        LLVMContextBuilder context = LLVMContextBuilder.builder();

        LLVMModuleBuilder module = context.createModuleByName("cacheoverflow/Main1");
        LLVMFunctionBuilder fun = module.createFunction("main1", new Class[]{int.class}, int.class, false);
        fun.addBlock("entry").next();
        module.next();
        fun.next();

        module = context.createModuleByName("cacheoverflow/Main2");
        fun = module.createFunction("main2", new Class[]{int.class}, int.class, false);
        fun.addBlock("entry").next();
        module.next();
        fun.next();

        context.build(LLVM.LLVMGetDefaultTargetTriple(), Path.of("output"), LLVM.LLVMObjectFile);
    }

}
````