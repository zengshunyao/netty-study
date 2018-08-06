package com.gupaoedu.catalina.servlets;

import com.gupaoedu.catalina.http.GPRequest;
import com.gupaoedu.catalina.http.GPResponse;

public abstract class GPServlet {

    public abstract void doGet(GPRequest request, GPResponse reponse) throws Exception;

    public abstract void doPost(GPRequest request, GPResponse reponse) throws Exception;
}
