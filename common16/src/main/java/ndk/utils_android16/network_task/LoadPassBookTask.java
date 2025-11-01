package ndk.utils_android16.network_task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import ndk.utils_android1.DateUtils1;
import ndk.utils_android1.ErrorUtils;
import ndk.utils_android1.ExceptionUtils1;
import ndk.utils_android1.LogUtils1;
import ndk.utils_android1.NetworkUtils1;
import ndk.utils_android1.ToastUtils1;
import ndk.utils_android14.NetworkUtils14;
import ndk.utils_android16.Float_Utils;
import ndk.utils_android16.JsonUtils;
import ndk.utils_android16.Pass_Book_Utils;
import ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntry;
import ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntryV2;
import ndk.utils_android16.widgets.pass_book.PassBookTableView;
import ndk.utils_android16.widgets.pass_book.PassBookTableViewV2;

import static ndk.utils_android1.DateUtils1.mySqlDateTimeFormat;
import static ndk.utils_android1.ProgressBarUtils1.showProgress;

public class LoadPassBookTask extends AsyncTask<Void, Void, String[]> {

    private String url, applicationName;
    private AppCompatActivity currentActivity;
    private ProgressBar progressBar;
    private View scrollView;
    private PassBookTableView passBookTableView;
    private PassBookTableViewV2 passBookTableViewV2;
    private Pair[] nameValuePair;
    private boolean v2Flag, sortFlag;
    private String currentAccountId;

    public LoadPassBookTask(String url, AppCompatActivity currentActivity, ProgressBar progressBar, View scrollView, String applicationName, PassBookTableView passBookTableView, Pair[] nameValuePair) {

        this.url = url;
        this.currentActivity = currentActivity;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.passBookTableView = passBookTableView;
        this.nameValuePair = nameValuePair;
        this.v2Flag = false;
        this.sortFlag = false;
    }

    public LoadPassBookTask(String url, AppCompatActivity currentActivity, ProgressBar progressBar, View scrollView, String applicationName, PassBookTableViewV2 passBookTableViewV2, String currentAccountId) {

        this.url = url;
        this.currentActivity = currentActivity;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.passBookTableViewV2 = passBookTableViewV2;
        this.currentAccountId = currentAccountId;
        this.v2Flag = true;
        this.sortFlag = false;
    }

    public LoadPassBookTask(String url, AppCompatActivity currentActivity, ProgressBar progressBar, View scrollView, String applicationName, PassBookTableViewV2 passBookTableViewV2, String currentAccountId, boolean sortFlag) {

        this.url = url;
        this.currentActivity = currentActivity;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.passBookTableViewV2 = passBookTableViewV2;
        this.currentAccountId = currentAccountId;
        this.v2Flag = true;
        //TODO : Implement Sort Option
        //Sort flag always true
        this.sortFlag = sortFlag;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        if (v2Flag) {
            return NetworkUtils1.performHttpClientGetTask(url);
        } else {
            return NetworkUtils14.performHttpClientPostTask(url, nameValuePair);
        }
    }


    @Override
    protected void onPostExecute(final String[] networkActionResponseArray) {

        showProgress(false, currentActivity, progressBar, scrollView);

        NetworkUtils1.displayNetworkActionResponse(applicationName, networkActionResponseArray, currentActivity);

        ArrayList<PassBookEntry> passBookEntries = new ArrayList<>();
        ArrayList<PassBookEntryV2> passBookEntryV2s = new ArrayList<>();

        if (networkActionResponseArray[0].equals("1")) {

            ToastUtils1.errorToast(currentActivity, applicationName);

        } else {

            try {

                JSONArray tempJsonArray = new JSONArray(networkActionResponseArray[1]);
                String tempStatus = tempJsonArray.getJSONObject(0).getString("status");
                if (tempStatus.equals("2")) {

                    ToastUtils1.noEntriesToast(currentActivity);

                } else if (tempStatus.equals("0")) {

                    if (sortFlag) {

                        //TODO : Enhancement - Use Direct Pattern
                        enterTransactions(JsonUtils.sortJsonArrayByDateInSimpleDateFormatField(tempJsonArray, DateUtils1.mySqlDateTimeFormat, "event_date_time", applicationName, currentActivity), passBookEntryV2s, 1);

                    } else {

                        if (v2Flag) {

                            enterTransactions(tempJsonArray, passBookEntryV2s, 1);

                        } else {

                            float balance = 0;

                            for (int i = 1; i < tempJsonArray.length(); i++) {

                                if (tempJsonArray.getJSONObject(i).getString("particulars").contains("Credit")) {

                                    balance = balance + Float.parseFloat(tempJsonArray.getJSONObject(i).getString("amount"));

                                    passBookEntries.add(0, new PassBookEntry(mySqlDateTimeFormat.parse(tempJsonArray.getJSONObject(i).getString("event_date_time")), tempJsonArray.getJSONObject(i).getString("particulars"), 0, Double.parseDouble(tempJsonArray.getJSONObject(i).getString("amount")), Float_Utils.roundOff_to_two_positions(balance)));

                                    LogUtils1.debug(applicationName, "Balance : " + balance, currentActivity);
                                }

                                if (tempJsonArray.getJSONObject(i).getString("particulars").contains("Debit")) {

                                    balance = balance - Float.parseFloat(tempJsonArray.getJSONObject(i).getString("amount"));

                                    passBookEntries.add(new PassBookEntry(mySqlDateTimeFormat.parse(tempJsonArray.getJSONObject(i).getString("event_date_time")), tempJsonArray.getJSONObject(i).getString("particulars"), Double.parseDouble(tempJsonArray.getJSONObject(i).getString("amount")), 0, Float_Utils.roundOff_to_two_positions(balance)));

                                    LogUtils1.debug(applicationName, "Balance : " + balance, currentActivity);
                                }
                            }

                            Pass_Book_Utils.bind(passBookTableView, currentActivity, passBookEntries);
                        }
                    }
                }
            } catch (JSONException | ParseException e) {

                ExceptionUtils1.handleExceptionOnGui(currentActivity, applicationName, e);
            }
        }
    }

    public void enterTransactions(JSONArray jsonArray, ArrayList<PassBookEntryV2> passBookEntryV2s, int jsonArrayStartIndex) {

        float balance = 0;

        try {
            for (int i = jsonArrayStartIndex; i < jsonArray.length(); i++) {

                if (jsonArray.getJSONObject(i).getString("from_account_id").equals(currentAccountId)) {

                    balance = balance - Float.parseFloat(jsonArray.getJSONObject(i).getString("amount"));

                    passBookEntryV2s.add(0, new PassBookEntryV2(mySqlDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("event_date_time")), jsonArray.getJSONObject(i).getString("particulars"), jsonArray.getJSONObject(i).getString("from_account_name"), jsonArray.getJSONObject(i).getString("to_account_name"), 0, Double.parseDouble(jsonArray.getJSONObject(i).getString("amount")), balance, jsonArray.getJSONObject(i).getInt("from_account_id"), jsonArray.getJSONObject(i).getInt("to_account_id"), jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("from_account_full_name"), jsonArray.getJSONObject(i).getString("to_account_full_name")));

                } else {

                    balance = balance + Float.parseFloat(jsonArray.getJSONObject(i).getString("amount"));

                    passBookEntryV2s.add(0, new PassBookEntryV2(mySqlDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("event_date_time")), jsonArray.getJSONObject(i).getString("particulars"), jsonArray.getJSONObject(i).getString("to_account_name"), jsonArray.getJSONObject(i).getString("from_account_name"), Double.parseDouble(jsonArray.getJSONObject(i).getString("amount")), 0, balance, jsonArray.getJSONObject(i).getInt("from_account_id"), jsonArray.getJSONObject(i).getInt("to_account_id"), jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("from_account_full_name"), jsonArray.getJSONObject(i).getString("to_account_full_name")));
                }
            }
            Pass_Book_Utils.bindV2(passBookTableViewV2, currentActivity, passBookEntryV2s);

        } catch (JSONException | ParseException e) {

            ExceptionUtils1.handleExceptionOnGui(currentActivity, applicationName, e);
        }
    }

    @Override
    protected void onCancelled() {
        showProgress(false, currentActivity, progressBar, scrollView);
    }
}
