package com.example.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    myDBHelper myDBHelper;
    EditText edtTitle, edtContext, edtTitleResult, edtContextResult;
    Button btnPostInsert, btnPostSelect;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("문의사항 게시판");


        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContext = (EditText) findViewById(R.id.edtContext);
        edtTitleResult = (EditText) findViewById(R.id.edtTitleResult);
        edtContextResult = (EditText) findViewById(R.id.edtContextResult);
        btnPostInsert = (Button) findViewById(R.id.btnPostInsert);
        btnPostSelect = (Button) findViewById(R.id.btnPostSelect);

        myDBHelper = new myDBHelper(this);
        btnPostInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO postTBL VALUES ('" + edtTitle.getText().toString()
                 + "', " + edtContext.getText().toString() + ");");
                sqlDB.close();
            }
        });

        btnPostSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT title FROM postTBL;", null);
                String strTitle = "제목" + "\r\n" + "\r\n";

                while (cursor.moveToNext()) {
                    strTitle += cursor.getString(0) + "\r";
                }
                edtTitleResult.setText(strTitle);
                cursor.close();
                sqlDB.close();
            }
        });

    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "postDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE postTBL (title CHAR(20), context CHAR(200))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS postTBL");
            onCreate(db);
        }
    }

}