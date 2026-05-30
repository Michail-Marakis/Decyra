package com.example.decyra.backend.ai.indexers;

import android.content.Context;
import android.util.Log;

import com.example.decyra.backend.ai.callbacks.EmbeddingCallback;
import com.example.decyra.backend.ai.clients.OpenAIChatClient;
import com.example.decyra.backend.ai.clients.PineconeClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

/**
 * The type Pinecone indexer master career.
 */
public class PineconeIndexerMaster {

    private final OpenAIChatClient openAIClient;
    private final PineconeClient pineconeClient;

    /**
     * Instantiates a new Pinecone indexer master career.
     *
     * @param context the context
     */
    public PineconeIndexerMaster(Context context) {
        openAIClient = new OpenAIChatClient(context);
        pineconeClient = new PineconeClient();
    }


    /**
     * Index master programs.
     */
//---------------------------------------MASTER-----------------------------------------------------
    public void indexMasterPrograms() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("master");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    Long idlong = item.child("id").getValue(Long.class);
                    String id = String.valueOf(idlong);
                    String name = item.child("name").getValue(String.class);
                    String description = item.child("description").getValue(String.class);
                    String university_name = item.child("university_name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String cost = item.child("cost").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String region = item.child("region").getValue(String.class);

                    StringBuilder tagsBuilder = new StringBuilder();
                    DataSnapshot tagsSnapshot = item.child("field_tags");
                    for (DataSnapshot tag : tagsSnapshot.getChildren()) {
                        tagsBuilder.append(tag.getValue(String.class)).append(", ");
                    }
                    String tagsString = tagsBuilder.toString();


                    String textToEmbed = "Πρόγραμμα: " + name + " | Πανεπιστήμιο: " + university_name +
                            " | Τοποθεσία: " + country + " (" + region + ") | " +
                            "Γλώσσα: " + language + " | Κόστος: " + cost + " | " +
                            "Θεματικοί Τομείς: " + tagsString + " | " +
                            "Περιγραφή: " + description;

                    openAIClient.getEmbedding(textToEmbed, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {
                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("university_name", university_name);
                                metadataObject.put("description", description);
                                metadataObject.put("language", language);
                                metadataObject.put("cost", cost);
                                metadataObject.put("country", country);
                                metadataObject.put("region", region);
                                metadataObject.put("field_tags", tagsString);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "europe-master",
                                        "master"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing master: " + exception.getMessage());
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (master): " + errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("INDEX", "Firebase Database Error: " + error.getMessage());
            }
        });
    }

}