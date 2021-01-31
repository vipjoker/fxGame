package mygame.editor.util;

public class ErrorUtil {
    private void printStackTrace() {
        StringBuilder builder = new StringBuilder();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            builder.append(stackTraceElement.getClassName())
                    .append(" : ")
                    .append(stackTraceElement.getMethodName())
                    .append("()")
                    .append(": line ")
                    .append(stackTraceElement.getLineNumber())
                    .append("\n");
            for (int j = 0; j < i; j++) {
                builder.append("\t");
            }

        }

        System.out.println(builder.toString());
    }
}
