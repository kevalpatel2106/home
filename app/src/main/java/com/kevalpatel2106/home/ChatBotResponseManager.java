package com.kevalpatel2106.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.responsePojo.base.Status;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by Keval on 25-Apr-17.
 */

public class ChatBotResponseManager {
    private static final String TAG = ChatBotResponseManager.class.getSimpleName();

    private static final String INTENT_WEB_SEARCH = "web.search";
    private static final String INTENT_INIT_A2DP = "init.a2dp";

    public static void manageResponse(@NonNull final Context context,
                                      @NonNull AIResponse aiResponse) {
        final Result result = aiResponse.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        Log.d("Query:", result.getFulfillment().getSpeech()
                + "\nAction: " + result.getAction()
                + "\nParameters: " + parameterString);

        switch (result.getAction()) {
            case INTENT_WEB_SEARCH:
                try {
                    Thread.sleep(400);
                    String searchUrl = "https://www.google.com/search?q="
                            + URLEncoder.encode(result.getParameters().get("q").toString().replace("\"", ""), "UTF-8");

                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(searchUrl));
                    context.startActivity(intent);
                } catch (UnsupportedEncodingException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case INTENT_INIT_A2DP:
                RetrofitUtils.subscribe(RetrofitUtils.getApiService().initA2DP(RetrofitUtils.getAuthString(context)),
                        new APIObserver<Status>() {
                            @Override
                            public void onError(String errorMessage, int statusCode) {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, errorMessage);
                            }

                            @Override
                            public void onSuccess(Status status) {
                                //Do nothing
                            }
                        });
                break;
        }
    }
}