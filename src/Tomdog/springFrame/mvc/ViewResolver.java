package Tomdog.springFrame.mvc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewResolver {

    private String viewName;
    private File templateFile;

    public ViewResolver(String viewName, File templateFile) {
        this.viewName = viewName;
        this.templateFile = templateFile;
    }

    public String resolve(ModelAndView modelAndView) throws IOException {

        StringBuffer sb = new StringBuffer();

        try (RandomAccessFile file = new RandomAccessFile(this.templateFile, "r")) {

            String line = null;

            while (null != (line = file.readLine())) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                Matcher matcher = matcher(line);
                while (matcher.find()) {

                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        String paramName = matcher.group(i);
                        Object paramValue = modelAndView.getModel().get(paramName);

                        if (null == paramValue) {
                            continue;
                        }

                        line = line.replaceAll("￥\\{" + paramName + "\\}", paramValue.toString());

                        line = new String(line.getBytes("utf-8"), "ISO-8859-1");
                    }

                }
                sb.append(line);
            }

        }
        return sb.toString();
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    private Matcher matcher(String line) {
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        return matcher;
    }
}
