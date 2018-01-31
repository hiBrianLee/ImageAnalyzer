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

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.databinding.ActivityGalleryBinding;
import com.hibrianlee.imageanalyzer.event.LoadPhotosEvent;
import com.hibrianlee.imageanalyzer.event.OpenAppSettingEvent;
import com.hibrianlee.imageanalyzer.event.RequestStoragePermissionEvent;
import com.hibrianlee.imageanalyzer.event.ViewImageEvent;
import com.hibrianlee.imageanalyzer.permission.PermissionManager;
import com.hibrianlee.imageanalyzer.recyclerview.ThumbnailAdapter;
import com.hibrianlee.imageanalyzer.recyclerview.GridPaddingDecoration;
import com.hibrianlee.imageanalyzer.viewmodel.GalleryViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class GalleryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;

    private EventBus eventBus;
    private GalleryViewModel viewModel;
    private ThumbnailAdapter thumbnailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventBus = new EventBus();
        viewModel = new GalleryViewModel(eventBus);
        ActivityGalleryBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_gallery);
        binding.setViewModel(viewModel);

        thumbnailAdapter = new ThumbnailAdapter(eventBus);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(new GridPaddingDecoration(getResources(),
                R.dimen.galleryGridPadding, R.integer.galleryGridSpan));
        recyclerView.setAdapter(thumbnailAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        viewModel.setStoragePermissionStatus(PermissionManager.getPermissionStatus(this,
                Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                viewModel.setStoragePermissionStatus(PermissionManager.getPermissionStatus(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE));
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Thumbnails.IMAGE_ID};
        return new CursorLoader(this, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, null, null, MediaStore.Images.Thumbnails.IMAGE_ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            thumbnailAdapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        thumbnailAdapter.setCursor(null);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onRequestStoragePermissionEvent(RequestStoragePermissionEvent event) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_EXTERNAL_STORAGE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onOpenAppSettingEvent(OpenAppSettingEvent event) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getApplicationContext().getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onLoadPhotosEvent(LoadPhotosEvent event) {
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onViewImageEvent(ViewImageEvent event) {
        Intent intent = new Intent(this, AnalyzeImageActivity.class);
        intent.putExtra(AnalyzeImageActivity.EXTRA_THUMBNAIL_PATH, event.getThumbnailPath());
        intent.putExtra(AnalyzeImageActivity.EXTRA_FULL_IMAGE_ID, event.getFullImageId());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, event.getView(), event.getFullImageId());
        startActivity(intent, options.toBundle());
    }
}
