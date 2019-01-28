package com.buinvent.jurassic_gains2;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


// Activity for the screen that gives users the ability to select the week they're training
public class WeekActivity extends AppCompatActivity {

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
                    addLayout(weekChecks);
                }

                // Add routine button
                Button routineButton = new Button(getApplicationContext());
                routineButton.setText("CREATE ROUTINE");
                routineButton.setTextSize(30);
                routineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), RoutineActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
                LinearLayout mLinearLayout = findViewById(R.id.week_layout);
                mLinearLayout.addView(routineButton);
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
            final int weekNum = i + 1;
            // Set a click listener that will start the Day activity and send the week's text
            buttons[i].setOnClickListener(view -> setDay(weekNum));
            weekLayout.addView(buttons[i], buttonLayoutParams);

            // Do a checkbox listener if i > 1. This is because you need three checkboxes to run
            // this function (one before, one middle, and one after)
            if (i > 1) {
                checkBoxListener(i, checkBoxes[i - 1], checkBoxes[i - 2], checkBoxes[i], buttons[i]);
            }

            // If the previous checkbox wasn't checked, uncheck the checkbox and buttons
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
        if (checkBoxes.length > 1) {
            checkBoxListener(checkBoxes[0], checkBoxes[1], buttons[1]);
            // Add a checkbox listener for the last week's checkbox
            checkBoxListener(checkBoxes[weekChecks.length - 1], checkBoxes[weekChecks.length - 2], weekChecks.length);
        }
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
    private void setDay(final int weekNum) {
        Intent day = new Intent(getApplicationContext(), DayActivity.class);
        day.putExtra(DayActivity.EXTRA_WEEK, weekNum); // Send the week so the day activity knows what week was selected
        day.putExtra(DayActivity.EXTRA_GAINER, gainer);
        startActivity(day);
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

    // If user hits back button on bottom (otherwise will go to blank screen)
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AuthActivity.class));
        super.onRestart();
    }

}
