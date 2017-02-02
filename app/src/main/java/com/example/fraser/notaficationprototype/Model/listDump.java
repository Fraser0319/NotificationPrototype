package com.example.fraser.notaficationprototype.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fraser on 02/02/2017.
 */

public class listDump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("Target", cricket);
        expandableListDetail.put("Authenticator", football);
        expandableListDetail.put("Emotion", basketball);
        return expandableListDetail;
    }

}
