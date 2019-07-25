package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CreateAssemblyActivity extends AppCompatActivity {

    /**
     * Create intent and launch this activity
     * @param context context of parent activity
     */
    public static void launchActivityFrom(Context context) {
        Intent intent = new Intent(context, CreateAssemblyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assembly);
    }
}
