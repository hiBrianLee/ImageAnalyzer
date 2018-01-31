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

import com.hibrianlee.imageanalyzer.event.LabelsReceiveErrorEvent;
import com.hibrianlee.imageanalyzer.event.LabelsReceivedEvent;
import com.hibrianlee.imageanalyzer.model.LabelInfo;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LabelAdapterTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private LabelsReceivedEvent labelsReceivedEvent;

    @Mock
    private LabelsReceiveErrorEvent labelsReceiveErrorEvent;

    @Mock
    private LabelInfo labelInfo0, labelInfo1;

    @Mock
    private LabelViewHolder holder;

    private LabelAdapter adapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        LabelAdapter realAdapter = new LabelAdapter(eventBus);
        adapter = spy(realAdapter);
        doNothing().when(adapter).notifyDataSetChanged();
    }

    @Test
    public void testLabelsReceived() {
        ArrayList<LabelInfo> labels = new ArrayList<>();
        labels.add(labelInfo0);
        labels.add(labelInfo1);
        when(labelsReceivedEvent.getLabels()).thenReturn(labels);

        adapter.onLabelsReceivedEvent(labelsReceivedEvent);
        verify(eventBus).removeStickyEvent(labelsReceivedEvent);
        verify(adapter).notifyDataSetChanged();
        assertEquals(2, adapter.getItemCount());

        adapter.onBindViewHolder(holder, 1);
        verify(holder).setData(labelInfo1);
    }

    @Test
    public void testLabelsError() {
        adapter.onLabelsReceiveError(labelsReceiveErrorEvent);
        verify(eventBus).removeStickyEvent(labelsReceiveErrorEvent);
        verify(adapter).notifyDataSetChanged();
        assertEquals(0, adapter.getItemCount());
    }
}