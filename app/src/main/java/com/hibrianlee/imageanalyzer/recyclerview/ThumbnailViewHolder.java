/*
 * Copyright (C) 2018 Brian Lee (@hiBrianLee)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hibrianlee.imageanalyzer.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.viewmodel.ThumbnailViewModel;

class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;
    private final ThumbnailViewModel viewModel;
    private final View thumbnailView;

    ThumbnailViewHolder(View itemView, ViewDataBinding binding,
                        ThumbnailViewModel viewModel) {
        super(itemView);
        thumbnailView = itemView.findViewById(R.id.thumbnailView);
        this.binding = binding;
        this.viewModel = viewModel;
    }

    View getThumbnailView() {
        return thumbnailView;
    }

    void setData(String path, int imageId) {
        viewModel.setThumbnailPath(path);
        viewModel.setFullImageId(imageId);
        binding.executePendingBindings();
    }
}
