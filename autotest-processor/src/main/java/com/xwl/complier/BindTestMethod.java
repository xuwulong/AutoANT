package com.xwl.complier;


import com.xwl.annotation.BindTest;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

/**
 * Created by xuwulong on 2018/1/9.
 */

public class BindTestMethod {
    private ExecutableElement executableElement;
    private int index;

    public BindTestMethod(Element element) {
        executableElement = (ExecutableElement) element;
        BindTest bindTest = executableElement.getAnnotation(BindTest.class);
        index = bindTest.index();
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public int getIndex() {
        return index;
    }

}
