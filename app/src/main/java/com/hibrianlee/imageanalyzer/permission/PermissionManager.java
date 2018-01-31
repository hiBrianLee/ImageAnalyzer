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

package com.hibrianlee.imageanalyzer.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    public static PermissionStatus getPermissionStatus(@NonNull Activity activity,
                                                       @NonNull String permission) {
        if (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(activity, permission)) {
            return PermissionStatus.PERMISSION_GRANTED;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission)){
            return PermissionStatus.CAN_ASK_PERMISSION;
        } else {
            return PermissionStatus.PERMISSION_DENIED;
        }
    }
}
