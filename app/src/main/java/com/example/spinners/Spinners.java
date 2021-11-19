package com.example.spinners;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Spinners extends AppCompatActivity {
    Spinner spinnerBrands, spinnerModels, spinnerEngine, spinnerTransmision;

    ArrayList<String> brands;
    ArrayList<String> models;
    ArrayList<String> brandNames;
    private String[] transmissao= {"Automatica","Manual"};

    JSONArray objBrands = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinners);

        spinnerBrands = (Spinner) findViewById(R.id.spinmarca);
        spinnerModels = (Spinner) findViewById(R.id.spinmodelo);
        spinnerEngine = (Spinner) findViewById(R.id.spinengine);
        spinnerTransmision = (Spinner) findViewById(R.id.spintrans);

        JSONArrayDownloader task = new JSONArrayDownloader();
        try {
            objBrands = task.execute("https://mechanic-on-the-go.herokuapp.com/api/brands").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objBrands = null;
        }

        JSONObject obj;
        brands = new ArrayList<>();
        models = new ArrayList<>();
        brandNames = new ArrayList<>();
        if(objBrands != null) {
            for(int i = 0; i < objBrands.length(); i++) {
                try {
                    obj = objBrands.getJSONObject(i);
                    String routeName = obj.getString("brandName");
                    brands.add(routeName);
                    models.add(obj.getString("id"));
                    brandNames.add(obj.getString("brandName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }





        ArrayAdapter<String> brandadapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brands);
        brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrands.setAdapter(brandadapter);

        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<String> models;
                JSONArray spotsArray = null;

                JSONArrayDownloader task = new JSONArrayDownloader();
                String url = "https://mechanic-on-the-go.herokuapp.com/api/models/brand/" + Spinners.this.models.get(position);
                try {
                    spotsArray = task.execute(url).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    spotsArray = null;
                }

                JSONObject obj;
                models = new ArrayList<>();

                if (spotsArray != null) {
                    for (int i = 0; i < spotsArray.length(); i++) {
                        try {
                            obj = spotsArray.getJSONObject(i);
                            models.add(obj.getString("modelName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }



                    ArrayAdapter<String> modeladapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, models);
                    modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerModels.setAdapter(modeladapter);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> transAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, transmissao);
        transAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransmision.setAdapter(transAdapter);

    }
}