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

import android.view.View;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.event.LoadPhotosEvent;
import com.hibrianlee.imageanalyzer.event.OpenAppSettingEvent;
import com.hibrianlee.imageanalyzer.event.RequestStoragePermissionEvent;
import com.hibrianlee.imageanalyzer.permission.PermissionStatus;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class GalleryViewModelTest {

    @Mock
    private EventBus eventBus;

    private GalleryViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new GalleryViewModel(eventBus);
    }

    @Test
    public void testCanAskPermission() {
        viewModel.setStoragePermissionStatus(PermissionStatus.CAN_ASK_PERMISSION);

        assertEquals(View.GONE, viewModel.getGalleryVisibility());
        assertEquals(View.VISIBLE, viewModel.getPermissionVisibility());
        assertEquals(R.string.permission_request, viewModel.getPermissionRequestText());
        assertEquals(R.string.grant_access, viewModel.getPermissionButtonText());
        verify(eventBus, never()).post(any(LoadPhotosEvent.class));
    }

    @Test
    public void testPermissionGranted() {
        viewModel.setStoragePermissionStatus(PermissionStatus.PERMISSION_GRANTED);

        assertEquals(View.VISIBLE, viewModel.getGalleryVisibility());
        assertEquals(View.GONE, viewModel.getPermissionVisibility());
        verify(eventBus).post(any(LoadPhotosEvent.class));
    }

    @Test
    public void testPermissionDenied() {
        viewModel.setStoragePermissionStatus(PermissionStatus.PERMISSION_DENIED);

        assertEquals(View.GONE, viewModel.getGalleryVisibility());
        assertEquals(View.VISIBLE, viewModel.getPermissionVisibility());
        assertEquals(R.string.permission_denied, viewModel.getPermissionRequestText());
        assertEquals(R.string.open_app_setting, viewModel.getPermissionButtonText());
        verify(eventBus, never()).post(any(LoadPhotosEvent.class));
    }

    @Test
    public void onClickPermissionRequestStoragePermission() {
        viewModel.setStoragePermissionStatus(PermissionStatus.CAN_ASK_PERMISSION);
        viewModel.onClickPermission();

        verify(eventBus).post(any(RequestStoragePermissionEvent.class));
    }

    @Test
    public void onClickPermissionOpenAppSetting() {
        viewModel.setStoragePermissionStatus(PermissionStatus.PERMISSION_DENIED);
        viewModel.onClickPermission();

        verify(eventBus).post(any(OpenAppSettingEvent.class));
    }
}