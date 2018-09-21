package com.example.saurav.podium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileForm extends AppCompatActivity {


    @BindView(R.id.TVName)
    EditText TVName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.Bsubmit)
    public void onViewClicked() {
        Intent replyIntent = new Intent();
        if (!TextUtils.isEmpty(TVName.getText())) {
            setResult(RESULT_OK, replyIntent);
            Toast.makeText(this, "Form filled", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setResult(RESULT_CANCELED, replyIntent);
            Toast.makeText(this, "Please ! Fill the form first to enjoy our feature", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Please ! Fill the form first to enjoy our feature", Toast.LENGTH_SHORT).show();
    }
}
