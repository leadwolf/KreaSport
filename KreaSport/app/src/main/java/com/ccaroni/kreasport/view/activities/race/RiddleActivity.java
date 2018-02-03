package com.ccaroni.kreasport.view.activities.race;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.dto.Riddle;
import com.ccaroni.kreasport.databinding.ActivityRiddleBinding;
import com.ccaroni.kreasport.view.activities.old.ExploreActivity;
import com.google.gson.Gson;

public class RiddleActivity extends AppCompatActivity {

    private static final String LOG = RiddleActivity.class.getSimpleName();
    public static final String KEY_USER_ANSWER = "com.ccaroni.kreasport." + RiddleActivity.class.getSimpleName() + "key.user_answer_index";


    private ActivityRiddleBinding binding;

    private Riddle riddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_riddle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String riddleJson = getIntent().getStringExtra(ExploreActivity.KEY_RIDDLE);
        riddle = new Gson().fromJson(riddleJson, Riddle.class);

        if (riddle == null) {
            Log.d(LOG, "Could not get riddle from intent");
            setResult(RESULT_CANCELED);
            finish();
        }

        setBindings();
    }

    private void setBindings() {
        binding.contentRiddle.tvRiddleQuestion.setText(riddle.getQuestion());

        binding.contentRiddle.gridViewAnswers.setAdapter(new GridAdapter());
        binding.contentRiddle.gridViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(RiddleActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return riddle.getAnswers().size();
        }

        @Override
        public Object getItem(int position) {
            return riddle.getAnswers().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Button button = new Button(RiddleActivity.this);
            String answer = riddle.getAnswers().get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG, "clicked on position: " + position);
                    if (position == riddle.getAnswerIndex()) {
                        Log.d(LOG, "answer was correct");
                        Toast.makeText(RiddleActivity.this, "Correct", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra(KEY_USER_ANSWER, position);

                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    } else {
                        Log.d(LOG, "answer was INcorrect");
                        Toast.makeText(RiddleActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button.setText(answer);

            return button;
        }
    }

}
