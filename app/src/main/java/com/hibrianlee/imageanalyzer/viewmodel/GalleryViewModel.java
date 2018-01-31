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
import android.support.annotation.StringRes;
import android.view.View;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.event.LoadPhotosEvent;
import com.hibrianlee.imageanalyzer.event.OpenAppSettingEvent;
import com.hibrianlee.imageanalyzer.event.RequestStoragePermissionEvent;
import com.hibrianlee.imageanalyzer.permission.PermissionStatus;

import org.greenrobot.eventbus.EventBus;

public class GalleryViewModel extends BaseObservable {

    private final EventBus eventBus;
    private PermissionStatus storagePermissionStatus;

    public GalleryViewModel(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Bindable
    public int getGalleryVisibility() {
        return storagePermissionStatus == PermissionStatus.PERMISSION_GRANTED ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getPermissionVisibility() {
        return storagePermissionStatus == PermissionStatus.PERMISSION_GRANTED ? View.GONE : View.VISIBLE;
    }

    @Bindable
    @StringRes
    public int getPermissionRequestText() {
        return storagePermissionStatus == PermissionStatus.CAN_ASK_PERMISSION
                ? R.string.permission_request: R.string.permission_denied;
    }

    @Bindable
    @StringRes
    public int getPermissionButtonText() {
        return storagePermissionStatus == PermissionStatus.CAN_ASK_PERMISSION
                ? R.string.grant_access : R.string.open_app_setting;
    }

    public void setStoragePermissionStatus(PermissionStatus newPermissionStatus) {
        if (storagePermissionStatus != newPermissionStatus) {
            storagePermissionStatus = newPermissionStatus;
            if (storagePermissionStatus == PermissionStatus.PERMISSION_GRANTED) {
                eventBus.post(new LoadPhotosEvent());
            }
            notifyChange();
        }
    }

    public void onClickPermission() {
        if (storagePermissionStatus == PermissionStatus.CAN_ASK_PERMISSION) {
            eventBus.post(new RequestStoragePermissionEvent());
        } else if (storagePermissionStatus == PermissionStatus.PERMISSION_DENIED) {
            eventBus.post(new OpenAppSettingEvent());
        }
    }
}
