package com.tkmce.railwayanalyzer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static final String url = "jdbc:mysql://116.68.64.55/projectR";
    private static final String user = "projectR";
    private static final String pass = "5TDF523yLU7oLJEX";
    TextView tv;
    Connection con = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.texttrail);
        new connTask().execute();
    }

    private void connect() {

    }

    private class connTask extends AsyncTask<Void,Void,Void>{
        int rowCount=0;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url,user,pass);
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("Select count(*) From data ;");
                rs.next();
                rowCount = rs.getInt(1);
            } catch (ClassNotFoundException e) {
                Toast.makeText(MainActivity.this, "Class not found", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(MainActivity.this, "SQL error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                tv.setText(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tv.setText("Success : "+ rowCount + "  rows ");
        }
    }

}
