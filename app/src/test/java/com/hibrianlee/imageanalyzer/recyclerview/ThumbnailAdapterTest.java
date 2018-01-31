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

import android.database.Cursor;
import android.provider.MediaStore;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThumbnailAdapterTest {

    private static final int THUMBNAIL_INDEX = 0;
    private static final int MEDIA_INDEX = 1;
    private static final String THUMBNAIL_PATH = "thumbnail path";
    private static final int IMAGE_ID = 123;

    @Mock
    private Cursor cursor;

    @Mock
    private ThumbnailViewHolder holder;

    private ThumbnailAdapter adapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ThumbnailAdapter realAdapter = new ThumbnailAdapter(mock(EventBus.class));
        adapter = spy(realAdapter);
        doNothing().when(adapter).notifyDataSetChanged();
    }

    @Test
    public void testNullCursor() {
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void testUpdatedCursor() {
        when(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA)).thenReturn(THUMBNAIL_INDEX);
        when(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID)).thenReturn(MEDIA_INDEX);
        when(cursor.getString(THUMBNAIL_INDEX)).thenReturn(THUMBNAIL_PATH);
        when(cursor.getInt(MEDIA_INDEX)).thenReturn(IMAGE_ID);

        adapter.setCursor(cursor);
        verify(adapter).notifyDataSetChanged();

        adapter.onBindViewHolder(holder, 1);
        verify(cursor).moveToPosition(1);
        verify(holder).setData(THUMBNAIL_PATH, IMAGE_ID);
    }
}