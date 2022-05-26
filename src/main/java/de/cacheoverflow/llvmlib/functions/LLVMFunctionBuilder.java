package de.cacheoverflow.llvmlib.functions;

import de.cacheoverflow.llvmlib.LLVMModuleBuilder;
import org.bytedeco.javacpp.LLVM;
import org.jetbrains.annotations.NotNull;

public class LLVMFunctionBuilder {

    private final LLVMModuleBuilder moduleBuilder;
    private final LLVM.LLVMValueRef function;

    private boolean isMainBlockBuilt;

    public LLVMFunctionBuilder(@NotNull final LLVMModuleBuilder moduleBuilder, @NotNull final LLVM.LLVMValueRef function) {
        this.moduleBuilder = moduleBuilder;
        this.function = function;
    }

    public @NotNull LLVMBlockBuilder addBlock(@NotNull final String blockName) {
        if (blockName.equals("entry")) this.isMainBlockBuilt = true;

        LLVM.LLVMBasicBlockRef block = LLVM.LLVMAppendBasicBlockInContext(this.moduleBuilder.getContextBuilder().getContext(), this.function, blockName);
        LLVM.LLVMPositionBuilderAtEnd(this.moduleBuilder.getBuilder(), block);
        return new LLVMBlockBuilder(this, block);
    }

    public LLVM.LLVMValueRef getParameter(final int index) {
        return LLVM.LLVMGetParam(this.function, index);
    }

    public @NotNull LLVMModuleBuilder next() {
        if (!this.isMainBlockBuilt)
            throw new IllegalStateException("The entry block of the function is not built!");

        return this.moduleBuilder;
    }

    public LLVMModuleBuilder getModuleBuilder() {
        return moduleBuilder;
    }

    public LLVM.LLVMValueRef getFunction() {
        return function;
    }

}
