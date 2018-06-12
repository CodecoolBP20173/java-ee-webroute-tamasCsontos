

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        System.out.println("Request path" + path);

        for (Method method : getClass().getMethods()) {
            WebRoute webroute = method.getAnnotation(WebRoute.class);
            if (webroute != null && webroute.path().equals(path)) {
                try {
                    method.invoke(this, httpExchange);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                httpExchange.sendResponseHeaders(404,0);
            }

        }
//        if (path.equals("/test1")) {
//            handleTest1(httpExchange);
//        }
//        else if(path.equals("/test2")){
//            handleTest2(httpExchange);
//
//        }
//        else {
//            httpExchange.sendResponseHeaders(404, 0);
//
//
//            System.out.println("Cannot handler request");
//        }
        httpExchange.close();
    }

    @WebRoute(path = "/test1")
    public void handleTest1(HttpExchange httpExchange) throws IOException {
        byte[] bytes = "lofasz1234".getBytes();
        httpExchange.sendResponseHeaders(200, bytes.length);
        httpExchange.getResponseBody().write(bytes);
        System.out.println("test 1 handled");
    }

    @WebRoute(path = "/test2")
    public void handleTest2(HttpExchange httpExchange) throws IOException {
        byte[] bytes = "lofasz5678".getBytes();
        httpExchange.sendResponseHeaders(200, bytes.length);
        httpExchange.getResponseBody().write(bytes);
        System.out.println("test 2 handled");
    }
}
