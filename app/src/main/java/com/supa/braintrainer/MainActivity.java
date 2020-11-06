package com.supa.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private enum Screen {
        startScreen,
        gameScreen,
        endScreen
    }
    private enum Mode {
        easy,
        normal,
        hard
    }

    private Screen screen;
    private Mode mode;

    private TextView timerTextView;
    private TextView scoreTextView;
    private TextView resultTextView;
    private TextView challengeTextView;
    private TextView answer1TextView;
    private TextView answer2TextView;
    private TextView answer3TextView;
    private TextView answer4TextView;
    private TextView salutationsTextView;
    private TextView endScoreTextView;
    private Button startButton;
    private Button restartButton;
    private ConstraintLayout startScreen;
    private ConstraintLayout gameScreen;
    private ConstraintLayout endScreen;

    private CountDownTimer clock;

    Random random = new Random();

    private int numberLimit = 21;
    private int timerLimit = 30;
    private int addNumOne = 0;
    private int addNumTwo = 0;
    private int answer = 0;
    private double correct = 0;
    private double wrong = 0;
    private double total = 0;

    private String equation = "";

    private ArrayList<Integer> randomAnswers;
    private ArrayList<TextView> gameTiles;

    DecimalFormat df = new DecimalFormat("0.00");

    public void setTimer() {
        clock = new CountDownTimer(timerLimit*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                gameScreen.setVisibility(View.GONE);
                endScreen.setVisibility(View.VISIBLE);
                screen = Screen.endScreen;

                double percent = (total > 0 ? (correct / total) * 100 : 0);

                if (percent >= 70) {
                    salutationsTextView.setText("Great Job!");
                } else {
                    salutationsTextView.setText("Try Again");
                }

                endScoreTextView.setText(df.format(percent) + "%");
            }
        }.start();
    }

    public void newEquation() {
        addNumOne = random.nextInt(numberLimit);
        addNumTwo = random.nextInt(numberLimit);
        equation = addNumOne + " + " + addNumTwo;
        answer = addNumOne + addNumTwo;
        challengeTextView.setText(equation);
        generateNewNumbers();
        setupGameTiles();
    }

    public void generateNewNumbers() {
        if(!randomAnswers.isEmpty()) {
            randomAnswers.clear();
        }
        randomAnswers.add(answer);
        int n = 0;
        for(int i = 0; i < 3; i++) {
            n = random.nextInt(numberLimit*2);
            if (randomAnswers.contains(n)) {
                while (randomAnswers.contains(n)) {
                    n = random.nextInt(numberLimit*2);
                }
            }
            randomAnswers.add(n);
        }

        setupGameTiles();
    }

    public void setupGameTiles() {
        Collections.shuffle(randomAnswers);
        Log.i("Random Answers", randomAnswers.toString());
        for (int i = 0; i < gameTiles.size(); i++) {
            gameTiles.get(i).setText(Integer.toString(randomAnswers.get(i)));
        }
    }

    public void checkAnswer(View view) {
        total++;
        TextView guessClicked = (TextView) view;
        int guessNumber = Integer.parseInt(guessClicked.getText().toString());
        if(guessNumber == answer) {
            correct++;
            resultTextView.setText("Correct!");
        } else {
            wrong++;
            resultTextView.setText("Wrong :(");
        }
        updateScore();
        newEquation();
        generateNewNumbers();
    }

    public void updateScore() {
        scoreTextView.setText((int) correct + "/" + (int) total);
    }

    public void startGame(View view) {
        switch (screen) {
            case startScreen:
                startScreen.setVisibility(View.GONE);
                gameScreen.setVisibility(View.VISIBLE);
            case endScreen:
                correct = 0;
                wrong = 0;
                total = 0;
                updateScore();
                resultTextView.setText("");
                endScreen.setVisibility(View.GONE);
                gameScreen.setVisibility(View.VISIBLE);
        }
        newEquation();
        generateNewNumbers();
        setupGameTiles();
        setTimer();
        screen = Screen.gameScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timer);
        scoreTextView = (TextView) findViewById(R.id.score);
        resultTextView = (TextView) findViewById(R.id.result);
        challengeTextView = (TextView) findViewById(R.id.challenge);
        answer1TextView = (TextView) findViewById(R.id.answer1);
        answer2TextView = (TextView) findViewById(R.id.answer2);
        answer3TextView = (TextView) findViewById(R.id.answer3);
        answer4TextView = (TextView) findViewById(R.id.answer4);
        salutationsTextView = (TextView) findViewById(R.id.salutations);
        endScoreTextView  = (TextView) findViewById(R.id.endScore);
        startButton = (Button) findViewById(R.id.startButton);
        restartButton = (Button) findViewById(R.id.playAgain);
        startScreen = (ConstraintLayout) findViewById(R.id.startScreen);
        gameScreen = (ConstraintLayout) findViewById(R.id.gameScreen);
        endScreen = (ConstraintLayout) findViewById(R.id.endScreen);

        startScreen.setVisibility(View.VISIBLE);
        gameScreen.setVisibility(View.GONE);
        endScreen.setVisibility(View.GONE);

        screen = Screen.startScreen;

        resultTextView.setText("");

        randomAnswers = new ArrayList<Integer>();
        gameTiles = new ArrayList<TextView>(){{
            add(answer1TextView);
            add(answer2TextView);
            add(answer3TextView);
            add(answer4TextView);
        }};

    }
}