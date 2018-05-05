package com.example.httprequesttest;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Rushikesh Jogdand.
 */
public class CallApi extends AsyncTask<String, String, String> {
    private CallApiClient client;
    private String res;

    public CallApi(final CallApiClient client) {
        this.client = client;
        res = "";
    }

    @Override
    protected String doInBackground(final String... strings) {
        String urlString = strings[0];
        try {
            URL                url                = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            try {
                InputStream  in     = new BufferedInputStream(httpsURLConnection.getInputStream());
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, "UTF-8");
                res += writer.toString();
            } finally {
                httpsURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(final String s) {
        client.handleResponse(s);
    }
}
