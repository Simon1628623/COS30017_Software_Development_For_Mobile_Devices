package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.simonor.motorcycleapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBike.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBike#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBike extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinner;
    Button b;
    EditText y;
    EditText m;
    EditText o;

    String Make;

    public AddBike() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBike.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBike newInstance(String param1, String param2) {
        AddBike fragment = new AddBike();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_bike, container, false);

        spinner = (Spinner) v.findViewById(R.id.make);
        b = (Button) v.findViewById(R.id.button);
        y = (EditText) v.findViewById(R.id.year);
        m = (EditText) v.findViewById(R.id.model);
        o = (EditText) v.findViewById(R.id.Odo);

        InitializeUI();

        return v;
    }

    public void InitializeUI()
    {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.bike_makes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                Make = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        b.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submit();
            }
        });
    }

    public void submit()
    {
        if(y.getText().toString().equalsIgnoreCase("") && m.getText().toString().equalsIgnoreCase("") && o.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
        }
        else
        {
            boolean success = true;
            Bike b = new Bike();
            try {

                b.Make = Make;
                String yr = y.getText().toString();
                b.Year = Integer.parseInt(yr);
                b.Model = m.getText().toString();
                String od = o.getText().toString();
                b.Odometer = Double.parseDouble(od);
            } catch (NumberFormatException e) {
                success = false;
            }
            if(success)
            {
                Toast.makeText(getContext(),b.Make + " " + b.Year + " " + b.Model + " " + b.Odometer, Toast.LENGTH_LONG).show();

                String s = b.Make + "," + b.Year + "," + b.Model + "," + b.Odometer + "\n";
                writeToFile(s);

                y.setText("");
                m.setText("");
                o.setText("");
            }
            else
            {
                Toast.makeText(getContext(), "Please enter correct fields", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void writeToFile(String s)
    {
        //creating file if it does not exist
        File f = new File("bikes.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] b = s.getBytes();
        FileOutputStream out = null;
        try {
            out = getActivity().openFileOutput(f.getName(), MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
