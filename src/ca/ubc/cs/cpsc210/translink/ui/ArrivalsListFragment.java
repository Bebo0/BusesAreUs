package ca.ubc.cs.cpsc210.translink.ui;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ubc.cs.cpsc210.translink.R;
import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.ArrivalsParser;
import ca.ubc.cs.cpsc210.translink.parsers.StopParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.AbstractHttpDataProvider;
import ca.ubc.cs.cpsc210.translink.providers.HttpArrivalDataProvider;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ca.ubc.cs.cpsc210.translink.model.StopManager.getInstance;

/**
 * Fragment to display list of arrivals at selected stop
 */
public class ArrivalsListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        ArrayList<Arrival> arrivals = getArrivalsForSelectedStop();
        ArrivalsAdapter adapter = new ArrivalsAdapter(arrivals);

        setListAdapter(adapter);
    }

    /**
     * Get arrivals for selected stop
     *
     * @return   list of arrivals at selected stop
     */
    private ArrayList<Arrival> getArrivalsForSelectedStop() {
        HttpArrivalDataProvider http = new HttpArrivalDataProvider(getInstance().getSelected());

        try {
            StopParser sp = new StopParser(http.dataSourceToString());
            sp.parseStops(http.dataSourceToString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (StopDataMissingException e) {
            e.printStackTrace();
        }

        ArrayList<Arrival> arrivals = new ArrayList<>();
        for (Arrival a: StopManager.getInstance().getSelected())
        {
            arrivals.add(a);
        }
        return arrivals;
    }

    /**
     * Array adapter for list of arrivals displayed to user
     */
    private class ArrivalsAdapter extends ArrayAdapter<Arrival> {
        public ArrivalsAdapter(ArrayList<Arrival> arrivals) {
            super(getActivity(), 0, arrivals);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.arrival_item, null);
            }

            Arrival arrival = getItem(position);
            TextView destination = (TextView) convertView.findViewById(R.id.destination);
            destination.setText(arrival.getDestination());
            TextView platform = (TextView) convertView.findViewById(R.id.platform);
            platform.setText(arrival.getRoute().getNumber());
            TextView waitTime = (TextView) convertView.findViewById(R.id.wait_time);
            waitTime.setText(Integer.toString(arrival.getTimeToStopInMins()) + " mins");
            switch (arrival.getStatus()) {
                case "+":
                    waitTime.setBackgroundColor(Color.GREEN);
                    break;
                case "-":
                    waitTime.setBackgroundColor(Color.RED);
                    break;
            }

            return convertView;
        }
    }
}
