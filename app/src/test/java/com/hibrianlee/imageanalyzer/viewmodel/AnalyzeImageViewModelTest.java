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

package com.hibrianlee.imageanalyzer.viewmodel;

import android.view.View;

import com.hibrianlee.imageanalyzer.api.ImageApi;
import com.hibrianlee.imageanalyzer.event.LabelsReceiveErrorEvent;
import com.hibrianlee.imageanalyzer.event.LabelsReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnalyzeImageViewModelTest {

    private static final String THUMBNAIL_PATH = "thumbnail path";
    private static final String FULL_IMAGE_PATH = "full image path";

    @Mock
    private EventBus eventBus;

    @Mock
    private ImageApi imageApi;

    private AnalyzeImageViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new AnalyzeImageViewModel(THUMBNAIL_PATH, eventBus, imageApi);
    }

    @Test
    public void testImageLoaded() {
        viewModel.setFullImagePath(FULL_IMAGE_PATH);

        assertEquals(FULL_IMAGE_PATH, viewModel.getImagePath());
        assertTrue(viewModel.isAnalyzeEnabled());
        assertEquals(View.GONE, viewModel.getProgressBarVisibility());
        assertEquals(View.GONE, viewModel.getErrorVisibility());
    }

    @Test
    public void testOnClickAnalyze() {
        viewModel.onClickAnalyze();

        assertFalse(viewModel.isAnalyzeEnabled());
        assertEquals(View.VISIBLE, viewModel.getProgressBarVisibility());
        assertEquals(View.GONE, viewModel.getErrorVisibility());
        verify(eventBus).register(viewModel);
        verify(imageApi).getLabels(THUMBNAIL_PATH);
    }

    @Test
    public void testLabelsReceived() {
        viewModel.onLabelsReceivedEvent(mock(LabelsReceivedEvent.class));

        assertTrue(viewModel.isAnalyzeEnabled());
        assertEquals(View.GONE, viewModel.getProgressBarVisibility());
        assertEquals(View.GONE, viewModel.getErrorVisibility());
        verify(eventBus).unregister(viewModel);
    }

    @Test
    public void testLabelsReceiveError() {
        viewModel.onLabelsReceiveError(mock(LabelsReceiveErrorEvent.class));

        assertTrue(viewModel.isAnalyzeEnabled());
        assertEquals(View.GONE, viewModel.getProgressBarVisibility());
        assertEquals(View.VISIBLE, viewModel.getErrorVisibility());
        verify(eventBus).unregister(viewModel);
    }
}