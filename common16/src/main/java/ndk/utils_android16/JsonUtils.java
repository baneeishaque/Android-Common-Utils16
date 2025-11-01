package ndk.utils_android16;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import ndk.utils_android1.ErrorUtils;
import ndk.utils_android1.ExceptionUtils1;
import ndk.utils_android1.LogUtils1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtils {

    private static List<JSONObject> sortJsonObjectListByDateField(List<JSONObject> jsonObjectList, SimpleDateFormat desiredDateFormat, String keyField) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            jsonObjectList.sort((firstJsonObject, secondJsonObject) -> {

                int compare = 0;

                try {

                    Date keyA = desiredDateFormat.parse(firstJsonObject.getString(keyField));
                    Date keyB = desiredDateFormat.parse(secondJsonObject.getString(keyField));
                    compare = Objects.requireNonNull(keyA).compareTo(keyB);

                } catch (JSONException | ParseException e) {

                    Log.e("JsonUtils", "JsonUtils.sortJsonObjectListByDateField - jsonObjectList = " + jsonObjectList + ", desiredDateFormat = " + desiredDateFormat + ", keyField = " + keyField);
                }
                return compare;
            });
        }

        return jsonObjectList;
    }

    public static JSONArray sortJsonArrayInStringByUkLocaleDateField(String jsonArray, String desiredDateFormatInUkLocale, String keyField, String applicationName, Context currentApplicationContext) {

        return sortJsonArrayInStringByDateInSimpleDateFormatField(jsonArray, new SimpleDateFormat(desiredDateFormatInUkLocale, Locale.UK), keyField, applicationName, currentApplicationContext);
    }

    public static JSONArray sortJsonArrayInStringByLocalizedDateField(String jsonArray, String desiredDateFormat, Locale locale, String keyField, String applicationName, Context currentApplicationContext) {

        return sortJsonArrayInStringByDateInSimpleDateFormatField(jsonArray, new SimpleDateFormat(desiredDateFormat, locale), keyField, applicationName, currentApplicationContext);
    }

    public static JSONArray sortJsonArrayInStringByDateInSimpleDateFormatField(String JSON_array, SimpleDateFormat desired_date_format, String key_field, String applicationName, Context currentApplicationContext) {

        return JSON_object_list_to_JSON_array(sortJsonObjectListByDateField(jsonArrayInStringToJsonObjectList(JSON_array, applicationName, currentApplicationContext), desired_date_format, key_field));
    }

    public static JSONArray sortJsonArrayByDateInSimpleDateFormatField(JSONArray JSON_array, SimpleDateFormat desired_date_format, String key_field, String applicationName, Context currentApplicationContext) {

        return JSON_object_list_to_JSON_array(sortJsonObjectListByDateField(jsonArrayToJsonObjectList(JSON_array, applicationName, currentApplicationContext), desired_date_format, key_field));
    }

    private static List<JSONObject> jsonArrayInStringToJsonObjectList(String jsonArray, String applicationName, Context currentApplicationContext) {

        try {

            return jsonArrayToJsonObjectList(new JSONArray(jsonArray), applicationName, currentApplicationContext);

        } catch (JSONException e) {

            LogUtils1.debug(applicationName, ExceptionUtils1.getExceptionDetails(e), currentApplicationContext);
            return jsonArrayToJsonObjectList(new JSONArray(), applicationName, currentApplicationContext);
        }
    }

    private static List<JSONObject> jsonArrayToJsonObjectList(JSONArray jsonArray, String applicationName, Context currentApplicationContext) {

        List<JSONObject> JSON_object_list = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSON_object_list.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {

            LogUtils1.debug(applicationName, ExceptionUtils1.getExceptionDetails(e), currentApplicationContext);
        }
        return JSON_object_list;
    }

    private static JSONArray JSON_object_list_to_JSON_array(List<JSONObject> JSON_object_list) {

        JSONArray JSON_array = new JSONArray();
        for (JSONObject json_object : JSON_object_list) {

            JSON_array.put(json_object);
        }
        return JSON_array;
    }

    public static void print_json_array(JSONArray JSON_array, String applicationName, Context currentApplicationContext) {

        try {
            for (int i = 0; i < JSON_array.length(); i++) {

                JSONObject innerObj = JSON_array.getJSONObject(i);

                for (Iterator<String> it = innerObj.keys(); it.hasNext(); ) {

                    String key = it.next();
                    System.out.println(key + ":" + innerObj.get(key));
                }
                System.out.println("---------------------------------");
            }
        } catch (JSONException e) {

            LogUtils1.debug(applicationName, ExceptionUtils1.getExceptionDetails(e), currentApplicationContext);
        }

    }

    private static List<JSONObject> sort_JSON_array_by_integer_field(List<JSONObject> JSON_array_list, String key_field) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            JSON_array_list.sort((first_json_object, second_json_object) -> {

                int compare = 0;

                try {
                    int first_json_object_key_value = first_json_object.getInt(key_field);
                    int second_json_object_key_value = second_json_object.getInt(key_field);
                    compare = Integer.compare(first_json_object_key_value, second_json_object_key_value);

                } catch (JSONException e) {

                    Log.e("JsonUtils", "JsonUtils.sort_JSON_array_by_integer_field - JSON_array_list = " + JSON_array_list + ", key_field = " + key_field);
                }
                return compare;
            });
        }

        return JSON_array_list;
    }

    public static JSONArray sort_JSON_array_by_integer_field(String JSON_array, String key_field, String applicationName, Context currentApplicationContext) {

        return JSON_object_list_to_JSON_array(sort_JSON_array_by_integer_field(jsonArrayInStringToJsonObjectList(JSON_array, applicationName, currentApplicationContext), key_field));
    }

    public static void JSON_array_to_array_list(JSONArray jsonArray, ArrayList<Object> arrayList, int start_position, Object_Utils.IGet_object iGet_object, Context context, String applicationName) {

        for (int i = start_position; i < jsonArray.length(); i++) {

            try {

                arrayList.add(iGet_object.get_object(jsonArray.getJSONObject(i)));

            } catch (JSONException e) {

                ExceptionUtils1.handleExceptionOnGui(context, applicationName, e);
            }
        }
    }
}
