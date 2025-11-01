package ndk.utils_android16;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import ndk.utils_android1.ErrorUtils;
import ndk.utils_android1.ExceptionUtils1;

/**
 * Created on 24-08-2018 21:22 under VLottery.
 */
public class Time_Over_Utils {

    private Time_Over_Actions time_over_actions = null;
    private Time_Not_Over_Actions time_not_over_actions = null;

    public Time_Over_Utils(Time_Over_Actions time_over_actions, Time_Not_Over_Actions time_not_over_actions) {

        this.time_over_actions = time_over_actions;
        this.time_not_over_actions = time_not_over_actions;

    }

    public void check_Time_Status(AppCompatActivity activity, JSONArray json_array, String applicationName) {
        try {

            //Example Response : [{"user_count":"1","id":"125"},{"time_status":"1"}]
            switch (json_array.getJSONObject(1).getString("time_status")) {
                case "0": //OK
                    time_not_over_actions.configure_time_not_over_actions();
                    break;

                case "1": //Time Over
                    time_over_actions.configure_time_over_actions();
                    break;

                case "-1": //Close Application
                    ServerUtils.onMaintenanceClose(activity);
                    break;
            }

        } catch (JSONException json_exception) {

            ExceptionUtils1.handleExceptionOnGui(activity, applicationName, json_exception);
        }
    }

    public interface Time_Over_Actions {
        void configure_time_over_actions();
    }

    public interface Time_Not_Over_Actions {
        void configure_time_not_over_actions();
    }
}
