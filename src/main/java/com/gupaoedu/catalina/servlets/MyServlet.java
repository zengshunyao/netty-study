package com.gupaoedu.catalina.servlets;

import com.gupaoedu.catalina.http.GPRequest;
import com.gupaoedu.catalina.http.GPResponse;

public class MyServlet extends GPServlet {
    @Override
    public void doGet(GPRequest request, GPResponse reponse) throws Exception {

        String value = request.getParameter("name");
        reponse.write(value);
    }

    @Override
    public void doPost(GPRequest request, GPResponse reponse) throws Exception {
        this.doGet(request, reponse);
    }
}
