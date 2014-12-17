package com.theandroidsuit.dichosrefranes.util;

import android.content.Context;

import com.theandroidsuit.dichosrefranes.bean.DichoRefran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

public class DichosRefranesJSONParser {
    public List<DichoRefran> getDatafromAsset(Context context){
        JSONObject jObject = loadJSON(context, DichosRefranesUtil.MODE_ASSET);

        return parse(jObject);
    }

    /** Receives a JSONObject and returns a list */
    public List<DichoRefran> parse(JSONObject jObject){

        JSONArray jWisdom = null;
        String type = "";
        try {
            /** Retrieves all the elements in the 'wisdom' array */
            jWisdom = jObject.getJSONArray("wisdom");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getSugarWisdom(jWisdom, type);
    }


    private List<DichoRefran> getSugarWisdom(JSONArray jWisdom, String type) {

        int wisdomCount = jWisdom.length();
        List<DichoRefran> wisdomList = new ArrayList<DichoRefran>();
        DichoRefran wisdom = null;

        /** Taking each Wisdom, parses and adds to list object */
        for(int i = 0; i < wisdomCount; i++){
            try {
                /** Call getCrystalWisdom with wisdom JSON object to parse the Wisdom */
                wisdom = getCrystalWisdom((JSONObject)jWisdom.get(i));
                wisdomList.add(wisdom);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return wisdomList;
    }

    private DichoRefran getCrystalWisdom(JSONObject jWisdom) {
        DichoRefran wisdom = new DichoRefran();

        try {

            // Extracting dicho, if available
            if(!jWisdom.isNull("sentence")){
                wisdom.setDichoRefran(jWisdom.getString("sentence"));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return wisdom;
    }

    public JSONObject loadJSON(Context context, String mode){
        if (mode.equals(DichosRefranesUtil.MODE_ASSET)){
            return loadJSONFromAsset(context);
        }else if (mode.equals(DichosRefranesUtil.MODE_SERVICE)){
            // Call Sinatra/Google AppEngine and return JSON
            return null;
        }

        return null;
    }


    public JSONObject loadJSONFromAsset(Context context) {
        String jsonStr = null;
        try {

            String fileTheme = "dichosRefranes.json";

            InputStream is = context.getAssets().open(fileTheme);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            jsonStr = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }
}
