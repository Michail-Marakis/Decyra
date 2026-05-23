package com.example.decyra.backend.ai.indexers;

import android.content.Context;
import android.util.Log;

import com.example.decyra.backend.ai.callbacks.EmbeddingCallback;
import com.example.decyra.backend.ai.clients.OpenAIChatClient;
import com.example.decyra.backend.ai.clients.PineconeClient;
import com.google.firebase.database.*;

import org.json.JSONObject;

import java.util.Objects;

/**
 * The type Pinecone indexer erasmus.
 */
public class PineconeIndexerErasmus {

    private final OpenAIChatClient openAIClient;
    private final PineconeClient pineconeClient;

    /**
     * Instantiates a new Pinecone indexer erasmus.
     *
     * @param context the context
     */
    public PineconeIndexerErasmus(Context context) {
        openAIClient = new OpenAIChatClient(context);
        pineconeClient = new PineconeClient();
    }

    /**
     * Index erasmus aueb.
     */
// --------------------------- AUEB ---------------------------
    public void indexErasmusAUEB() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Οικονομικό Πανεπιστήμιο Αθηνών")) continue;

                    Long idlong = item.child("id").getValue(Long.class);
                    String id = String.valueOf(idlong);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Οικονομικό Πανεπιστήμιο Αθηνών προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Οικονομικό Πανεπιστήμιο Αθηνών");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-AUEB",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "AUEB error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus ekpa.
     */
// --------------------------- EKPA ---------------------------
    public void indexErasmusEKPA() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Εθνικό και Καποδιστριακό Πανεπιστήμιο Αθηνών")) continue;

                    Long longid3 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid3);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Πανεπιστήμιο Αθηνών προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Πανεπιστήμιο Αθηνών");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-EKPA",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "EKPA error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus papei.
     */
// --------------------------- PAPEI ---------------------------
    public void indexErasmusPAPEI() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Πανεπιστήμιο Πειραιώς")) continue;

                    Long longid5 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid5);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Πανεπιστήμιο Πειραιά προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Πανεπιστήμιο Πειραιά");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-PAPEI",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "PAPEI error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus crete.
     */
// --------------------------- CRETE ---------------------------
    public void indexErasmusCRETE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Πανεπιστήμιο Κρήτης")) continue;

                    Long longid2 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid2);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Πανεπιστήμιο Κρήτης προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Πανεπιστήμιο Κρήτης");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-CRETE",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "CRETE error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus aristotle.
     */
// --------------------------- ARISTOTLE ---------------------------
    public void indexErasmusARISTOTLE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Αριστοτέλειο Πανεπιστήμιο Θεσσαλονίκης")) continue;

                    Long longid = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Αριστοτέλειο Πανεπιστήμιο Θεσσαλονίκης προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Αριστοτέλειο Πανεπιστήμιο Θεσσαλονίκης");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-ARISTOTLE",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "ARISTOTLE error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus harokopio.
     */
// --------------------------- HAROKOPIO ---------------------------
    public void indexErasmusHAROKOPIO() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Χαροκόπειο Πανεπιστήμιο")) continue;

                    Long longid = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Χαροκόπειο Πανεπιστήμιο προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Χαροκόπειο Πανεπιστήμιο");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-HAROKOPIO",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "HAROKOPIO error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus ionian.
     */
// --------------------------- IONIAN ---------------------------
    public void indexErasmusIONIAN() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Ιόνιο Πανεπιστήμιο")) continue;

                    Long longid4 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid4);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Ιόνιο Πανεπιστήμιο προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Ιόνιο Πανεπιστήμιο");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-IONIAN",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "IONIAN error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus thessaly.
     */
// --------------------------- THESSALY ---------------------------
    public void indexErasmusTHESSALY() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Πανεπιστήμιο Θεσσαλίας")) continue;

                    Long longid7 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid7);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Πανεπιστήμιο Θεσσαλίας προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Πανεπιστήμιο Θεσσαλίας");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-THESSALY",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "THESSALY error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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

    /**
     * Index erasmus pelloponese.
     */
// --------------------------- PELLOPONESE ---------------------------
    public void indexErasmusPELLOPONESE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if (!Objects.equals(uniName, "Πανεπιστήμιο Πελοποννήσου")) continue;

                    Long longid6 = item.child("id").getValue(Long.class);
                    String id = String.valueOf(longid6);
                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);

                    String text =
                            "Το Πανεπιστήμιο Πελοποννήσου προσφέρει πρόγραμμα Erasmus στο "
                                    + name + " στην πόλη " + city + " της χώρας " + country + ". "
                                    + "Η απαίτηση γλώσσας είναι: " + language + " και η χρηματοδότηση είναι "
                                    + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {
                            try {
                                JSONObject metadata = new JSONObject();
                                metadata.put("Όνομα Πανεπιστημίου", "Πανεπιστήμιο Πελοποννήσου");
                                metadata.put("Όνομα Προγράμματος", name);
                                metadata.put("Χώρα", country);
                                metadata.put("Πόλη", city);
                                metadata.put("Γλώσσα", language);
                                metadata.put("Χρηματοδότηση", fund);
                                metadata.put("Περιγραφή", text);

                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadata,
                                        "erasmus-PELLOPONESE",
                                        "Education"
                                );

                            } catch (Exception e) {
                                Log.e("INDEX", "PELLOPONESE error", e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", errorMessage);
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