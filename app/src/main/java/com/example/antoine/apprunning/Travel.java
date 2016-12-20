package com.example.antoine.apprunning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by valentin on 20/12/2016.
 */

public class Travel implements Serializable {
    public List<Mesure> points;
    public String proprio;
    public Travel(String p){
        proprio=p;
        points=new ArrayList<>();
    }

    @Override
    public String toString(){
        return proprio;
    }
}
