package com.xwl.complier;

import com.google.auto.service.AutoService;
import com.xwl.annotation.BindTest;

import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AutoProcessor extends AbstractProcessor {
    private static final String TAG = "ANT: ";
    private List<BindTestMethod> bindTestMethods = new ArrayList<BindTestMethod>();
    private Filer mFiler;
    private Messager messager;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<String>();
        types.add(BindTest.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, "handle process...");
        System.out.print("process-------");
        bindTestMethods.clear();
        processBindTest(roundEnvironment);
        generateBindTest(bindTestMethods);
        return false;
    }

    public void generateBindTest(List<BindTestMethod> bindTestMethods) {
        log("test method size;" + bindTestMethods.size());
        if (bindTestMethods.size() == 0) {
            return;
        }
        String packageName = mElementUtils.getPackageOf(bindTestMethods.get(0).getExecutableElement()).getQualifiedName().toString();
        TypeElement element = (TypeElement) bindTestMethods.get(0).getExecutableElement().getEnclosingElement();
        log("print pkg name:" + packageName);
        try {
            JavaFileObject jfo = mFiler.createSourceFile("com.xwl.autotest.FakeDevelopReceiver",
                    element);
            Writer writer = jfo.openWriter();
            writer.write(brewJava(bindTestMethods));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log("exception:" + e.toString());
        }

    }


    private void processBindTest(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindTest.class)) {
            BindTestMethod method = new BindTestMethod(element);
            bindTestMethods.add(method);
            log("add element");
        }
    }

    private String generateSwitchCaseCode(List<BindTestMethod> bindTestMethods) {
        StringBuilder sb = new StringBuilder();
        sb.append("int extra = intent.getIntExtra(EXTRA_NAME, 0);\n");
        sb.append("int data0=intent.getIntExtra(EXTRA_DAT1, 0);\n");
        sb.append("int data1=intent.getIntExtra(EXTRA_DAT2, 0);\n");
        sb.append("String dataS0=intent.getStringExtra(EXTRA_DAT3);\n");
        sb.append("String dataS1= intent.getStringExtra(EXTRA_DAT4);\n");
        sb.append("boolean dataB0=intent.getBooleanExtra(EXTRA_DAT5,false);\n");
        sb.append("boolean dataB1= intent.getBooleanExtra(EXTRA_DAT6,false);\n");
        sb.append("Log.d(TAG,\"receive extra:\"+extra);\n");
        sb.append("switch(extra){\n");

        for (BindTestMethod bindTestMethod : bindTestMethods) {

            int index = bindTestMethod.getIndex();
            TypeElement classElement = (TypeElement) bindTestMethod
                    .getExecutableElement().getEnclosingElement();
            String fqClassName = classElement.getQualifiedName().toString();
            sb.append("case " + index + ":\n");
            sb.append("Log.d(TAG,\"handle case:\"+extra);\n");
            sb.append(fqClassName + ".getInstance(context)."
                    + bindTestMethod.getExecutableElement().getSimpleName()
                    + generateMethod(bindTestMethod.getExecutableElement())
                    + ";\n");
            sb.append("break;\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    private String generateMethod(ExecutableElement element) {
        int dataInt = 0;
        int dataString = 0;
        int dataBoolean = 0;

        if (element.getParameters().size() == 0) {
            return "()";
        }

        String args = handleMethodArg(element);
        if (args != "") {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            String[] argTypes = args.split(",");
            for (int i = 0; i < argTypes.length; i++) {
                String argType = argTypes[i];
                if (argType.contains("String")) {
                    sb.append("dataS" + dataString);
                    dataString++;
                } else if (argType.contains("int")) {
                    sb.append("data" + dataInt);
                    dataInt++;
                } else if (argType.contains("bool")) {
                    sb.append("dataB" + dataBoolean);
                    dataBoolean++;
                }
                if (i + 1 < argTypes.length) {
                    sb.append(",");
                }

            }
            sb.append(")");
            return sb.toString();
        }

        return "()";

    }

    private String handleMethodArg(ExecutableElement element) {

        int argSize = element.getParameters().size();
        if (argSize != 0) {
            String method = element.toString();
            return Utils.getParamsList(method);
        }
        return "";
    }



    private void log(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, TAG + msg);
    }

    private String brewJava(List<BindTestMethod> bindTestMethods) {
        //String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from AutoProcessor. Do not modify!\n");
        builder.append("package ").append("com.xwl.autotest").append(";\n\n");
        builder.append('\n');
        builder.append("import android.content.BroadcastReceiver;\n" +
                "import android.content.Context;\n" +
                "import android.content.Intent;\n");
        builder.append("import android.util.Log;\n");
        builder.append("public class ").append("FakeDevelopReceiver extends BroadcastReceiver ");
        builder.append(" {\n");
        builder.append("private final String TAG = \"FakeDevelopReceiver\";\n");
        builder.append("private final String ACTION_NAME = \t\"android.intent.action.test\";\n");
        builder.append("private final String EXTRA_NAME = \t\"android.intent.extra.test\";\n");
        builder.append("private final String EXTRA_DAT1 = \t\"android.intent.extra.test.dat1\";\n");
        builder.append("private final String EXTRA_DAT2 = \t\"android.intent.extra.test.dat2\";\n");
        builder.append("private final String EXTRA_DAT3 = \t\"android.intent.extra.test.dat3\";\n");
        builder.append("private final String EXTRA_DAT4 = \t\"android.intent.extra.test.dat4\";\n");
        builder.append("private final String EXTRA_DAT5 = \t\"android.intent.extra.test.dat5\";\n");
        builder.append("private final String EXTRA_DAT6 = \t\"android.intent.extra.test.dat6\";\n");
        builder.append("@Override\n");
        builder.append("public void onReceive(Context context, Intent intent)");
        builder.append("{\n");
        builder.append(generateSwitchCaseCode(bindTestMethods));
        builder.append("}\n");
        builder.append(" }\n");
        return builder.toString();
    }
}
