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

package com.hibrianlee.imageanalyzer.event;

import com.hibrianlee.imageanalyzer.model.LabelInfo;

import java.util.ArrayList;

public class LabelsReceivedEvent {

    private final ArrayList<LabelInfo> labels;

    public LabelsReceivedEvent(ArrayList<LabelInfo> labels) {
        this.labels = labels;
    }

    public ArrayList<LabelInfo> getLabels() {
        return labels;
    }
}
