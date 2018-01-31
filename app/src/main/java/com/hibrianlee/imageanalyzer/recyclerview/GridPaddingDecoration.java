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

package com.hibrianlee.imageanalyzer.recyclerview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridPaddingDecoration extends RecyclerView.ItemDecoration {

    private final int padding;
    private final int numColumns;

    public GridPaddingDecoration(Resources resources,
                                 @DimenRes int gridPaddingResId,
                                 @IntegerRes int gridSpanResId) {
        padding = resources.getDimensionPixelSize(gridPaddingResId);
        numColumns = resources.getInteger(gridSpanResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.top = padding;
        if (position % numColumns == 0) {
            outRect.left = padding;
        } else {
            outRect.left = 0;
        }
        outRect.right = padding;
    }
}
