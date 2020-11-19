package org.acme;

import org.bson.types.ObjectId;

public class Image {

    private String title;
    private ObjectId id;

    public Image(ObjectId id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return this.id.toString();
    }

    public String getTitle() {
        return this.title;
    }


}
