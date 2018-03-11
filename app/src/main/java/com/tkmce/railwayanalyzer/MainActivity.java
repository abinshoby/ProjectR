package com.tkmce.railwayanalyzer;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


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
/*to get time elapsed SELECT( date(d.time)-date('2018-02-25 23:30:30'))*24+(hour(d.time)-hour('2018-02-25 23:30:30'))+(minute(d.time)-minute('2018-02-25 23:30:30'))*(1/60) from data as d;*/
public class MainActivity extends AppCompatActivity {
//jdbc:mysql://116.68.64.55/projectR
    private static final String url = "jdbc:mysql://138.197.1.111/projectR";
    private static final String user = "projectR";
    private static final String pass = "5TDF523yLU7oLJEX";
    private GraphView mGraph;
    Thread t;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private LineGraphSeries<DataPoint> series;
    TextView out;
   // TextView tv;
    Connection con = null;
    ProgressDialog dialog;
android.support.v7.widget.Toolbar toolbar;


    //Setting the actionbarToggle to drawer layout


    //calling sync state is necessary or else your hamburger icon wont show up
    Handler mHandler;
    View navHeader;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=(NavigationView)findViewById(R.id.nav_view) ;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
       navHeader = navigationView.getHeaderView(0);
         mHandler = new Handler();

        loadNavHeader();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
               // Toast.makeText(getApplicationContext(),"Touched",Toast.LENGTH_LONG).show();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.dist_speed:
                        drawer.closeDrawers();
                        loading("Fetching data please wait...");
                        dialog.show();
                       // tv.setText("Loading");

                       // Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_LONG).show();
                        new connTask().execute();
                        //  navItemIndex = 0;
                        //   CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.dis_time:
                        //  navItemIndex = 1;
                        //   CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.settings:
                        // navItemIndex = 2;
                        //  CURRENT_TAG = TAG_MOVIES;
                        break;
                    //  case R.id.nav_notifications:
                    //   navItemIndex = 3;
                    //   CURRENT_TAG = TAG_NOTIFICATIONS;
                    //    break;
                    //  case R.id.nav_settings:
                    //   navItemIndex = 4;
                    //   CURRENT_TAG = TAG_SETTINGS;
                    //     break;
                    // case R.id.nav_about_us:
                    // launch new intent instead of loading fragment
                    // startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                    //     drawer.closeDrawers();
                    //    return true;
                    // case R.id.nav_privacy_policy:
                    // launch new intent instead of loading fragment
                    // startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                    //   drawer.closeDrawers();
                    //  return true;
                    default:
                        //  navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                // loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                navigationView.bringToFront();
                navigationView.requestLayout();
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


//out=(TextView)findViewById(R.id.out) ;

      //  tv = findViewById(R.id.texttrail);


       // new connTask().execute();
    }
private  void loading(String msg){
     dialog = new ProgressDialog(this); // this = YourActivity
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setMessage(msg);
    dialog.setIndeterminate(true);
    dialog.setCanceledOnTouchOutside(false);
    //dialog.show();
}
    private void connect() {

    }

    public void onselds() {
     //   tv.setText("Loading");
        Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_LONG).show();
        new connTask().execute();

    }
    private void loadNavHeader() {
        // name, website
       // txtName.setText("Ravi Tamada");
        //txtWebsite.setText("www.androidhive.info");

        // loading header background image
        //Glide.with(this).load(urlNavHeaderBg)
          //      .crossFade()
            //    .diskCacheStrategy(DiskCacheStrategy.ALL)
              //  .into(imgNavHeaderBg);

        // Loading profile image
        //Glide.with(this).load(urlProfileImg)
          //      .crossFade()
            //    .thumbnail(0.5f)
              //  .bitmapTransform(new CircleTransform(this))
               // .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.into(imgProfile);

        // showing dot next to notifications label
      //  navigationView.getMenu().getItem(0).setOnMenuItemClickListener();
    /*    navigationView.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
               // YourActivity.this.someFunctionInYourActivity();
                onselds();
                return true;
            }
        });*/
    }



    private void selectNavMenu() {
      //  navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
   /* public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_view, menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    // private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu

    //}



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



            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url,user,pass);
                Statement stmt = con.createStatement();


                ResultSet  rs=stmt.executeQuery("select  * from data order by distance ;");
                rs.last();
rowCount=rs.getRow();
                time = new String[rowCount];
                speed = new String[rowCount];
                distance = new String[rowCount];
                driver = new String[rowCount];//rs.getrow()

               rs.beforeFirst();
                count=0;
                while (rs.next() ) {
            	/* Store Values */
                    //time[count] = rs.getString("time");

                    speed[count] = rs.getString("speed");
                    distance[count] = rs.getString("distance");
                   driver[count] = rs.getString("driver_id");
            	/* Increment count */
                    count++;

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
                cumd+=Double.valueOf(distance[i]);
                if(Objects.equals(cumd, d)){
                    in=i;
                    break;
                }

            }
            return in;

        }

class loadthread extends Thread{
            public void run(){
                loading("plotting...");

            }

}
        @Override

        protected void onPostExecute(Void aVoid) {
          //  tv.setText("Success : "+ count + "  rows ");


            
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

            dialog.dismiss();
            mGraph.setVisibility(View.VISIBLE);



        }
    }

}
