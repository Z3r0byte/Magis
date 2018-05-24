package com.z3r0byte.magis.DetailActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Grade;

public class GradeDetails extends AppCompatActivity {

    private static final String TAG = "GradeDetailsActivity";

    Grade mGrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGrade = new Gson().fromJson(extras.getString("Grade"), Grade.class);
        } else {
            Log.e(TAG, "onCreate: Impossible to show details of a null Grade!", new IllegalArgumentException());
            Toast.makeText(GradeDetails.this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        if (mGrade.description != null) {
            toolbar.setTitle(mGrade.description);
        } else {
            toolbar.setTitle(getString(R.string.msg_details));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView grade = (TextView) findViewById(R.id.cijfer);
        TextView gradeCircle = (TextView) findViewById(R.id.grade);
        TextView test = (TextView) findViewById(R.id.toets);
        TextView subject = (TextView) findViewById(R.id.vak);
        TextView dateSubmitted = (TextView) findViewById(R.id.datum);
        TextView isPassGrade = (TextView) findViewById(R.id.isVoldoende);
        TextView wage = (TextView) findViewById(R.id.weging);
        TextView testDate = (TextView) findViewById(R.id.toetsDatum);

        if (mGrade.grade != null) {
            grade.setText(mGrade.grade);
            gradeCircle.setText(mGrade.grade);
        }
        if (mGrade.description != null) {
            test.setText(mGrade.description);
        }
        if (mGrade.subject.name != null) {
            subject.setText(mGrade.subject.name);
        }
        if (mGrade.filledInDate != null) {
            dateSubmitted.setText(DateUtils.formatDate(mGrade.filledInDate, "dd MMMM yyyy 'om' HH:mm"));
        }
        if (!mGrade.isSufficient) {
            isPassGrade.setText(R.string.msg_no);
            isPassGrade.setTextColor(getResources().getColor(R.color.md_red_500));
            grade.setTextColor(getResources().getColor(R.color.md_red_500));
        } else {
            isPassGrade.setText(R.string.msg_yes);
        }
        if (mGrade.wage != null) {
            wage.setText(mGrade.wage.toString());
        }
        if (mGrade.testDate != null) {
            testDate.setText(DateUtils.formatDate(mGrade.testDate, "dd MMMM yyyy 'om' HH:mm"));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

