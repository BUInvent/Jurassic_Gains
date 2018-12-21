package com.buinvent.jurassic_gains;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


// Activity for the screen that gives users the ability to select the week they're training
public class WeekActivity extends AppCompatActivity {

    public static final int WEEKS_NUM = 12;  // Number of weeks for training
    public static final int SET_NUM = 3;
    public static final String[][] EXERCISES = new String[][]{
            {"Bench Press", "Skull Crushers", "Flys", "Incline Bench Press"},
            {"Chin-Ups", "Bicep Curls", "Bent Over Rows", "Lat Pulldown"},
            {},
            {"Squats", "Leg Extensions", "Leg Curls", "Calf Raises"},
            {"Shoulder Press", "Side Lateral Raise", "Upright Rows", "Seated Bent Over Flys"},
            {},
            {}
    };

    public static final String WEEK_PREFERENCES = "WEEK_PREFERENCES";
    SharedPreferences weekPreferences;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference userRef = db.collection("users").document(user.getUid());

    Gainer gainer;
    Boolean[] weekChecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        weekPreferences = getSharedPreferences(WEEK_PREFERENCES, Context.MODE_PRIVATE);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    gainer = documentSnapshot.toObject(Gainer.class);
                    weekChecks = gainer.getWeekChecks();
                } else {
                    createBlankDoc(user.getUid());
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot innerDocumentSnapshot) {
                            gainer = innerDocumentSnapshot.toObject(Gainer.class);
                            weekChecks = gainer.getWeekChecks();
                        }
                    });
                }
                addLayout(weekChecks);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("it failed", "Error getting document", e);
                    }
                });

    }

    private void addLayout(Boolean[] weekChecks) {

        CheckBox[] checkBoxes = new CheckBox[weekChecks.length];
        Button[] buttons = new Button[weekChecks.length];
        LinearLayout mLinearLayout = findViewById(R.id.week_layout);

        LayoutParams weekLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        LayoutParams checkBoxLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < weekChecks.length; i++) {

            LinearLayout weekLayout = new LinearLayout(this);
            weekLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Add checkbox for each week
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setGravity(Gravity.CENTER);
            checkBoxes[i].setScaleX((float) 1.3);
            checkBoxes[i].setScaleY((float) 1.3);
            weekLayout.addView(checkBoxes[i], checkBoxLayoutParams);

            // Set the checkbox to be checked or unchecked based on what was previously saved
            checkBoxes[i].setChecked(weekChecks[i]);

            // Add a button and set the text for each week
            buttons[i] = new Button(this);
            buttons[i].setTextSize(30);
            buttons[i].setGravity(Gravity.CENTER);
            String weekText = "WEEK " + String.valueOf(i + 1);
            buttons[i].setText(weekText);
            // Set a click listener that will start the Day activity and send the week's text
            buttons[i].setOnClickListener(view -> setDay(weekText));
            weekLayout.addView(buttons[i], buttonLayoutParams);

            // Do a checkbox listener if i > 1. This is because you need three checkboxes to run
            // this function (one before, one middle, and one after)
            if (i > 1) {
                checkBoxListener(i, checkBoxes[i - 1], checkBoxes[i - 2], checkBoxes[i], buttons[i]);
            }

            // If the previous checkbox wasn't checked, discheck the checkbox and buttons
            if (i != 0 && !checkBoxes[i - 1].isChecked()) {
                checkBoxes[i].setEnabled(false);
                buttons[i].setEnabled(false);
            }

            // Disable the previous checkbox if the checkbox is checked
            if (i != 0 && checkBoxes[i].isChecked())
                checkBoxes[i - 1].setEnabled(false);

            mLinearLayout.addView(weekLayout, weekLayoutParams);
        }

        // Add checkbox listener for the first week's checkbox
        checkBoxListener(checkBoxes[0], checkBoxes[1], buttons[1]);
        // Add a checkbox listener for the last week's checkbox
        checkBoxListener(checkBoxes[weekChecks.length - 1], checkBoxes[weekChecks.length - 2], weekChecks.length);
    }

    //    This is for all weeks that are not first or last
    private void checkBoxListener(final int weekNum, final CheckBox currentCheckBox,
                                  final CheckBox previousCheckBox, final CheckBox nextCheckBox,
                                  final Button nextButton) {

        currentCheckBox.setOnClickListener((View v) -> {
            // Enable the next checkbox and button if the current checkbox gets checked, disable if vice versa
            // Disable the last checkbox if current checkbox gets checked, disable if vice versa
            nextCheckBox.setEnabled(currentCheckBox.isChecked());
            nextButton.setEnabled(currentCheckBox.isChecked());
            previousCheckBox.setEnabled(!currentCheckBox.isChecked());

            // Save the state of the current checkbox
            db.collection("users").document(user.getUid())
                    .update("weeks.WEEK " + (weekNum) + ".checked", currentCheckBox.isChecked()
                    );
            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week" + Integer.toString(weekNum), currentCheckBox.isChecked());
            editor.apply();
        });
    }

    //    This is for the first week since it doesn't have a previous week
    private void checkBoxListener(final CheckBox currentCheckBox,
                                  final CheckBox nextCheckBox, final Button nextButton) {

        currentCheckBox.setOnClickListener((View v) -> {
            nextCheckBox.setEnabled(currentCheckBox.isChecked());
            nextButton.setEnabled(currentCheckBox.isChecked());

            db.collection("users").document(user.getUid())
                    .update("weeks.WEEK 1.checked", currentCheckBox.isChecked());
            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week1", currentCheckBox.isChecked());
            editor.apply();
        });
    }

    //    For the last week since it doesn't have a next week
    private void checkBoxListener(final CheckBox currentCheckBox, final CheckBox previousCheckBox, final int weeksLength) {

        currentCheckBox.setOnClickListener((View v) -> {
            previousCheckBox.setEnabled(!currentCheckBox.isChecked());

            db.collection("users").document(user.getUid())
                    .update("weeks.WEEK " + weeksLength + ".checked", currentCheckBox.isChecked());
            SharedPreferences.Editor editor = weekPreferences.edit();
            editor.putBoolean("checkbox_week" + weeksLength, currentCheckBox.isChecked());
            editor.apply();
        });
    }

    // Function to start the Day Activity
    private void setDay(final String week) {
        Intent day = new Intent(getApplicationContext(), DayActivity.class);
        day.putExtra(DayActivity.EXTRA_WEEK, week); // Send the week so the day activity knows what week was selected
        startActivity(day);
    }

    private void getWeekChecks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference userRef = db.collection("users");
        Query usersDataQuery = userRef.whereEqualTo("name", "Jason");

        usersDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Got it dawg", document.getId() + " => " + document.getData());
                    }
                } else {
                    System.out.println("query failed man");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_out_button) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(WeekActivity.this, "Logged out successfully",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    public void createBlankDoc(String userID) {

        HashMap<String, HashMap> setsMap = new HashMap<>();

        HashMap<String, Integer> setMap = new HashMap<>();
        setMap.put("weight", 0);
        setMap.put("reps", 0);

        for (int i = 1; i <= SET_NUM; i++) {
            setsMap.put("set " + i, setMap);
        }

        HashMap<String, HashMap> daysMap = new HashMap<>();

        for (int i = 0; i < EXERCISES.length; i++) {

            HashMap<String, HashMap> exercisesMap = new HashMap<>();
            for (int j = 0; j < EXERCISES[i].length; j++) {
                HashMap<String, Object> exerciseMap = new HashMap<>();
                exerciseMap.put("sets", setsMap);
                exercisesMap.put(EXERCISES[i][j], exerciseMap);
            }

            HashMap<String, Object> dayMap = new HashMap<>();
            dayMap.put("checked", false);
            dayMap.put("exercises", exercisesMap);

            daysMap.put("DAY " + (i + 1), dayMap);
        }

        HashMap<String, Object> weekMap = new HashMap<>();
        weekMap.put("checked", false);
        weekMap.put("days", daysMap);

        HashMap<String, HashMap> weeksMap = new HashMap<>();
        for (int i = 0; i < WEEKS_NUM; i++) {
            weeksMap.put("WEEK " + (i + 1), weekMap);
        }

        HashMap<String, HashMap> docMap = new HashMap<>();
        docMap.put("weeks", weeksMap);

        // Add a new document with the user's ID
        db.collection("users")
                .document(userID)
                .set(docMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure", "Error writing document", e);
                    }
                });
    }

}
