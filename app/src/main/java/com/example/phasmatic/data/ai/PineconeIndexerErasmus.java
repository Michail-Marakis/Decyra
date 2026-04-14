package com.example.phasmatic.data.ai;

import android.content.Context;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Objects;

public class PineconeIndexerErasmus {


    private final OpenAIChatClient openAIClient;
    private final PineconeClient pineconeClient;

    public PineconeIndexerErasmus(Context context) {
        openAIClient = new OpenAIChatClient(context);
        pineconeClient = new PineconeClient();
    }


    //---------------------------------AUEB-----------------------------------------------------
    public void indexErasmusAUEB() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "Athens University of Economics and Business")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Οικονομικό Πανεπιστήμιο Αθηνών (ΟΠΑ/AUEB) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-AUEB",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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


    //-----------------------------------------------EKPA----------------------------------------------------

    public void indexErasmusEKPA() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "University of Athens")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Πανεπιστήμειο Αθηνών (ΕΚΠΑ/UOA) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-EKPA",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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


    //-------------------------------------------PAPEI---------------------------------------------------------------------

    public void indexErasmusPAPEI() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "University of Piraeus")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Πανεπιστήμειο Πειραιά (ΠΑΠΕΙ/PAPEI) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-PAPEI",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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


    //-----------------------------------------CRETE--------------------------------------------------------------------

    public void indexErasmusCRETE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "University of Crete")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Πανεπιστήμειο Κρήτης (CSD/UOC) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-CRETE",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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


    //------------------------------------------ARISTOTELIO-------------------------------------------------------------

    public void indexErasmusARISTOTLE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "Aristotle University")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Αριστοτέλειο Πανεπιστήμειο Θεσσαλονίκης (ΑΠΘ/AUTH) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-ARISTOTLE",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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

    //------------------------------------------------------HAROKOPIO----------------------------------------------------

    public void indexErasmusHAROKOPIO() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "Harokopio University")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Χαροκόπειο Πανεπιστήμειο(ΧΠΑ/HUA) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-HAROKOPIO",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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

    //-----------------------------------------------IONIAN--------------------------------------------------------------------

    public void indexErasmusIONIAN() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "Ionian University")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Ιόνειο Πανεπιστήμειο(ΙΟΠ/IOP) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-IONIAN",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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

    //----------------------------------------THESSALY------------------------------------------------------------

    public void indexErasmusTHESSALY() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "University of Thessaly")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Πανεπιστήμειο της Θεσσαλίας (ΠΑΘ/UTH) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-THESSALY",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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

    //------------------------------------------------PELOPONNESE----------------------------------------------

    public void indexErasmusPELLOPONESE() {

        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference("erasmus");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    String uniName = item.child("university_name").getValue(String.class);
                    if(!Objects.equals(uniName, "University of Peloponnese")) continue;


                    String id = item.child("id").getValue(String.class);

                    String city = item.child("city").getValue(String.class);
                    String country = item.child("country").getValue(String.class);
                    String name = item.child("name").getValue(String.class);
                    String language = item.child("language").getValue(String.class);
                    String fund = item.child("fund").getValue(String.class);


                    String text = "Το Πανεπιστήμειο της Πελλοπονήσου(ΠΑΠΕ/PAPE) προσφέρει πρόγραμμα Erasmus στο πανεπιστήμιο "
                            + name + " στην πόλη " + city + " της χώρας " + country + ". "
                            + "Η απαίτηση ξένης γλώσσας είναι μια απο: " + language + " και η μηνιαία επιχορήγηση (fund) ανέρχεται σε " + fund + " ευρώ.";

                    openAIClient.getEmbedding(text, new EmbeddingCallback() {

                        @Override
                        public void onSuccess(float[] embeddingVector) {

                            try {

                                JSONObject metadataObject = new JSONObject();
                                metadataObject.put("name", name);
                                metadataObject.put("country", country);
                                metadataObject.put("city", city);
                                metadataObject.put("language", language);
                                metadataObject.put("fund", fund);


                                pineconeClient.upsertVector(
                                        embeddingVector,
                                        id,
                                        metadataObject,
                                        "erasmus-PELLOPONESE",
                                        "Education"
                                );

                            } catch (Exception exception) {
                                Log.e("INDEX", "Error indexing Erasmus", exception);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("INDEX", "Embedding error (fields): " + errorMessage);
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
