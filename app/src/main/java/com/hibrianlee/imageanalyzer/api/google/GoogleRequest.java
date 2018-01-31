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

package com.hibrianlee.imageanalyzer.api.google;

import android.support.annotation.NonNull;

import com.hibrianlee.imageanalyzer.api.JsonModel;

import java.util.ArrayList;

class GoogleRequest extends JsonModel {

    private final GoogleImage image;
    private final ArrayList<GoogleFeature> features;

    private GoogleRequest(GoogleImage image, ArrayList<GoogleFeature> features) {
        this.image = image;
        this.features = features;
    }

    static class Builder {

        private GoogleImage image;
        private ArrayList<GoogleFeature> features;

        Builder() {
            features = new ArrayList<>();
        }

        Builder addFeature(@NonNull GoogleFeature feature) {
            features.add(feature);
            return this;
        }

        Builder setImage(@NonNull GoogleImage image) {
            this.image = image;
            return this;
        }

        GoogleRequest build() {
            return new GoogleRequest(image, features);
        }
    }
}
