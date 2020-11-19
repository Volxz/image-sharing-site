package org.acme;

import org.bson.Document;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Path("/api/i")
public class ImageResource {

    @Inject
    ImageService imageService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Image> getAllImages() {
        return imageService.listImages();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload (@MultipartForm ImageFormData formData) {

        if( formData == null) {
            return Response.status(422).entity(new Document("error", "No request body provided.").toJson()).build();
        }

        // Form validation
        // Max file size: 50 MB
        if(formData.file == null || formData.file.length() > 50000000) {
            return Response.status(413).entity(new Document("error", "Invalid image payload. Image must be less than 50MB.").toJson()).build();
        }

        // Title is > 3 and < 30
        if(formData.title == null || formData.title.length() > 30 || formData.title.length() <  3){
            return Response.status(422).entity(new Document("error", "Invalid data for the file name. Minimum 3 chars max 30.").toJson()).build();
        }

        // Create an image object for easier serialization in the DB
        try {
            Image uploadedImage = imageService.saveImageFile(new FileInputStream(formData.file), formData.title);
            // Return the URL of the new image
            return Response.created(URI.create("/api/i/" + uploadedImage.getId())).build();
            // Catch in-case the file is corrupt.
        } catch (IOException e ) {
            return Response.status(422).entity(new Document("error", "Could not read the file.").toJson()).build();
        }
    }

    // Url paths are the new cool thing
    @Path("/{documentId}.png")
    @GET
    public Response getImage(@PathParam("documentId") String uuid) {
        // Get our image as a byte array from the DB
        byte[] image = imageService.getImageFile(uuid);
        // If no image, 404.
        if(image == null || image.length <= 0)
            return Response.status(404).build();

        return Response.ok(image, "image/png").build();
    }

    @Path("/{uuid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetails(@PathParam("uuid") String uuid) {

        Image imageDetails = imageService.getImage(uuid);
        if(imageDetails == null) {
            return Response.status(404).build();
        }
        return Response.ok(imageDetails).build();
    }
}