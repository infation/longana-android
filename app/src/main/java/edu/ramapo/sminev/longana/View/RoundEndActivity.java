package edu.ramapo.sminev.longana.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.ramapo.sminev.longana.Model.Tournament;
import edu.ramapo.sminev.longana.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RoundEndActivity extends AppCompatActivity {

    private Button save, next;
    private int computerRScore, humanRScore, computerTScore, humanTScore, tournamentMaxScore, engine, roundNum;
    private TextView compRScoreView, humanRScoreView, compTScoreView, humanTScoreView, winnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_end);
        Bundle bundle = getIntent().getExtras();
        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(saveButtonListener);
        next = (Button) findViewById(R.id.next_button);
        next.setOnClickListener(nextButtonListener);
        compRScoreView = (TextView) findViewById(R.id.comp_round_score);
        humanRScoreView = (TextView) findViewById(R.id.human_round_score);
        compTScoreView = (TextView) findViewById(R.id.comp_tour_score);
        humanTScoreView = (TextView) findViewById(R.id.human_tour_score);
        winnerView = (TextView) findViewById(R.id.winner);
        computerRScore = bundle.getInt("comp_round_score", 0);
        humanRScore = bundle.getInt("human_round_score", 0);
        computerTScore = bundle.getInt("comp_tour_score", 0);
        humanTScore = bundle.getInt("human_tour_score", 0);
        tournamentMaxScore = bundle.getInt("tournament_max", 0);
        roundNum = bundle.getInt("round_num", 1);
        engine = bundle.getInt("engine", 6);
        updateScores();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

    }

    public String decideWinner(){
        if(computerRScore > humanRScore){
            return "Computer";
        }
        else if (computerRScore < humanRScore){
            return "Human";
        }
        else{
            return "Draw";
        }
    }

    public void checkTournamentEnding(){
        Intent endRound = new Intent(RoundEndActivity.this, EndTournamentActivity.class);
        endRound.putExtra("comp_tour_score", computerTScore);
        endRound.putExtra("human_tour_score", humanTScore);
        endRound.putExtra("winner", decideWinner());
        if(humanTScore >= tournamentMaxScore){
            startActivity(endRound);
            finish();
        }
        else if(computerTScore >= tournamentMaxScore){
            startActivity(endRound);
            finish();
        }
    }

    View.OnClickListener nextButtonListener = (new View.OnClickListener() {
        public void onClick(View view) {
            Intent endRound = new Intent(RoundEndActivity.this, RoundActivity.class);
            endRound.putExtra("comp_tour_score", computerTScore);
            endRound.putExtra("human_tour_score", humanTScore);
            endRound.putExtra("tournament_max", tournamentMaxScore);
            endRound.putExtra("round_num", roundNum+1);
            endRound.putExtra("engine",engine);
            endRound.putExtra("load", false);
            startActivity(endRound);
            finish();
        }
    });


    View.OnClickListener saveButtonListener = (new View.OnClickListener() {
        public void onClick(View view) {

        }
    });

    public void updateScores(){
        winnerView.setText("Winner: " + decideWinner());
        checkTournamentEnding();
        compRScoreView.setText("Computer Round Score: " + computerRScore);
        humanRScoreView.setText("Human Round Score: " + humanRScore);
        compTScoreView.setText("Computer Tournament Score: " + computerTScore);
        humanTScoreView.setText("Human Tournament Score: " + humanTScore);

    }

}
