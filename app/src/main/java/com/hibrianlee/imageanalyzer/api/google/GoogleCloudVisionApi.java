/*
 * Copyright (C) 2018 Brian Lee (@hiBrianLee)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hibrianlee.imageanalyzer.api.google;

import android.content.Context;

import com.hibrianlee.imageanalyzer.BuildConfig;
import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.api.ImageApi;
import com.hibrianlee.imageanalyzer.event.LabelsReceiveErrorEvent;
import com.hibrianlee.imageanalyzer.event.LabelsReceivedEvent;
import com.hibrianlee.imageanalyzer.model.LabelInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GoogleCloudVisionApi extends ImageApi {

    private final EventBus eventBus;
    private final GoogleApiService api;
    private final String apiKey;

    public GoogleCloudVisionApi(Context context, EventBus eventBus) {
        this.eventBus = eventBus;
        apiKey = context.getString(R.string.google_cloud_vision_api_key);
        String url = context.getString(R.string.google_cloud_vision_api_url);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder = builder.client(client);
        }
        api = builder.build().create(GoogleApiService.class);
    }

    @Override
    public void getLabels(String imagePath) {
        GoogleRequest request = new GoogleRequest.Builder()
                .setImage(new GoogleImage(getEncodedImage(imagePath)))
                .addFeature(new GoogleFeature(GoogleFeatureType.LABEL_DETECTION, 20))
                .build();

        GoogleRequestBody requestBody = new GoogleRequestBody(request);
        api.annotateImage(apiKey, requestBody).enqueue(new ResponseCallback(eventBus));
    }

    private static class ResponseCallback implements Callback<GoogleResponseBody> {

        private final EventBus eventBus;

        ResponseCallback(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        @Override
        public void onResponse(Call<GoogleResponseBody> call, Response<GoogleResponseBody> response) {
            GoogleResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                ArrayList<GoogleLabelAnnotation> annotations = responseBody
                        .getResponses().get(0).getLabelAnnotations();
                ArrayList<LabelInfo> labels = new ArrayList<>();
                for (GoogleLabelAnnotation annotation : annotations) {
                    labels.add(new LabelInfo(annotation.getDescription(), annotation.getScore()));
                }
                eventBus.postSticky(new LabelsReceivedEvent(labels));
            } else {
                eventBus.postSticky(new LabelsReceiveErrorEvent());
            }
        }

        @Override
        public void onFailure(Call<GoogleResponseBody> call, Throwable t) {
            t.printStackTrace();
            eventBus.postSticky(new LabelsReceiveErrorEvent());
        }
    }
}
