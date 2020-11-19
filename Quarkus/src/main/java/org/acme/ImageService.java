package org.acme;

import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ImageService {

    @Inject
    MongoClient mongoClient;

    GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document("type", "image").append("content_type", "image/png"));


    public Image saveImageFile(FileInputStream imageStream, String title) {
        Document doc = new Document("title", title);
        getCollection().insertOne(doc);
        ObjectId documentId = (ObjectId) doc.get("_id");

        MongoDatabase db = mongoClient.getDatabase("imagedb");
        GridFSBuckets.create(db).uploadFromStream("image-" + documentId.toString(), imageStream, uploadOptions);
        return new Image(documentId, title);
    }

    public List<Image> listImages() {
        List<Image> images = new ArrayList<>();
        MongoDatabase db = mongoClient.getDatabase("imagedb");
        MongoCollection<Document> imagesCollection = db.getCollection("images");
        FindIterable<Document> imagesDoc = imagesCollection.find();
        for (Document imageDoc : imagesDoc) {
            // We can cast this since its formatted json
            ObjectId docId = (ObjectId) imageDoc.get("_id");
            Image cImage = new Image(docId, imageDoc.getString("title"));
            images.add(cImage);
        }
        return images;
    }

    private MongoDatabase getDatabase() {
        return mongoClient.getDatabase("imagedb");
    }

    private MongoCollection<Document> getCollection() {
        try {
            return getDatabase().getCollection("images");
            // This is thrown if the collection does not exist
        } catch (MongoCommandException e) {
            // Create the collection
            getDatabase().createCollection("images");
            // Return the collection now that we have created it.
            return getDatabase().getCollection("images");
        }
    }

    public Image getImage(String id) {
        Document details = getCollection().find(new Document("_id", new ObjectId(id))).first();
        if(details == null)
            return null;
        return new Image((ObjectId) details.get("_id"), details.getString("title"));
    }


    // Get an image as byte array from UUID
    public byte[] getImageFile(String documentId) {
        // We use GridFS to retrieve the file from the db id.
        GridFSFile imageDoc = GridFSBuckets.create(getDatabase()).find(new Document("filename", "image-" + documentId)).first();
        if (imageDoc == null)
            return null;
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        GridFSBuckets.create(mongoClient.getDatabase("imagedb")).downloadToStream(imageDoc.getObjectId(), oStream);
        return oStream.toByteArray();
    }


}