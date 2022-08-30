package com.varsitycollege.vinyl_warehouse;

import static com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import java.util.ArrayList;
import java.util.List;

public class Graph extends AppCompatActivity {

    public static final String ITEMS_IN_EACH_CATEGORY_BY = "Items In Each Category by %";
    public static final String MUSIC_CATEGORIES = "Music Categories";
    // Declaring variables for the GUI components
    List<Categories> categoriesList;
    //make pie chart variable
    private PieChart genresChart;
    ProgressBar progressGraph;
    ImageView btn_back_arrow;
    //Code Attribution
    //Link: https://www.youtube.com/watch?v=S3zqxVoIUig
    //Author: Learn to Droid
    //End
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Assigning GUI components to the variables
        //pass pie chart from view into graph pie chart variable
        genresChart = findViewById(R.id.pieChart_genres);
        progressGraph = findViewById(R.id.progressGraph);
        progressGraph.setVisibility(View.VISIBLE);
        btn_back_arrow = findViewById(R.id.imgGraph_BackArrow);

        // Handling the button click
        btn_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMain = new Intent(Graph.this, MainActivity.class);
                startActivity(backToMain);
                finish();
            }
        });

        categoriesList = new ArrayList<>();
        //Get all categories
        // Creating firebase read worker object
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        firebaseReadWorker.getAllCategory(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Categories categories = dataSnapshot.getValue(Categories.class);
                categoriesList.add(categories);
            }

            @Override
            public void onStartWork() {
                progressGraph.setVisibility(View.INVISIBLE);
                ArrayList<PieEntry> graphEntries = new ArrayList<PieEntry>();
                // /settup format of pie chart before dealing with data of piechart
                setupPieChart();
                //Connect to graph to add data here
                double totalItems = 0.0;
                for (Categories categories : categoriesList) {
                    if (categories.getAlbumIDs() != null) {
                        totalItems = totalItems + categories.getAlbumIDs().size();
                    }
                }
                double eachItem = 0.0;
                eachItem = 1 / totalItems;

                // Adding data to the graph to display
                for (Categories categories : categoriesList) {
                    if (categories.getAlbumIDs() != null) {
                        float myFloat = (float) (eachItem * categories.getAlbumIDs().size());
                        graphEntries.add(new PieEntry(myFloat, categories.getCategoryName()));
                    } else {
                        graphEntries.add(new PieEntry(0f, categories.getCategoryName()));
                    }
                }

                //create arraylist to hold integers that reference colors from MPAndroidChart library, to be used for graph
                ArrayList<Integer> colors = new ArrayList<Integer>();

                //run through colors in MPAndroidChart Libraries "ColorTemplate" for a color pallette with "MATERIAL_COLORS"
                for (int color : MATERIAL_COLORS) {
                    colors.add(color);
                }

                //run through colors in MPAndroidChart Libraries "ColorTemplate" for a color pallette with "VORDIPLOM_COLORS"
                for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }

                //create a data set of values for chart using entries from "graphEntries"
                //Multiple datasets can be added to a graph, each with their own entries, however we only have genres as a type of
                //data set
                PieDataSet dataSet = new PieDataSet(graphEntries, MUSIC_CATEGORIES);
                //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                //set colours for the dataset that will appear on graph to colors pulled into colors arraylist
                dataSet.setColors(colors);


                //creating of actual pie data for pie chart
                PieData data = new PieData(dataSet);

                //draw values of different percentages on graph
                data.setDrawValues(true);
                //set format for values entered in graph entries to read them as percentages of the graph
                data.setValueFormatter(new PercentFormatter(genresChart));
                //set the text size of values in piechart data for displaying format
                data.setValueTextSize(17);
                //set colour of text values in piechart data for displaying format
                data.setValueTextColor(Color.BLACK);

                //send piechart data to piechart
                genresChart.setData(data);
                //call invalidate method to communicate to the pie chart that their is a change in data and it will update on UI side
                genresChart.invalidate();
            }

            @Override
            public void onFailure() {
            //method unused
            }
        });

    }

    private void setupPieChart() {
        //allows a transparent hole in middle of piechart, to form "donut" look, to be made
        genresChart.setDrawHoleEnabled(true);
        //set colour for hole to transparent
        genresChart.setHoleColor(getColor(R.color.dark_purple)); //(R.color.dark_purple); // setHoleColorTransparent(true);
        //communicate to pie chart that values sed will represent percentages
        genresChart.setUsePercentValues(true);
        //set the text size for labels the piechart will show
        genresChart.setEntryLabelTextSize(17);
        //set the text colour for labels the piechart will show
        genresChart.setEntryLabelColor(Color.BLACK);
        //set text that will be displayed in centre of chart
        genresChart.setCenterText(ITEMS_IN_EACH_CATEGORY_BY);
        //set text size for centre text
        genresChart.setCenterTextSize(22);
        //set colour for centre text
        genresChart.setCenterTextColor(Color.WHITE);
        //disable showing of chart description text
        genresChart.getDescription().setEnabled(false);

        //get legend for piechart
        Legend chartLegend = genresChart.getLegend();
        //set text colour for legend
        chartLegend.setTextColor(Color.WHITE);
        //set text size for text in legend
        chartLegend.setTextSize(14);
        //set legend to appear bellow chart
        chartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //set legend to align with middle of chart
        chartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //set orientation of legend to Horizontal
        chartLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        chartLegend.setDrawInside(false);
        //enable legend for chart
        chartLegend.setEnabled(true);
    }
}