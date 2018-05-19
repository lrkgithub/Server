package Tomdog.webFrame.ServletApi;

import Tomdog.http.HttpRequest;
import Tomdog.http.HttpResponse;

public abstract class HttpServlet implements Servlet {

    protected void doGet(HttpRequest request, HttpResponse response) {
        response.sendError();
    }

    protected void doPost(HttpRequest request, HttpResponse response) {
        response.sendError();
    }

    protected void doDeleted(HttpRequest request, HttpResponse response) {
        response.sendError();
    }

}
