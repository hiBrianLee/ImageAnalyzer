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
import com.hibrianlee.imageanalyzer.api.ImageApi;
import com.hibrianlee.imageanalyzer.event.LabelsReceiveErrorEvent;
import com.hibrianlee.imageanalyzer.event.LabelsReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AnalyzeImageViewModel extends BaseObservable {

    private final String thumbnailPath;
    private final EventBus eventBus;
    private final ImageApi imageApi;

    private String fullImagePath;
    private boolean analysisPending;
    private boolean errorOccurred = false;

    public AnalyzeImageViewModel(String thumbnailPath, EventBus eventBus, ImageApi imageApi) {
        this.thumbnailPath = thumbnailPath;
        this.eventBus = eventBus;
        this.imageApi = imageApi;
    }

    @Bindable
    public String getImagePath() {
        return fullImagePath;
    }

    @Bindable
    public boolean isAnalyzeEnabled() {
        return !analysisPending;
    }

    @Bindable
    public int getProgressBarVisibility() {
        return analysisPending ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getErrorVisibility() {
        return errorOccurred ? View.VISIBLE : View.GONE;
    }

    public void setFullImagePath(String imagePath) {
        this.fullImagePath = imagePath;
        notifyPropertyChanged(BR.imagePath);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onLabelsReceivedEvent(LabelsReceivedEvent event) {
        analysisPending = false;
        errorOccurred = false;
        notifyPropertyChanged(BR.analyzeEnabled);
        notifyPropertyChanged(BR.progressBarVisibility);
        notifyPropertyChanged(BR.errorVisibility);
        eventBus.unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    void onLabelsReceiveError(LabelsReceiveErrorEvent event) {
        analysisPending = false;
        errorOccurred = true;
        notifyPropertyChanged(BR.analyzeEnabled);
        notifyPropertyChanged(BR.progressBarVisibility);
        notifyPropertyChanged(BR.errorVisibility);
        eventBus.unregister(this);
    }

    public void onClickAnalyze() {
        eventBus.register(this);
        imageApi.getLabels(thumbnailPath);
        analysisPending = true;
        errorOccurred = false;
        notifyPropertyChanged(BR.analyzeEnabled);
        notifyPropertyChanged(BR.progressBarVisibility);
        notifyPropertyChanged(BR.errorVisibility);
    }
}
