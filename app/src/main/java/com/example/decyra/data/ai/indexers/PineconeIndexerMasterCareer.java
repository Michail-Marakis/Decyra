package com.example.decyra.data.ai.indexers;

import android.content.Context;
import android.util.Log;

import com.example.decyra.data.ai.callbacks.EmbeddingCallback;
import com.example.decyra.data.ai.clients.OpenAIChatClient;
import com.example.decyra.data.ai.clients.PineconeClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class PineconeIndexerMasterCareer {

    private final OpenAIChatClient openAIClient;
    private final PineconeClient pineconeClient;

    public PineconeIndexerMasterCareer(Context context) {
        openAIClient = new OpenAIChatClient(context);
        pineconeClient = new PineconeClient();
    }


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

    //------------------------------------------------CAREER----------------------------------------------------
    public void indexCareer() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("career");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    Long idLong = item.child("id").getValue(Long.class);
                    Long salaryLong = item.child("avg_salary_with_master").getValue(Long.class);
                    Long salaryNoMasterLong = item.child("avg_salary_no_master").getValue(Long.class);
                    String field_name = item.child("field_name").getValue(String.class);

                    String salary = String.valueOf(salaryLong);
                    String salaryNoMaster = String.valueOf(salaryNoMasterLong);
                    String id = String.valueOf(idLong);
                    String country_name = item.child("country_name").getValue(String.class);


                    String text = "Στη χώρα " + country_name + " για τον τομέα " + field_name + ", " +
                            "ο μέσος μισθός ΜΕ μεταπτυχιακό (Master) είναι " + salary + " ευρώ. " +
                            "Ο μέσος μισθός ΧΩΡΙΣ μεταπτυχιακό είναι " + salaryNoMaster + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("salary_With_Master", salary);
                                metadataObject.put("salary_WithOut_Master", salaryNoMaster);
                                metadataObject.put("field_name", field_name);
                                metadataObject.put("country_name", country_name);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "main-career",
                                        "career"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing career", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (career): " + errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }


}