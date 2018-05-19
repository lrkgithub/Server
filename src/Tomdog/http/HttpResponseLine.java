package Tomdog.http;

import static Tomdog.http.HTTPCommon.defaultHttpVersion;

class HttpResponseLine {

    private String httpVersion;

    private HttpStatusCode statusCode;

    private Integer code;

    private String reason_phrase;

    String getHttpVersion() {
        return httpVersion;
    }

    void setHttpVersion(String version) {

        if (!version.equals(defaultHttpVersion)) {

//    应该使用log来打印，为了方便起见，这里直接打印
System.out.println("only support http/1.1");

            this.httpVersion = defaultHttpVersion;

        } else {

            this.httpVersion = version;

        }

    }

    @Deprecated
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    void setStatusCode(HttpStatusCode statusCode) {

        if (statusCode == null) {
            return;
        }

        String phrase = HttpStatusCode.getPhr(statusCode);
        Integer code = HttpStatusCode.getCode(statusCode);

        if (phrase != null || code != null) {
            this.statusCode = statusCode;
            this.reason_phrase = phrase;
            this.code = code;
        }
    }

    Integer getCode() {
        return this.code;
    }

    String getReasonPhrase() {
        return this.reason_phrase;
    }
}