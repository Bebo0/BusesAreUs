package ca.ubc.cs.cpsc210.translink.providers;

import ca.ubc.cs.cpsc210.translink.auth.TranslinkToken;
import ca.ubc.cs.cpsc210.translink.model.Stop;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wrapper for Translink Bus Location Data Provider
 */
public class HttpBusLocationDataProvider extends AbstractHttpDataProvider {
    private Stop stop;

    public HttpBusLocationDataProvider(Stop stop) {
        super();
        this.stop = stop;
    }

    @Override
    /**
     * Produces URL used to query Translink web service for locations of buses serving
     * the stop specified in call to constructor.
     *
     * @returns URL to query Translink web service for arrival data
     */
    protected URL getURL() throws MalformedURLException {
        String key = TranslinkToken.TRANSLINK_API_KEY;
        Integer routeno = stop.getNumber();
        String routenostring = Integer.toString(routeno);
        //"http://api.translink.ca/rttiapi/v1/buses?apikey="+key"&stopNo=53987"

        String url = "http://api.translink.ca/rttiapi/v1/buses?apikey="+key+"&stopNo="+routenostring;
        URL actualurl = new URL(url);

        return actualurl;

    }

    @Override
    public byte[] dataSourceToBytes() throws IOException {
        return new byte[0];
    }
}
