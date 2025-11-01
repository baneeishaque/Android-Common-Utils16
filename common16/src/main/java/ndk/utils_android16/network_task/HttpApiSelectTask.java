package ndk.utils_android16.network_task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ndk.utils_android1.ExceptionUtils1;
import ndk.utils_android1.LogUtils1;
import ndk.utils_android1.NetworkUtils1;
import ndk.utils_android1.ToastUtils1;

import static ndk.utils_android1.NetworkUtils1.performHttpClientGetTask;
import static ndk.utils_android1.ProgressBarUtils1.showProgress;

public class HttpApiSelectTask extends AsyncTask<Void, Void, String[]> {

    private String url, applicationName;
    private Context context;
    private ProgressBar progressBar;
    private View scrollView;

    private int progressFlag = 0;
    private int responseFlag = 0;
    private int splashFlag = 0;
    private boolean backgroundFlag = false;

    private boolean errorFlag = true;
    private AsyncResponseJsonArray asyncResponseJSONArray = null;
    private AsyncResponse asyncResponse = null;
    private AsyncResponseJSONObject asyncResponseJSONObject = null;

    public HttpApiSelectTask(String url, Context context, ProgressBar progressBar, View scrollView, String applicationName, AsyncResponseJsonArray asyncResponseJSONArray
    ) {
        this.url = url;
        this.context = context;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.asyncResponseJSONArray = asyncResponseJSONArray;
    }

    HttpApiSelectTask(String url, Context context, ProgressBar progressBar, View scrollView, String applicationName, AsyncResponse asyncResponse) {

        this.url = url;
        this.context = context;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.asyncResponse = asyncResponse;
        responseFlag = 1;
    }

    HttpApiSelectTask(String url, Context context, ProgressBar progressBar, View scrollView, String applicationName, AsyncResponseJSONObject asyncResponseJSONObject) {

        this.url = url;
        this.context = context;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.applicationName = applicationName;
        this.asyncResponseJSONObject = asyncResponseJSONObject;
        responseFlag = 2;
    }

    public HttpApiSelectTask(String url, Context context, String applicationName, AsyncResponse asyncResponse) {

        this.url = url;
        this.context = context;
        this.applicationName = applicationName;
        this.asyncResponse = asyncResponse;
        progressFlag = 1;
        responseFlag = 1;
    }

    public HttpApiSelectTask(String url, Context context, String applicationName, AsyncResponseJsonArray asyncResponseJsonArray) {

        this.url = url;
        this.context = context;
        this.applicationName = applicationName;
        this.asyncResponseJSONArray = asyncResponseJsonArray;
        progressFlag = 1;
        splashFlag = 1;
    }

    public HttpApiSelectTask(String url, Context context, ProgressBar progressBar, View scrollView, String applicationName, AsyncResponseJsonArray asyncResponseJSONArray, boolean errorFlag) {

        this.url = url;
        this.context = context;
        this.applicationName = applicationName;
        this.asyncResponseJSONArray = asyncResponseJSONArray;
        this.progressBar = progressBar;
        this.scrollView = scrollView;
        this.errorFlag = errorFlag;
    }

    public HttpApiSelectTask(String url, Context context, String applicationName, AsyncResponseJsonArray asyncResponseJSONArray, boolean errorFlag) {

        this.url = url;
        this.context = context;
        this.applicationName = applicationName;
        this.asyncResponseJSONArray = asyncResponseJSONArray;
        this.progressFlag = 1;
        this.errorFlag = errorFlag;
    }

    public HttpApiSelectTask(String url, Context context, String applicationName, AsyncResponseJsonArray asyncResponseJSONArray, boolean errorFlag, boolean backgroundFlag) {

        this.url = url;
        this.context = context;
        this.applicationName = applicationName;
        this.asyncResponseJSONArray = asyncResponseJSONArray;
        this.progressFlag = 1;
        this.errorFlag = errorFlag;
        this.backgroundFlag = backgroundFlag;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        LogUtils1.debugOnGui(applicationName, "URL : " + url, context);
        return performHttpClientGetTask(url);
    }

    @Override
    protected void onPostExecute(final String[] networkActionResponseArray) {

        LogUtils1.debugOnGui(applicationName, "Network Action status is " + networkActionResponseArray[0], context);
        LogUtils1.debugOnGui(applicationName, "Network Action response is " + networkActionResponseArray[1], context);

        if (progressFlag == 0) {

            showProgress(false, context, progressBar, scrollView);
        }

        if (responseFlag == 1) {

            if (networkActionResponseArray[0].equals("1")) {

                NetworkUtils1.displayFriendlyExceptionMessage(context, networkActionResponseArray[1]);
                asyncResponse.processFinish("exception");

            } else {

                asyncResponse.processFinish(networkActionResponseArray[1]);
            }

        } else if (responseFlag == 2) {

            if (networkActionResponseArray[0].equals("1")) {

                NetworkUtils1.displayFriendlyExceptionMessage(context, networkActionResponseArray[1]);

            } else {

                try {

                    JSONObject jsonObject = new JSONObject(networkActionResponseArray[1]);
                    asyncResponseJSONObject.processFinish(jsonObject);

                } catch (JSONException e) {

                    ToastUtils1.errorToast(context, applicationName);
                    LogUtils1.debug(applicationName, "Error : " + e.getLocalizedMessage(), context);
                }
            }

        } else {

            if (networkActionResponseArray[0].equals("1")) {

                if (backgroundFlag) {

                    LogUtils1.debug(applicationName, "Error...", context);

                } else {

                    ToastUtils1.errorToast(context, applicationName);
                }

                if (splashFlag == 1) {

                    ((AppCompatActivity) context).finish();
                }

            } else {

                try {
                    JSONArray jsonArray = new JSONArray(networkActionResponseArray[1]);

                    if ((splashFlag == 1) || (!errorFlag)) {

                        asyncResponseJSONArray.processFinish(jsonArray);

                    } else {

                        JSONObject tempJsonObject = jsonArray.getJSONObject(0);

                        switch (tempJsonObject.getString("status")) {

                            case "2":
                                LogUtils1.debug(applicationName, "No Entries...", context);
                                if (!backgroundFlag) {
                                    ToastUtils1.longToast(context, "No Entries...");
                                }
                                break;

                            case "0":
                                asyncResponseJSONArray.processFinish(jsonArray);
                                break;

                            case "1":
                                LogUtils1.debug(applicationName, "Error : " + tempJsonObject.getString("error"), context);
                                ToastUtils1.errorToast(context, applicationName);
                                break;
                        }
                    }

                } catch (JSONException e) {

                    ExceptionUtils1.handleExceptionOnGui(context, applicationName, e);
                }
            }
        }
    }

    @Override
    protected void onCancelled() {

        if (progressFlag == 0) {

            showProgress(false, context, progressBar, scrollView);
        }
    }

    public interface AsyncResponseJsonArray {

        void processFinish(JSONArray jsonArray);
    }

    public interface AsyncResponse {

        void processFinish(String response);
    }

    public interface AsyncResponseJSONObject {

        void processFinish(JSONObject jsonObject);
    }
}
