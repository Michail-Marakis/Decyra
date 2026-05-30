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
 * The type Pinecone indexer career.
 */
public class PineconeIndexerCareer {


    private final OpenAIChatClient openAIClient;
    private final PineconeClient pineconeClient;

    /**
     * Instantiates a new Pinecone indexer master career.
     *
     * @param context the context
     */
    public PineconeIndexerCareer(Context context) {
        openAIClient = new OpenAIChatClient(context);
        pineconeClient = new PineconeClient();
    }

    /**
     * Index career.
     */
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
