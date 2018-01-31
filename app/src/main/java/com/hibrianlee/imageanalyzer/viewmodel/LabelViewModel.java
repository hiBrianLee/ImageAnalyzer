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

import com.hibrianlee.imageanalyzer.model.LabelInfo;

public class LabelViewModel extends BaseObservable {

    private static final float MULTIPLIER_OFFSET = 0.4f;

    private final float initialTextSize;
    private LabelInfo label;

    public LabelViewModel(float initialTextSize) {
        this.initialTextSize = initialTextSize;
    }

    public String getLabel() {
        return label.getLabel();
    }

    public float getTextSize() {
        return initialTextSize * (MULTIPLIER_OFFSET + label.getConfidence());
    }

    public void setLabel(LabelInfo label) {
        this.label = label;
        notifyChange();
    }
}
