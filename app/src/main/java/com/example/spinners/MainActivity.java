package com.example.spinners;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] transmissoes={"Automatica","Manual"};
    private RequestQueue queue;
    public Spinner spinnermarcas,spinnermodels,spinnerengine,spinnertransmission;
    public int brandid,modelid,temp=1,temp1=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //atribuicao dos elementos de design com os objeto
        spinnermarcas = (Spinner) findViewById(R.id.marca);
        spinnermodels = (Spinner) findViewById(R.id.modelo);
        //spinnerengine = (Spinner) findViewById(R.id.motor);
        //spinnertransmission = (Spinner) findViewById(R.id.transmissao);

        //                                             Criaçao das arrays list
        ArrayList<String> brandlist= new ArrayList<>();
        ArrayList<String> brandidlist= new ArrayList<>();
        ArrayList<String> modellist= new ArrayList<>();
        ArrayList<String> enginelist= new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        //get das brands do server
        StringRequest brands = new StringRequest(Request.Method.GET, "https://mechanic-on-the-go.herokuapp.com/api/brands",
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.e("this", "entrou");
                            JSONArray json  = new JSONArray(response);
                            for (int i = 0; i<json.length(); i++){
                                JSONObject motor = json.getJSONObject(i);
                                brandlist.add(motor.getString("brandName"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { System.out.println(error); }
                });




        spinnermarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                temp=position;
                Log.e("this", ""+position);


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        StringRequest models = new StringRequest(Request.Method.GET, "https://mechanic-on-the-go.herokuapp.com/api/models/brand/"+temp,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.e("this", "models brands");
                            JSONArray json  = new JSONArray(response);
                            for (int i = 0; i<json.length(); i++){
                                JSONObject motor = json.getJSONObject(i);
                                modellist.add(motor.getString("modelName"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { System.out.println(error); }
                });





        spinnermodels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    temp1=temp1+position;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> brandadapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,brandlist);
        brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermarcas.setAdapter(brandadapter);
        // spinnermarcas.set;

        ArrayAdapter<String> modeldapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,modellist);
        modeldapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermodels.setAdapter(modeldapter);

        queue.add(brands);
        queue.add(models);








    }


}