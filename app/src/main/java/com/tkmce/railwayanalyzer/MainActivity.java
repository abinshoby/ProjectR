package com.tkmce.railwayanalyzer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
//jdbc:mysql://116.68.64.55/projectR
    private static final String url = "jdbc:mysql://138.197.1.111/projectR";
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
        private GraphView mGraph;
        private LineGraphSeries<DataPoint> series;
        int rowCount=0;
        String time[];
        Float speed[];
        Float distance[];
        Float driver[];
        int count;

        @Override
        protected Void doInBackground(Void... voids) {
            mGraph =(GraphView)findViewById(R.id.all);

            int size = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url,user,pass);
                Statement stmt = con.createStatement();

                ResultSet rs1 = stmt.executeQuery("Select count(*) From data ;");
                rs1.next();
                rowCount = rs1.getInt(1);
                ResultSet  rs=stmt.executeQuery("select  * from data limit 30;");


                time = new String[30];
                speed = new Float[30];
                distance = new Float[30];
                driver = new Float[30];//rs.getrow()



               // viewport.setYAxisBoundsManual(true);
               // viewport.setMinY(0);
               // viewport.setMaxY(3000);


               rs.first();
 count=0;
                while (!rs.isAfterLast() ) {
            	/* Store Values */
                    //time[count] = rs.getString("time");
                    speed[count] = Float.valueOf(rs.getString("speed"));


                    distance[count] = Float.valueOf(rs.getString("distance"));
                    //series.appendData(new DataPoint(speed[count], distance[count]), true, 30000);
                    driver[count] = Float.valueOf(rs.getString("driver_id"));
                    rs.next();
            	/* Increment count */
                    count++;
                    //System.out.println(String.valueOf(rs.getString("time")));
                }
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
            series = new LineGraphSeries<DataPoint>();
            mGraph.addSeries(series);
            Viewport viewport = mGraph.getViewport();
            mGraph.getViewport().setScrollable(true); // enables horizontal scrolling
            mGraph.getViewport().setScrollableY(true);
            viewport.setYAxisBoundsManual(true);
            viewport.setXAxisBoundsManual(true);
          //viewport.setScalable(true);
            viewport.setMinY(30);
            viewport.setMaxY(50);
            viewport.setMaxX(150);
            viewport.setMinX(100);
            GridLabelRenderer gridLabel = mGraph.getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Speed");
            gridLabel.setVerticalAxisTitle("Distance");

          //  mGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
          //  mGraph.getViewport().setScalableY(true);

           // viewport.setScrollable(true);
           // viewport.setScrollableY(true);
            for(int i=0;i<30;i++){
                series.appendData(new DataPoint(speed[i], distance[i]), true, 30000);
            }


        }
    }

}
