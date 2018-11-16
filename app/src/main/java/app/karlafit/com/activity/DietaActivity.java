package app.karlafit.com.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import app.karlafit.com.R;

public class DietaActivity extends Fragment {

    private ImageView play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_dieta, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        play = (ImageView) view.findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SemanaDietaActivity nextFrag= new SemanaDietaActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();


            }
        });

    }
}
