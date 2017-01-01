package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewInflator = inflater.inflate(R.layout.fragment_start, container, false);
        endNotification(viewInflator);
        startNotification(viewInflator);
        showSummary(viewInflator);

        return viewInflator;
    }


    public void startNotification(View v) {
        Button startNotification = (Button) v.findViewById(R.id.startButton);
        startNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast t = Toast.makeText(v.getContext(), "yay button click", Toast.LENGTH_LONG);
//                t.show();
                Intent service = new Intent(getActivity(), NotificationForgroundService.class);
                service.setAction("startForeground");
                getActivity().startService(service);
                //sendNotification();
            }
        });

    }

    public void endNotification(View v) {
        Button endNotification = (Button) v.findViewById(R.id.endButton);
        endNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationForgroundService.class);
                getActivity().stopService(intent);
                Toast.makeText(getActivity(), "service stoped", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showSummary(View v) {
        Button summarybtn = (Button) v.findViewById(R.id.summaryButton);
        summarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment summaryFragment = new SummaryFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.activity_main, summaryFragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
    }
}

