package de.cacheoverflow.llvmlib;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.LLVM;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LLVMContextBuilder {

    private final List<LLVMModuleBuilder> moduleBuilders = new ArrayList<>();
    private final LLVM.LLVMContextRef context;

    private LLVMContextBuilder() {
        this.context = LLVM.LLVMContextCreate();
    }

    public static @NotNull LLVMContextBuilder builder() {
        return new LLVMContextBuilder();
    }

    public @NotNull LLVMModuleBuilder createModuleByName(@NotNull final String name) {
        LLVMModuleBuilder builder = new LLVMModuleBuilder(name, this);
        this.moduleBuilders.add(builder);
        return builder;
    }

    public void build(@NotNull final BytePointer targetTriple, @NotNull final Path outputDirectory, final int targetFileType) throws IOException {

        for (LLVMModuleBuilder builder : this.moduleBuilders) {
            byte[] message = new byte[8192];
            outputDirectory.resolve(builder.getName()).getParent().toFile().mkdirs(); // To lazy for good solution

            if (LLVM.LLVMVerifyModule(builder.getModule(), LLVM.LLVMAbortProcessAction, message) != 0)
                throw new RuntimeException(new String(message));

            LLVM.LLVMInitializeAllTargetInfos();
            LLVM.LLVMInitializeAllTargets();
            LLVM.LLVMInitializeAllTargetMCs();
            LLVM.LLVMInitializeAllAsmParsers();
            LLVM.LLVMInitializeAllAsmPrinters();

            LLVM.LLVMTargetRef reference = new LLVM.LLVMTargetRef();
            if (LLVM.LLVMGetTargetFromTriple(targetTriple, reference, message) != 0)
                throw new RuntimeException(new String(message));

            LLVM.LLVMTargetMachineRef targetMachine = LLVM.LLVMCreateTargetMachine(reference, targetTriple,
                    new BytePointer("generic"), new BytePointer(""), LLVM.LLVMCodeGenLevelDefault,
                    LLVM.LLVMRelocDefault, LLVM.LLVMCodeModelDefault);

            LLVM.LLVMSetModuleDataLayout(builder.getModule(), LLVM.LLVMCreateTargetDataLayout(targetMachine));
            LLVM.LLVMSetTarget(builder.getModule(), targetTriple);

            if (LLVM.LLVMTargetMachineEmitToFile(targetMachine, builder.getModule(), outputDirectory.resolve(builder.getName() + ".o")
                    .toString().getBytes(StandardCharsets.UTF_8), targetFileType, message) != 0)
                throw new RuntimeException(new String(message));

            LLVM.LLVMDisposeModule(builder.getModule());
            LLVM.LLVMDisposeMessage(message);
        }
        LLVM.LLVMContextDispose(this.context);
    }

    public @NotNull LLVM.LLVMContextRef getContext() {
        return context;
    }

}
