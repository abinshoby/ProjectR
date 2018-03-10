package com.tkmce.railwayanalyzer;

import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
//jdbc:mysql://116.68.64.55/projectR
    private static final String url = "jdbc:mysql://138.197.1.111/projectR";
    private static final String user = "projectR";
    private static final String pass = "5TDF523yLU7oLJEX";
    private GraphView mGraph;
    private LineGraphSeries<DataPoint> series;
    TextView out;
    TextView tv;
    Connection con = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//out=(TextView)findViewById(R.id.out) ;

        tv = findViewById(R.id.texttrail);


        new connTask().execute();
    }

    private void connect() {

    }

    public void onselg(View view) {

    }



    private class connTask extends AsyncTask<Void,Void,Void>{


        int rowCount=0;
        String time[];
        String speed[];
        String distance[];
        String driver[];
        int count;






        @Override
        protected Void doInBackground(Void... voids) {
            mGraph =(GraphView)findViewById(R.id.all);

            int size = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url,user,pass);
                Statement stmt = con.createStatement();

             //   ResultSet rs1 = stmt.executeQuery("Select count(*) From data ;");
               // rs1.next();
               // rowCount = rs1.getInt(1);
                ResultSet  rs=stmt.executeQuery("select  * from data order by distance ;");
                rs.last();
rowCount=rs.getRow();
                time = new String[rowCount];
                speed = new String[rowCount];
                distance = new String[rowCount];
                driver = new String[rowCount];//rs.getrow()



               // viewport.setYAxisBoundsManual(true);
               // viewport.setMinY(0);
               // viewport.setMaxY(3000);


               rs.beforeFirst();
 count=0;
                while (rs.next() ) {
            	/* Store Values */
                    //time[count] = rs.getString("time");
                   // count++;

                    speed[count] = rs.getString("speed");


                    distance[count] = rs.getString("distance");
                    //series.appendData(new DataPoint(speed[count], distance[count]), true, 30000);
                   driver[count] = rs.getString("driver_id");
                   // Log.i("distance: .",distance[count].toString() );
                  //  Log.i("speed: .",speed[count].toString() );


            	/* Increment count */
                    count++;
                    //System.out.println(String.valueOf(rs.getString("time")));
                }
            } catch (ClassNotFoundException e) {
               // Toast.makeText(MainActivity.this, "Class not found", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
              //  Toast.makeText(MainActivity.this, "SQL error" + e.getMessage(), Toast.LENGTH_SHORT).show();
               // tv.setText(e.getMessage());
            }
            return null;
        }
        public int find(Double d,Double s){
            int in=-1;
            Double cumd=0.0;
            for(int i=0;i<count;i++){

                if(cumd==d && Double.parseDouble(speed[i])==s){
                    in=i;
                    break;
                }
                cumd+=Double.valueOf(distance[i]);
            }
            return in;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            tv.setText("Success : "+ count + "  rows ");
            series = new LineGraphSeries<DataPoint>();
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(MainActivity.this ,"Series: On Data Point clicked"  + dataPoint, Toast.LENGTH_SHORT).show();
                        int in=find(dataPoint.getX(),dataPoint.getY());
                        if(in!=-1)
                            Toast.makeText(getApplicationContext(),"Driver id:"+driver[in],Toast.LENGTH_LONG).show();
                }
            });
             series .setDrawDataPoints(true);
            series.setDataPointsRadius(15f);
            mGraph.addSeries(series);
            Viewport viewport = mGraph.getViewport();
            mGraph.getViewport().setScrollable(true); // enables horizontal scrolling
            mGraph.getViewport().setScrollableY(true);
            viewport.setYAxisBoundsManual(true);
           viewport.setXAxisBoundsManual(true);



            viewport.setScalable(true);
           // viewport.setScalableY(true);
          //  viewport.setMinY(20);
            viewport.setMaxY(200);
            viewport.setMaxX(100);
            viewport.setMinX(0);
            GridLabelRenderer gridLabel = mGraph.getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Distance");
            gridLabel.setVerticalAxisTitle("Speed");

          //  mGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
          //  mGraph.getViewport().setScalableY(true);

           // viewport.setScrollable(true);
           // viewport.setScrollableY(true);
            //Toast.makeText(getApplicationContext(), count, Toast.LENGTH_SHORT).show();
          //  out.setText(Integer.toString(count));
            Double cumd=0.0;
            for(int i=0;i<count;i++){
                cumd+=Double.valueOf(distance[i]);
                series.appendData(new DataPoint( cumd,Double.valueOf(speed[i])) ,true, 300000);

            }


        }
    }

}
