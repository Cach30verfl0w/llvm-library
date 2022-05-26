package de.cacheoverflow.llvmlib;

import de.cacheoverflow.llvmlib.functions.LLVMFunctionBuilder;
import de.cacheoverflow.llvmlib.types.LLVMTypeFactory;
import org.bytedeco.javacpp.LLVM;
import org.bytedeco.javacpp.PointerPointer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LLVMModuleBuilder {

    private static final LLVMTypeFactory LLVM_TYPE_FACTORY = new LLVMTypeFactory();

    private final String name;
    private final LLVM.LLVMModuleRef module;
    private final LLVM.LLVMBuilderRef builder;
    private final LLVMContextBuilder contextBuilder;

    public LLVMModuleBuilder(@NotNull final String name, @NotNull final LLVMContextBuilder contextBuilder) {
        this.module = LLVM.LLVMModuleCreateWithNameInContext(name, contextBuilder.getContext());
        this.builder = LLVM.LLVMCreateBuilderInContext(contextBuilder.getContext());
        this.contextBuilder = contextBuilder;
        this.name = name;
    }

    public LLVMFunctionBuilder createFunction(@NotNull final String name, @NotNull final Class<?>[] rawArgs, @NotNull final Class<?> returnType, final boolean isVararg) {
        LLVM.LLVMTypeRef[] args = Arrays.stream(rawArgs).map(arg -> LLVM_TYPE_FACTORY.getTypeFromContext(this.contextBuilder.getContext(), arg)).toArray(LLVM.LLVMTypeRef[]::new);

        PointerPointer<LLVM.LLVMTypeRef> argsPointer = new PointerPointer<>(args);
        LLVM.LLVMTypeRef type = LLVM.LLVMFunctionType(LLVM_TYPE_FACTORY.getTypeFromContext(this.contextBuilder.getContext(), returnType), argsPointer, args.length, isVararg ? 1 : 0);
        LLVM.LLVMValueRef function = LLVM.LLVMAddFunction(this.module, name, type);
        return new LLVMFunctionBuilder(this, function);
    }

    public @NotNull LLVMContextBuilder next() {
        LLVM.LLVMDumpModule(this.module);
        return this.contextBuilder;
    }

    public @NotNull LLVMContextBuilder getContextBuilder() {
        return contextBuilder;
    }

    public @NotNull LLVM.LLVMBuilderRef getBuilder() {
        return builder;
    }

    public @NotNull LLVM.LLVMModuleRef getModule() {
        return module;
    }

    public @NotNull String getName() {
        return name;
    }

}
