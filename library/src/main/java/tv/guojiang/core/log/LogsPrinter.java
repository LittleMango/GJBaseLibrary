package tv.guojiang.core.log;


import android.util.Log;
import tv.guojiang.core.log.impl.IPrinter;

/**
 * author KK
 * date 2017/12/21
 */
public class LogsPrinter implements IPrinter {

    private LogsWriter mLogsWriter;
    private boolean isDebug = true;

    public LogsPrinter() {
        mLogsWriter = new LogsWriter();
    }

    public LogsPrinter(String path) {
        mLogsWriter = new LogsWriter(path);
    }

    @Override
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public void d(String tag, String msg, boolean isWrite) {
        print(Log.DEBUG, tag, msg, isWrite);
    }

    @Override
    public void e(String tag, String msg, boolean isWrite) {
        print(Log.ERROR, tag, msg, isWrite);
    }

    @Override
    public void w(String tag, String msg, boolean isWrite) {
        print(Log.WARN, tag, msg, isWrite);
    }

    @Override
    public void i(String tag, String msg, boolean isWrite) {
        print(Log.INFO, tag, msg, isWrite);
    }

    @Override
    public void v(String tag, String msg, boolean isWrite) {
        print(Log.VERBOSE, tag, msg, isWrite);
    }

    private void print(int type, String tag, String msg, boolean isWrite) {
        //        Logger.log(type, tag, msg, null);
        StackTraceElement caller = getCallerStackTraceElement();
        String t = generateTag(caller);
        if (isDebug) {
            Log.println(type, tag, "-----------------------------------------------------------");
            Log.println(type, tag, "|  " + t);
            Log.println(type, tag, "|  " + formatLogToString(tag, msg));
            Log.println(type, tag, "-----------------------------------------------------------");
        }
        if (isWrite) {
            mLogsWriter.logs(t + " \n---- " + tag, msg);
        }
    }


    static String formatLogToString(String tag, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append("[" + tag + "]");
        sb.append("[" + msg + "]");
        return sb.toString();
    }

    private static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length > 7) {
            return elements[7];
        } else {
            return elements[6];
        }
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String
            .format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber()); // 替换
        return tag;
    }
}
