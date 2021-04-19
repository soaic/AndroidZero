package com.soaic.butterknife_compiler;

//import com.google.auto.service.AutoService;
import com.soaic.butterknife_annotations.BindView;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

//@AutoService(Processor.class)
public class ButterKnife extends AbstractProcessor {
    // 指定处理的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    // 给到需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();

        for (Class<? extends Annotation> annotations : getSupportedAnnotations()) {
            types.add(annotations.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new HashSet<>();
        // 需要解析的自定义注解 BindView onClick
        annotations.add(BindView.class);
        return annotations;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("---------------------");

        return false;
    }
}
