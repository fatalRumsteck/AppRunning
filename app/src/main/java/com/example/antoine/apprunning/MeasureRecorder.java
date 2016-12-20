package com.example.antoine.apprunning;

import android.content.Context;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by valentin on 20/12/2016.
 */

public class MeasureRecorder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7345525981579010310L;

    public List<Travel> travels;
    private Travel currentTravel = new Travel("first");
    public boolean isRecording = false;

    public MeasureRecorder(){travels = new ArrayList<>();}

    public void add(Mesure mes){
        currentTravel.points.add(mes);
    }

    public void start(String nom){
        currentTravel = new Travel(nom);
        isRecording=true;
    }

    public ArrayList<String> stop(Context con){
        travels.add(currentTravel);
        isRecording=false;

        ArrayList<String> list = new ArrayList<String>(travels.size());
        for (int i = 0; i < travels.size(); ++i) {
            list.add(i + " : " + travels.get(i).toString());
        }
        return list;
    }
}
