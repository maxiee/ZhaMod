package com.maxiee.zhamod.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.maxiee.zhamod.Constants;
import com.maxiee.zhamod.R;

/**
 * Created by maxiee on 15-8-7.
 */
public class ThemeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int ROWS = 4;
    private static final int COLUMNS = 3;

    private Toolbar mToolbar;
    private GridLayout mGrid;
    private View mCurrentSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGrid = (GridLayout) findViewById(R.id.grid);
        mCurrentSelect = (View) findViewById(R.id.current_select);

        generateColoredButtons();

        int spCurrent = getSharedPreferences(Constants.SP_FILE, Context.MODE_WORLD_READABLE).getInt(Constants.SP_BACKGROUND, 0);
        mCurrentSelect.setBackgroundColor(getResources().getColor(Constants.COLORS[spCurrent]));
    }

    private void generateColoredButtons() {
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLUMNS; j++) {
                int count = COLUMNS * i + j;
                if (count > Constants.COLORS.length - 1) {
                    return;
                }
                Button b = new Button(this);
                b.setBackgroundResource(R.drawable.round_button);
                b.setBackgroundColor(getResources().getColor(Constants.COLORS[count]));
                b.setTag(count);
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
        int count = (int) v.getTag();
        mCurrentSelect.setBackgroundColor(getResources().getColor(Constants.COLORS[count]));
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_FILE, Context.MODE_WORLD_READABLE).edit();
        editor.putInt(Constants.SP_BACKGROUND, count);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
