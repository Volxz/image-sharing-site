package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;

@Path("/")
public class FrontendResource {

    final String spaHTML = new Scanner(this.getClass().getResourceAsStream("/META-INF/resources/index.html"), "UTF-8").useDelimiter("\\A").next();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getSpaImageList() {
        return Response.status(200).entity(spaHTML).build();
    }

    @GET
    @Path("i/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response getImage() {
        return Response.status(200).entity(spaHTML).build();
    }

    @GET
    @Path("upload")
    @Produces(MediaType.TEXT_HTML)
    public Response getUploadPage() {
        return Response.status(200).entity(spaHTML).build();
    }
}
