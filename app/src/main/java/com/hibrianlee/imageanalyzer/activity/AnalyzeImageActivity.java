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

package com.hibrianlee.imageanalyzer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.api.ImageApi;
import com.hibrianlee.imageanalyzer.api.google.GoogleCloudVisionApi;
import com.hibrianlee.imageanalyzer.databinding.ActivityAnalyzeImageBinding;
import com.hibrianlee.imageanalyzer.recyclerview.LabelAdapter;
import com.hibrianlee.imageanalyzer.viewmodel.AnalyzeImageViewModel;

import org.greenrobot.eventbus.EventBus;

public class AnalyzeImageActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String EXTRA_THUMBNAIL_PATH = "thumbnailPath";
    static final String EXTRA_FULL_IMAGE_ID = "fullImageId";

    private EventBus eventBus;
    private LabelAdapter labelAdapter;
    private AnalyzeImageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = new EventBus();

        getSupportLoaderManager().initLoader(0, null, this);

        ImageApi imageApi = new GoogleCloudVisionApi(this, eventBus);
        String thumbnailPath = getIntent().getStringExtra(EXTRA_THUMBNAIL_PATH);
        viewModel = new AnalyzeImageViewModel(thumbnailPath, eventBus, imageApi);
        ActivityAnalyzeImageBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_analyze_image);
        binding.setViewModel(viewModel);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setTransitionName(getIntent().getStringExtra(EXTRA_FULL_IMAGE_ID));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        labelAdapter = new LabelAdapter(eventBus);
        recyclerView.setAdapter(labelAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.FLEX_END);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(labelAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(labelAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getIntent();
        String imageId = intent.getStringExtra(EXTRA_FULL_IMAGE_ID);
        String[] projection = {MediaStore.Images.Media.DATA};
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, MediaStore.Images.Media._ID + " = ? ",
                new String[]{imageId}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String imagePath = data.getString(0);
            Log.d(this.getLocalClassName(), "imagePath: " + imagePath);
            viewModel.setFullImagePath(imagePath);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
