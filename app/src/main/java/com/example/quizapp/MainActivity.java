package com.example.quizapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totalQuestionTextView;
    TextView questionTextView;
    Button AnsA, AnsB, AnsC, AnsD;
    Button submitbtn;

    int score = 0;
    int totalQuestion = QuestionAnswer.question.length;
    int currentQuestion = 0;
    String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Show "Launching Quiz App" dialog with a 10-second delay
        showLaunchingDialog();

        // Initialize UI components after the delay
        new Handler().postDelayed(() -> {
            initializeUI();
        }, 3000); // 10-second delay
    }

    private void showLaunchingDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Launching Quiz App...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Dismiss the dialog after 10 seconds
        new Handler().postDelayed(progressDialog::dismiss, 3000);
    }

    private void initializeUI() {
        totalQuestionTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        AnsA = findViewById(R.id.ans_A);
        AnsB = findViewById(R.id.ans_B);
        AnsC = findViewById(R.id.ans_C);
        AnsD = findViewById(R.id.ans_D);
        submitbtn = findViewById(R.id.submit);

        AnsA.setOnClickListener(this);
        AnsB.setOnClickListener(this);
        AnsC.setOnClickListener(this);
        AnsD.setOnClickListener(this);
        submitbtn.setOnClickListener(this);

        // Set initial total questions
        totalQuestionTextView.setText("Total Questions : " + totalQuestion);

        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        AnsA.setBackgroundColor(Color.WHITE);
        AnsB.setBackgroundColor(Color.WHITE);
        AnsC.setBackgroundColor(Color.WHITE);
        AnsD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if (clickedButton.getId() == R.id.submit) {
            if (selectedAnswer.equals(QuestionAnswer.Answers[currentQuestion])) {
                score++;
            }
            currentQuestion++;
            // Update the total question number displayed
            totalQuestionTextView.setText("Remaining Question : " + (totalQuestion - currentQuestion));
            loadNewQuestion();
        } else {
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.CYAN);
        }
    }

    void loadNewQuestion() {
        if (currentQuestion == totalQuestion) {
            finishQuiz();
            return;
        }
        questionTextView.setText(QuestionAnswer.question[currentQuestion]);
        AnsA.setText(QuestionAnswer.options[currentQuestion][0]);
        AnsB.setText(QuestionAnswer.options[currentQuestion][1]);
        AnsC.setText(QuestionAnswer.options[currentQuestion][2]);
        AnsD.setText(QuestionAnswer.options[currentQuestion][3]);
    }

    void finishQuiz() {
        String pass = "";
        int iconResId;

        if (score > totalQuestion * 0.60) {
            pass = "Passed";
            iconResId = R.drawable.img; // Placeholder for cup icon
        } else {
            pass = "Failed";
            iconResId = R.drawable.sad; // Placeholder for sad face icon
        }

        new AlertDialog.Builder(this)
                .setTitle(pass)
                .setMessage("Score is " + score + " out of " + totalQuestion)
                .setIcon(iconResId)
                .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    void restartQuiz() {
        score = 0;
        currentQuestion = 0;
        // Reset the total question number displayed
        totalQuestionTextView.setText("Total Questions : " + totalQuestion);
        loadNewQuestion();
    }
}