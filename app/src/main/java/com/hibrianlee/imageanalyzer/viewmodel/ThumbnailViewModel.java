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

package com.hibrianlee.imageanalyzer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.hibrianlee.imageanalyzer.BR;
import com.hibrianlee.imageanalyzer.event.ViewImageEvent;

import org.greenrobot.eventbus.EventBus;

public class ThumbnailViewModel extends BaseObservable {

    private final EventBus eventBus;

    private String thumbnailPath;
    private int fullImageId;

    public ThumbnailViewModel(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Bindable
    public String getImagePath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String path) {
        thumbnailPath = path;
        notifyPropertyChanged(BR.imagePath);
    }

    public void setFullImageId(int fullImageId) {
        this.fullImageId = fullImageId;
    }

    public void onClickImage(View view) {
        eventBus.post(new ViewImageEvent(thumbnailPath, String.valueOf(fullImageId), view));
    }
}
