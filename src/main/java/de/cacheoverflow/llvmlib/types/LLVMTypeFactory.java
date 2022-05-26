package de.cacheoverflow.llvmlib.types;

import org.bytedeco.javacpp.LLVM;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LLVMTypeFactory {

    private final Map<Class<?>, Function<LLVM.LLVMContextRef, LLVM.LLVMTypeRef>> factoryMap = new HashMap<>();

    public LLVMTypeFactory() {
        this.factoryMap.put(Byte.class, LLVM::LLVMInt8TypeInContext);
        this.factoryMap.put(Character.class, LLVM::LLVMInt16TypeInContext);
        this.factoryMap.put(Short.class, LLVM::LLVMInt32TypeInContext);
        this.factoryMap.put(Integer.class, LLVM::LLVMInt64TypeInContext);
        this.factoryMap.put(Long.class, LLVM::LLVMInt128TypeInContext);
        this.factoryMap.put(Void.class, LLVM::LLVMVoidTypeInContext);
        this.factoryMap.put(Double.class, LLVM::LLVMDoubleTypeInContext);
        this.factoryMap.put(Float.class, LLVM::LLVMFloatTypeInContext);

        this.factoryMap.put(byte.class, LLVM::LLVMInt8TypeInContext);
        this.factoryMap.put(char.class, LLVM::LLVMInt16TypeInContext);
        this.factoryMap.put(short.class, LLVM::LLVMInt32TypeInContext);
        this.factoryMap.put(int.class, LLVM::LLVMInt64TypeInContext);
        this.factoryMap.put(long.class, LLVM::LLVMInt128TypeInContext);
        this.factoryMap.put(void.class, LLVM::LLVMVoidTypeInContext);
        this.factoryMap.put(double.class, LLVM::LLVMDoubleTypeInContext);
        this.factoryMap.put(float.class, LLVM::LLVMFloatTypeInContext);
    }

    public @NotNull LLVM.LLVMTypeRef getTypeFromContext(@NotNull final LLVM.LLVMContextRef context, @NotNull final Class<?> typeClass) {
        return this.factoryMap.get(typeClass).apply(context);
    }

}
