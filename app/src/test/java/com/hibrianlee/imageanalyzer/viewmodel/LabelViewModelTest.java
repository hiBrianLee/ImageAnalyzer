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

import com.hibrianlee.imageanalyzer.model.LabelInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class LabelViewModelTest {

    private static final float EXPECTED_MULTIPLIER = 0.4f;
    private static final float INITIAL_TEXT_SIZE = 100f;

    @Mock
    private LabelInfo labelInfo;

    private LabelViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new LabelViewModel(INITIAL_TEXT_SIZE);
    }

    @Test
    public void testLowConfidence() {
        String label = "small label";
        float confidence = 0.3f;
        float expectedTextSize = INITIAL_TEXT_SIZE * (EXPECTED_MULTIPLIER + confidence);

        when(labelInfo.getLabel()).thenReturn(label);
        when(labelInfo.getConfidence()).thenReturn(confidence);
        viewModel.setLabel(labelInfo);

        assertEquals(label, viewModel.getLabel());
        assertEquals(0, Float.compare(expectedTextSize, viewModel.getTextSize()));
    }

    @Test
    public void testHighConfidence() {
        String label = "big label";
        float confidence = 0.9f;
        float expectedTextSize = INITIAL_TEXT_SIZE * (EXPECTED_MULTIPLIER + confidence);

        when(labelInfo.getLabel()).thenReturn(label);
        when(labelInfo.getConfidence()).thenReturn(confidence);
        viewModel.setLabel(labelInfo);

        assertEquals(label, viewModel.getLabel());
        assertEquals(0, Float.compare(expectedTextSize, viewModel.getTextSize()));
    }
}