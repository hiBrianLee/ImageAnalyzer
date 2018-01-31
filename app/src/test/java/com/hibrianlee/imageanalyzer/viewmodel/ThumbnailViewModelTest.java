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

import com.hibrianlee.imageanalyzer.event.ViewImageEvent;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThumbnailViewModelTest {

    private static final String THUMBNAIL_PATH = "thumbnail path";
    private static final int IMAGE_ID = 123;
    private static final String EXPECTED_IMAGED_ID = "123";

    @Mock
    private EventBus eventBus;

    @Captor
    private ArgumentCaptor<ViewImageEvent> eventCaptor;

    private ThumbnailViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new ThumbnailViewModel(eventBus);
    }

    @Test
    public void testImagePath() {
        viewModel.setThumbnailPath(THUMBNAIL_PATH);
        assertEquals(THUMBNAIL_PATH, viewModel.getImagePath());
    }

    @Test
    public void testOnClickImage() {
        View view = mock(View.class);
        viewModel.setThumbnailPath(THUMBNAIL_PATH);
        viewModel.setFullImageId(IMAGE_ID);
        viewModel.onClickImage(view);

        verify(eventBus).post(eventCaptor.capture());
        ViewImageEvent event = eventCaptor.getValue();
        assertNotNull(event);
        assertEquals(view, event.getView());
        assertEquals(EXPECTED_IMAGED_ID, event.getFullImageId());
        assertEquals(THUMBNAIL_PATH, event.getThumbnailPath());
    }
}