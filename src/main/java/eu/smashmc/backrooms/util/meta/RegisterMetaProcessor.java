package eu.smashmc.backrooms.util.meta;

import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorPhase;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RegisterMetaProcessor extends AbstractMetaProcessor {

    private RegisterCallback callback;

    public RegisterMetaProcessor(MetaRegistry registry, RegisterCallback callback) {
        super(registry, MetaProcessorPhase.RESCAN);
        this.callback = callback;
    }

    @Override
    public void process(Class<?> clazz) throws Exception {
        if(clazz.isAnnotationPresent(Register.class)) {
            Object instance = clazz.getConstructor().newInstance();
            callback.register(instance);
        }
    }

    public interface RegisterCallback {

        void register(Object instance);
    }

}
