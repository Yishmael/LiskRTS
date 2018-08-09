package encaps;

import java.lang.reflect.Method;

public class MethodAndArgs {
    private Method method;
    private Object[] args;

    public MethodAndArgs(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

}
