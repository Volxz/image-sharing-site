package org.acme;

import javax.ws.rs.FormParam;
import java.io.File;

public class ImageFormData {
    @FormParam("file")
    public File file;

    @FormParam("title")
    public String title;
}
