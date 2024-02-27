package in.godspunky.skyblock.nms.nmsutil.reflection.resolver.wrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodWrapper<R> extends WrapperAbstract {
    private final Method method;

    public MethodWrapper(Method method) {
        this.method = method;
    }

    @Override
    public boolean exists() {
        return null != this.method;
    }

    public String getName() {
        return this.method.getName();
    }

    public R invoke(Object object, Object... args) {
        try {
            return (R) this.method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public R invokeSilent(Object object, Object... args) {
        try {
            return (R) this.method.invoke(object, args);
        } catch (Exception e) {
            return null;
        }
    }

    public Method getMethod() {
        return this.method;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object || this.getClass() != object.getClass()) {
            return false;
        }
        MethodWrapper<?> that = (MethodWrapper<?>) object;
        return Objects.equals(this.method, that.method);
    }

    @Override
    public int hashCode() {
        return (null != this.method) ? this.method.hashCode() : 0;
    }

    public static String getMethodSignature(Method method, boolean fullClassNames) {
        return MethodSignature.of(method, fullClassNames).getSignature();
    }

    public static String getMethodSignature(Method method) {
        return getMethodSignature(method, false);
    }

    public static class MethodSignature {
        static final Pattern SIGNATURE_STRING_PATTERN;
        private final String returnType;
        private final Pattern returnTypePattern;
        private final String name;
        private final Pattern namePattern;
        private final String[] parameterTypes;
        private final String signature;

        public MethodSignature(String returnType, String name, String[] parameterTypes) {
            this.returnType = returnType;
            this.returnTypePattern = Pattern.compile(returnType.replace("?", "\\w").replace("*", "\\w*"));
            this.name = name;
            this.namePattern = Pattern.compile(name.replace("?", "\\w").replace("*", "\\w*"));
            this.parameterTypes = parameterTypes;
            StringBuilder builder = new StringBuilder();
            builder.append(returnType).append(" ").append(name).append("(");
            boolean first = true;
            for (String parameterType : parameterTypes) {
                if (!first) {
                    builder.append(",");
                }
                builder.append(parameterType);
                first = false;
            }
            this.signature = builder.append(")").toString();
        }

        public static MethodSignature of(Method method, boolean fullClassNames) {
            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();
            final String returnTypeString;
            if (returnType.isPrimitive()) {
                returnTypeString = returnType.toString();
            } else {
                returnTypeString = (fullClassNames ? returnType.getName() : returnType.getSimpleName());
            }
            String methodName = method.getName();
            String[] parameterTypeStrings = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypeStrings.length; ++i) {
                if (parameterTypes[i].isPrimitive()) {
                    parameterTypeStrings[i] = parameterTypes[i].toString();
                } else {
                    parameterTypeStrings[i] = (fullClassNames ? parameterTypes[i].getName() : parameterTypes[i].getSimpleName());
                }
            }
            return new MethodSignature(returnTypeString, methodName, parameterTypeStrings);
        }

        public static MethodSignature fromString(String signatureString) {
            if (null == signatureString) {
                return null;
            }
            Matcher matcher = SIGNATURE_STRING_PATTERN.matcher(signatureString);
            if (!matcher.find()) {
                throw new IllegalArgumentException("invalid signature");
            }
            if (3 != matcher.groupCount()) {
                throw new IllegalArgumentException("invalid signature");
            }
            return new MethodSignature(matcher.group(1), matcher.group(2), matcher.group(3).split(","));
        }

        public String getReturnType() {
            return this.returnType;
        }

        public boolean isReturnTypeWildcard() {
            return "?".equals(this.returnType) || "*".equals(this.returnType);
        }

        public String getName() {
            return this.name;
        }

        public boolean isNameWildcard() {
            return "?".equals(this.name) || "*".equals(this.name);
        }

        public String[] getParameterTypes() {
            return this.parameterTypes;
        }

        public String getParameterType(int index) throws IndexOutOfBoundsException {
            return this.parameterTypes[index];
        }

        public boolean isParameterWildcard(int index) throws IndexOutOfBoundsException {
            return "?".equals(this.getParameterType(index)) || "*".equals(this.getParameterType(index));
        }

        public String getSignature() {
            return this.signature;
        }

        public boolean matches(MethodSignature other) {
            if (null == other) {
                return false;
            }
            if (!this.returnTypePattern.matcher(other.returnType).matches()) {
                return false;
            }
            if (!this.namePattern.matcher(other.name).matches()) {
                return false;
            }
            if (this.parameterTypes.length != other.parameterTypes.length) {
                return false;
            }
            for (int i = 0; i < this.parameterTypes.length; ++i) {
                if (!Pattern.compile(this.getParameterType(i).replace("?", "\\w").replace("*", "\\w*")).matcher(other.getParameterType(i)).matches()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (null == o || this.getClass() != o.getClass()) {
                return false;
            }
            MethodSignature signature1 = (MethodSignature) o;
            return this.returnType.equals(signature1.returnType) && this.name.equals(signature1.name) && Arrays.equals(this.parameterTypes, signature1.parameterTypes) && this.signature.equals(signature1.signature);
        }

        @Override
        public int hashCode() {
            int result = this.returnType.hashCode();
            result = 31 * result + this.name.hashCode();
            result = 31 * result + Arrays.hashCode(this.parameterTypes);
            result = 31 * result + this.signature.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return this.getSignature();
        }

        static {
            SIGNATURE_STRING_PATTERN = Pattern.compile("(.+) (.*)\\((.*)\\)");
        }
    }
}
