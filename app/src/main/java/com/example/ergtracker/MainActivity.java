package com.example.ergtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.ergtracker.Model.Database.ModelRepository;
import com.example.ergtracker.Model.RawDataPoint;
import com.example.ergtracker.Model.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private LifecycleOwner lifecycleOwner;
//    private User user;
    private List<User> userList;
    private String previousUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lifecycleOwner = this;

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        this.previousUserName = sharedPref.getString("userName","Default user");
        // try to find user that matches in database, if not there make a new one
        this.userList = new ArrayList<>();
        readAllFromDB(findViewById(android.R.id.content).getRootView());
    }

    public void writeUserToDB(View view, User user) {
        new Thread(() -> {
            ModelRepository modelRepository =  new ModelRepository(view.getContext());
            user.getUserData().forEach(dataPoint -> {
                try {
                    modelRepository.insertTask(dataPoint.getUserName(),
                            dataPoint.getRawTime(), dataPoint.getRawDistance(),
                            dataPoint.getDate().format(DateTimeFormatter.ofPattern(getResources().getString(R.string.date_format))));
                } catch (Exception e) {
                    System.out.println(R.string.write_problem + dataPoint.getUserName());
                    e.printStackTrace();
                }
            });
        }).start();
    }


    public void readUserFromDB(View view, String userName) {
        new Thread(() -> {
            ModelRepository modelRepository =  new ModelRepository(view.getContext());
            try{RawDataPoint rawDataPoint = modelRepository.getTask(userName);
            } catch (Exception e) {
                System.out.println(R.string.read_problem + userName);
                e.printStackTrace();
            }
        }).start();

    }

    void readAllFromDB(View view) {
        try {
            ModelRepository modelRepository = new ModelRepository(view.getContext());
            Observer<List<RawDataPoint>> listObserver = rawDataPointList -> {
                Set<String> userNameSet = new HashSet<>(); // sets have no duplicates
                rawDataPointList.forEach(rawDataPoint -> userNameSet.add(rawDataPoint.getUserName()));
                userNameSet.forEach(userName -> this.userList.add(new User(userName)));
                for (RawDataPoint rawDataPoint : rawDataPointList) {
                    // all users have already been added to user list outside for loop, identify which user to add to
                    User thisUser = this.userList.stream().filter(user -> user.getUserName().equals(rawDataPoint.getUserName())).findFirst().orElse(null);
                    LocalDate date = LocalDate.parse(rawDataPoint.getDateString(),DateTimeFormatter.ofPattern(getResources().getString(R.string.date_format)));
                    thisUser.addDataPoint(rawDataPoint.getRawTime(),rawDataPoint.getRawDistance(),date);
                    System.out.println(date + "data added to User: " + thisUser.getUserName());
                }
            };
            modelRepository.getTasks().observe(lifecycleOwner, listObserver);
        } catch(Exception e) {
            System.out.println("Unable to load from DB");
            e.printStackTrace();
        }
    }

    public void deleteAllDBEntries(View view){
        ModelRepository modelRepository =  new ModelRepository(view.getContext());
        modelRepository.deleteAll();
    }

    public void drawGraph(View view){
        GraphView graph = findViewById(R.id.graph);
        User user = this.userList.stream().filter(previousUser -> previousUser.getUserName().equals(previousUserName)).findFirst().orElse(null);
        if(user!=null) {
            user.estimateAll2KTimes();
            DataPoint[] graphDataArray = new DataPoint[user.getUserData().size()];
            long earliestTimeSinceEpocSeconds = user.getUserData().get(0).getDate().atStartOfDay(ZoneId.of("GMT")).toEpochSecond();
            long latestTimeSinceEpocSeconds = earliestTimeSinceEpocSeconds;
            double minY = user.getUserData().get(0).getPredicted2KTime();
            double maxY = minY;
            for (int i = 0; i < user.getUserData().size(); i++) {
                com.example.ergtracker.Model.DataPoint ergDataPoint = user.getUserData().get(i);
                long timeSinceEpocSeconds = ergDataPoint.getDate().atStartOfDay(ZoneId.of("GMT")).toEpochSecond();
                if(timeSinceEpocSeconds<earliestTimeSinceEpocSeconds){
                    earliestTimeSinceEpocSeconds = timeSinceEpocSeconds;
                } else if(timeSinceEpocSeconds>latestTimeSinceEpocSeconds){
                    latestTimeSinceEpocSeconds = timeSinceEpocSeconds;
                }
                if(ergDataPoint.getPredicted2KTime()<minY){
                    minY = ergDataPoint.getPredicted2KTime();
                } else if(ergDataPoint.getPredicted2KTime()>maxY){
                    maxY = ergDataPoint.getPredicted2KTime();
                }
                graphDataArray[i] = new com.jjoe64.graphview.series.DataPoint(1000*timeSinceEpocSeconds, ergDataPoint.getPredicted2KTime());
            }
            double yPadding = 0.1*(maxY-minY);
            if(yPadding == 0){
                yPadding = 1;
            }
            LineGraphSeries<com.jjoe64.graphview.series.DataPoint> series = new LineGraphSeries<>(graphDataArray);
            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new SimpleDateFormat(getResources().getString(R.string.date_format))));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3);
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(45);
            graph.getViewport().setMinX(1000*earliestTimeSinceEpocSeconds);
            graph.getViewport().setMaxX(1000*latestTimeSinceEpocSeconds);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinY(minY-yPadding);
            graph.getViewport().setMaxY(maxY+yPadding);
            graph.getViewport().setYAxisBoundsManual(true);
//            graph.getGridLabelRenderer().setHumanRounding(false);
        }
    }

    public boolean addDataPoint(MenuItem item){
        View view = this.findViewById(R.id.mainFragmentLayout);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.add_score_title);
        View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_datapoint_dialogue, (ViewGroup) view,false);

        // Decide whether to offer user the option to input name
        final EditText nameText = (EditText) viewInflated.findViewById(R.id.name_field);
        this.previousUserName = sharedPref.getString("userName","Default user");
        if(!previousUserName.equals("Default user")){
            nameText.setText(previousUserName);
            nameText.setEnabled(false);
        }
        User user = this.userList.stream().filter(previousUser ->
                previousUser.getUserName().equals(previousUserName)).findFirst().orElse(new User(previousUserName));

        final EditText distanceText = (EditText) viewInflated.findViewById(R.id.distance_field);
        final EditText timeText = (EditText) viewInflated.findViewById(R.id.time_field);
        final EditText dateText = (EditText) viewInflated.findViewById(R.id.date_field);
        dateText.setText(LocalDate.now().format(DateTimeFormatter.ofPattern(getResources().getString(R.string.date_format))));
        builder.setView(viewInflated);
        builder.setPositiveButton(R.string.OK, (dialog, i) -> {
            // FIX THIS: add data validation here
            dialog.dismiss();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userName",nameText.getText().toString());
            editor.apply();
            this.setTitle(nameText.getText().toString());
            user.setUserName(nameText.getText().toString());
            user.addDataPoint(Double.parseDouble(timeText.getText().toString()),
                    Double.parseDouble(distanceText.getText().toString()),
                    dateText.getText().toString());
            writeUserToDB(view,user);
            System.out.println("Adding: " + distanceText.getText() + "m " + timeText.getText() + "s " + dateText.getText() + " to database");
            if(user.getUserData().size()<2){
                Toast.makeText(view.getContext(),
                        "At least 2 data points are required to scale readings.",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.Cancel, (dialog,i) -> dialog.cancel());
        builder.show();
        return true;
    }

}