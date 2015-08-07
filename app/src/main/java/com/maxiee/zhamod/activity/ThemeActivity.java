package com.maxiee.zhamod.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.maxiee.zhamod.R;

/**
 * Created by maxiee on 15-8-7.
 */
public class ThemeActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int[] COLORS = {
            R.color.red,
            R.color.pink,
            R.color.purple,
            R.color.indigo,
            R.color.blue,
            R.color.teal,
            R.color.lime,
            R.color.yellow,
            R.color.orange,
            R.color.gray
    };

    private static final int ROWS = 3;
    private static final int COLUMNS = 4;

    private GridLayout mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        mGrid = (GridLayout) findViewById(R.id.grid);

        generateColoredButtons();
    }

    private void generateColoredButtons() {
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLUMNS; j++) {
                int count = COLUMNS * i + j;
                if (count > COLORS.length - 1) {
                    return;
                }
                Button b = new Button(this);
                b.setBackgroundResource(R.drawable.round_button);
                b.setBackgroundColor(getResources().getColor(COLORS[count]));
                b.setOnClickListener(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                b.setLayoutParams(params);
                mGrid.addView(b);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
