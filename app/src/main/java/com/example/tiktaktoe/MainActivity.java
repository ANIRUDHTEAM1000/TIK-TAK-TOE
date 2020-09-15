package com.example.tiktaktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {
    int[] positions={-1,-1,-1,-1,-1,-1,-1,-1,-1};
    TextView tx;
    List<Integer> available;
    int[][] winningPos={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    View parent;
    List<Integer> temp,temp2;
    List<PointsAndScores> rootsChildrenScores;
    boolean GAMEOVER=false;
    ImageView b1,b2,b3,b4,b5,b6,b7,b8,b9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent=findViewById(R.id.parent);
        tx=findViewById(R.id.status);
        b1=findViewById(R.id.image0);
        b2=findViewById(R.id.image1);
        b3=findViewById(R.id.image2);
        b4=findViewById(R.id.image3);
        b5=findViewById(R.id.image4);
        b6=findViewById(R.id.image5);
        b7=findViewById(R.id.image6);
        b8=findViewById(R.id.image7);
        b9=findViewById(R.id.image8);
        reset();
    }
    void reset()
    {
        for(int i=0;i<9;i++)
        {
            positions[i]=-1;
        }
        b1.setImageResource(R.drawable.white);
        b2.setImageResource(R.drawable.white);
        b3.setImageResource(R.drawable.white);
        b4.setImageResource(R.drawable.white);
        b5.setImageResource(R.drawable.white);
        b6.setImageResource(R.drawable.white);
        b7.setImageResource(R.drawable.white);
        b8.setImageResource(R.drawable.white);
        b9.setImageResource(R.drawable.white);
        tx.setText("");
        GAMEOVER=false;
    }
    int is_game_over()
    {
        int won=-1;
        for(int[] winningPon : winningPos)
        {
            if( positions[winningPon[0]]==positions[winningPon[1]] && positions[winningPon[1]]==positions[winningPon[2]]  && positions[winningPon[0]]!=-1)
            {
                won=positions[winningPon[0]];
                return won;
            }
        }
//        if(available.isEmpty())
//        {
//            return 2;
//        }
        return won;
    }
//    boolean hastie()
//    {
//        if(is_game_over()==2)
//        {
//            return true;
//        }else
//        {
//            return false;
//        }
//    }
    boolean hasXwon()
    {
        if(is_game_over()==1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    boolean hasOwon()
    {
        if(is_game_over()==0)
        {
            return true;
        }
        else
        {
           return false;
        }
    }
    List<Integer> getAvailableStates() {
        available=new ArrayList<Integer>();
        if(!available.isEmpty())
            available.clear();
        for (int i = 0; i < 9; i++)
        {
            if(positions[i]==-1)
            {
                available.add(i);
            }
        }
        return available;
    }
    public void callMinimax(int depth, int turn){
      rootsChildrenScores = new ArrayList<PointsAndScores>();
        minimax(depth, turn);
      int best=returnBestMove();
      ImageView img = parent.findViewWithTag(""+best);
      img.setImageResource(R.drawable.x);
        positions[best]=1;
        temp2=getAvailableStates();
        int w=is_game_over();

        if(w==1)
        {
           // X has won
            tx.setText("X has won");
            GAMEOVER=true;
        }
        else if(temp2.isEmpty())
        {
            //its a draw
            tx.setText(" its a tie ");
            GAMEOVER=true;
        }
    }
    public int minimax(int depth, int turn) {

        if (hasXwon()) return +2;
        if (hasOwon()) return -1;
        List<Integer> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0;

        List<Integer> scores = new ArrayList<Integer>();

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Integer point = pointsAvailable.get(i);

            if (turn == 1) { //X's turn select the highest from below minimax() call
                positions[point]=1;
                int currentScore = minimax(depth + 1, 0);
                scores.add(currentScore);

                if (depth == 0)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));

            } else if (turn == 0) {//O's turn select the lowest from below minimax() call
                positions[point]=0;
                scores.add(minimax(depth + 1, 1));
            }
            positions[point]=-1; //Reset this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }
    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
     public void clicked(View v)
     {
         if(GAMEOVER)
         {
             reset();
         }
         else
             {
             ImageView img = (ImageView) v;
             int i = Integer.parseInt(img.getTag().toString());
             if (positions[i] == -1) {
                 img.setImageResource(R.drawable.o);
                 positions[i] = 0;
                 int w = is_game_over();
                 temp = getAvailableStates();
                 if (w == -1 && !temp.isEmpty()) {
                     callMinimax(0, 1);
                 } else {
//              some thing has won;
                     if (w == 0) {
//                    O has won
                         tx.setText("O has won");
                         GAMEOVER = true;
                     } else if (w == 1) {
//                    X has won
                         tx.setText("X has won");
                         GAMEOVER = true;
                     } else {
//                    its a tie
                         tx.setText(" its a tie ");
                         GAMEOVER = true;
                     }
                 }
             }
         }
     }
    public int returnBestMove() {
        int MAX = -100000;
        int best = -1;
        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point;
    }
}
class PointsAndScores {
    int score;
    int point;

    PointsAndScores(int score, int point) {
        this.score = score;
        this.point = point;
    }
}