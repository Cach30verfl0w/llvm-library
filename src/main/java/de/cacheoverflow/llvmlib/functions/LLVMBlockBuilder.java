package de.cacheoverflow.llvmlib.functions;

import de.cacheoverflow.llvmlib.types.LLVMTypeFactory;
import org.bytedeco.javacpp.LLVM;
import org.jetbrains.annotations.NotNull;

public class LLVMBlockBuilder {

    private static final LLVMTypeFactory LLVM_TYPE_FACTORY = new LLVMTypeFactory();

    private final LLVMFunctionBuilder functionBuilder;
    private final LLVM.LLVMBasicBlockRef block;

    public LLVMBlockBuilder(@NotNull final LLVMFunctionBuilder functionBuilder, @NotNull final LLVM.LLVMBasicBlockRef block) {
        this.functionBuilder = functionBuilder;
        this.block = block;

        LLVM.LLVMValueRef val2 = LLVM.LLVMConstInt(LLVM_TYPE_FACTORY.getTypeFromContext(functionBuilder.getModuleBuilder().getContextBuilder().getContext(), Integer.class), 1, 0);
        LLVM.LLVMBuildRet(this.functionBuilder.getModuleBuilder().getBuilder(), LLVM.LLVMBuildAdd(this.functionBuilder.getModuleBuilder().getBuilder(), this.functionBuilder.getParameter(0), val2, "sum"));
    }

    public @NotNull LLVMFunctionBuilder next() {
        return this.functionBuilder;
    }

    public LLVMFunctionBuilder getFunctionBuilder() {
        return functionBuilder;
    }

}
