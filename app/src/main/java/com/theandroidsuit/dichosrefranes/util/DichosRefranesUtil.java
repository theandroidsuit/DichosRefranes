package com.theandroidsuit.dichosrefranes.util;

import android.content.Context;

import com.theandroidsuit.dichosrefranes.bean.DichoRefran;

import java.util.List;
import java.util.Random;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

public class DichosRefranesUtil {

    private static final String TAG = "DichosRefranesUtil";


    public static final String WIDGET_UPDATE_ACTION = "com.theandroidsuit.dichosrefranes.intent.action.UPDATE_WIDGET";
    public static final String PREFIX_WIDGET_PROPERTIES = "dichorefran";


    public static final String DICHOS_Y_REFRANES_TITLE = "Dichos y Refranes";

    public static final String MODE_ASSET = "asset";
    public static final String MODE_SERVICE = "service";

    private List<DichoRefran> listWisdom = null;
    private static DichoRefran wisdom = null;

    private Context context;
    private int color;

    public DichosRefranesUtil( Context context, int color) {
        this.context = context;
        this.color = color;
        getNewDichoRefran();
    }

    private  void getNewDichoRefran() {
        if (null != context ||  null != listWisdom){

            if (null == listWisdom){
                DichosRefranesJSONParser jParser = new DichosRefranesJSONParser();
                listWisdom = jParser.getDatafromAsset(context);
            }

            Random rnd = new Random(System.currentTimeMillis());
            int randomWisdomLocation = rnd.nextInt(listWisdom.size());

            wisdom = listWisdom.get(randomWisdomLocation);


        }else{
            wisdom = new DichoRefran();
        }
    }

    public static DichoRefran getCurrentDichoRefran() {
        return wisdom;
    }
}
