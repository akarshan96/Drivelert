package com.example.akarshan.drivelert;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import br.com.bloder.magic.view.MagicButton;

public class monitor_menu extends Fragment {
    MagicButton b;
    SeekBar s;
    TextView ttv;
    private String key_2 = "akarshan's project";
    private String key_4 = "senstivity";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root;
        root = inflater.inflate(R.layout.activity_monitor_menu,container,false);
        b = (MagicButton) root.findViewById(R.id.magic_button);
        s = (SeekBar)root.findViewById(R.id.seekBar2);
        ttv = (TextView)root.findViewById(R.id.textView21);
        if(s.getProgress() == 0)
        {
            ttv.setText("0.5 second");
        }
        else if(s.getProgress() == 1)
        {
            ttv.setText("0.75 second");
        }
        else if(s.getProgress() == 2)
        {
            ttv.setText("1 seconds");
        }
        else if(s.getProgress() == 3)
        {
            ttv.setText("1.25 seconds");
        }
        else if(s.getProgress() == 4)
        {
            ttv.setText("1.5 seconds");
        }
        else if(s.getProgress() == 5)
        {
            ttv.setText("1.75 seconds");
        }
        else if(s.getProgress() == 6)
        {
            ttv.setText("2 seconds");
        }
        else if(s.getProgress() == 7)
        {
            ttv.setText("2.25 seconds");
        }
        else
        {
            ttv.setText("2.5 seconds");
        }
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                         @Override
                                         public void onStopTrackingTouch(SeekBar seekBar) {

                                         }

                                         @Override
                                         public void onStartTrackingTouch(SeekBar seekBar) {

                                         }

                                         @Override
                                         public void onProgressChanged(SeekBar seekBar, int progress,
                                                                       boolean fromUser) {
                                             if(s.getProgress() == 0)
                                             {
                                                 ttv.setText("0.5 second");
                                             }
                                             else if(s.getProgress() == 1)
                                             {
                                                 ttv.setText("0.75 second");
                                             }
                                             else if(s.getProgress() == 2)
                                             {
                                                 ttv.setText("1 seconds");
                                             }
                                             else if(s.getProgress() == 3)
                                             {
                                                 ttv.setText("1.25 seconds");
                                             }
                                             else if(s.getProgress() == 4)
                                             {
                                                 ttv.setText("1.5 seconds");
                                             }
                                             else if(s.getProgress() == 5)
                                             {
                                                 ttv.setText("1.75 seconds");
                                             }
                                             else if(s.getProgress() == 6)
                                             {
                                                 ttv.setText("2 seconds");
                                             }
                                             else if(s.getProgress() == 7)
                                             {
                                                 ttv.setText("2.25 seconds");
                                             }
                                             else
                                             {
                                                 ttv.setText("2.5 seconds");
                                             }


                                         }
                                     });

                b.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(root.getContext(),FaceTrackerActivity.class);
                i.putExtra(key_4,""+s.getProgress());
                i.putExtra(key_2,DateFormat.getDateTimeInstance().format(new Date()));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();

            }
        });
        return root;

    }
}
