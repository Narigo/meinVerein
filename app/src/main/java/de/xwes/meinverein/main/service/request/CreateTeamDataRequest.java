package de.xwes.meinverein.main.service.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.xwes.meinverein.main.view.OverviewActivity;
import de.xwes.meinverein.main.view.SearchResultsActivity;

/**
 * Created by Xaver on 20.05.2015.
 */
public class CreateTeamDataRequest extends AsyncTask<String, Void, JSONArray>
{
    static InputStream is = null;
    static JSONArray jsonArray;
    static String json = "";
    Context context;
    ProgressDialog progressDialog;

    public CreateTeamDataRequest(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected JSONArray doInBackground(String... params)
    {
        try {
            StringBuilder url_request = new StringBuilder(SearchRequest.BASE_URL + "create_data.php?team=");
            url_request.append(params[0]);
            url_request.append("&link=");
            url_request.append(params[1]);

            Log.i("url", url_request.toString());
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_request.toString());
            HttpResponse getResponse = httpClient.execute(httpGet);

            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url_request.toString());
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            is = getResponseEntity.getContent();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray)
    {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        Intent overview = new Intent(context, OverviewActivity.class);
        overview.putExtra("json", jsonArray.toString());
        context.startActivity(overview);
    }
}
