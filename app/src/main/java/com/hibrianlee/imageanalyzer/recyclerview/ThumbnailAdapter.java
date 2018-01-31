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

package com.hibrianlee.imageanalyzer.recyclerview;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.databinding.ItemThumbnailBinding;
import com.hibrianlee.imageanalyzer.viewmodel.ThumbnailViewModel;

import org.greenrobot.eventbus.EventBus;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailViewHolder> {

    private final EventBus eventBus;

    private Cursor cursor;
    private int thumbnailIndex;
    private int mediaIndex;

    public ThumbnailAdapter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        if (cursor != null) {
            thumbnailIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            mediaIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
        }
        notifyDataSetChanged();
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thumbnail, parent, false);
        ThumbnailViewModel viewModel = new ThumbnailViewModel(eventBus);
        ItemThumbnailBinding binding = ItemThumbnailBinding.bind(itemView);
        binding.setViewModel(viewModel);
        return new ThumbnailViewHolder(itemView, binding, viewModel);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            ViewCompat.setTransitionName(holder.getThumbnailView(),
                    String.valueOf(cursor.getInt(mediaIndex)));
            holder.setData(cursor.getString(thumbnailIndex), cursor.getInt(mediaIndex));
        }
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }
}
