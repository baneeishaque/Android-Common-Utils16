package ndk.utils_android16;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import ndk.utils_android1.ErrorUtils;
import ndk.utils_android1.ExceptionUtils1;
import ndk.utils_android1.LogUtils1;

public class LocationUtils {

    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    String applicationTag;
    Context currentApplicationContext;

    public LocationUtils(String applicationTag, Context currentApplicationContext) {

        this.applicationTag = applicationTag;
        this.currentApplicationContext = currentApplicationContext;
    }

    LocationListener locationListenerNetwork = new LocationListener() {

        public void onLocationChanged(Location location) {

            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerGps = new LocationListener() {

        public void onLocationChanged(Location location) {

            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public boolean getLocation(Context context, LocationResult result) {

        //Use LocationResult callback class to pass location value from LocationUtils to user code.
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps_enabled) {

                SnackbarUtils16.displayShortNoFabErrorBottomSnackBar(context, "Please Enable GPS...");
                return false;
            }
        } catch (Exception ex) {

            ExceptionUtils1.handleExceptionOnGui(context, applicationTag, ex);
            return false;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!network_enabled) {

                SnackbarUtils16.displayShortNoFabErrorBottomSnackBar(context, "Please Enable Mobile Data...");
                return false;
            }
        } catch (Exception ex) {

            ExceptionUtils1.handleExceptionOnGui(context, applicationTag, ex);
            return false;
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {

            return false;
        }
        try {
            if (gps_enabled)

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);

            if (network_enabled)

                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

        } catch (SecurityException ex) {

            ExceptionUtils1.handleExceptionOnGui(context, applicationTag, ex);
            return false;
        }

        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 20000);
        return true;
    }

    public void cancelTimer() {

        timer1.cancel();
        lm.removeUpdates(locationListenerGps);
        lm.removeUpdates(locationListenerNetwork);
    }

    public static abstract class LocationResult {

        public abstract void gotLocation(Location location);
    }

    class GetLastLocation extends TimerTask {

        @Override
        public void run() {

            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            try {

                if (gps_enabled)
                    gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (network_enabled)
                    net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            } catch (SecurityException e) {

                LogUtils1.debug(applicationTag, "exception", currentApplicationContext);
            }
            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {

                if (gps_loc.getTime() > net_loc.getTime())

                    locationResult.gotLocation(gps_loc);

                else
                    locationResult.gotLocation(net_loc);

                return;
            }

            if (gps_loc != null) {

                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {

                locationResult.gotLocation(net_loc);
                return;
            }
            locationResult.gotLocation(null);
        }
    }
}
