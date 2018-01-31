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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hibrianlee.imageanalyzer.R;
import com.hibrianlee.imageanalyzer.databinding.ItemLabelBinding;
import com.hibrianlee.imageanalyzer.event.LabelsReceiveErrorEvent;
import com.hibrianlee.imageanalyzer.event.LabelsReceivedEvent;
import com.hibrianlee.imageanalyzer.model.LabelInfo;
import com.hibrianlee.imageanalyzer.viewmodel.LabelViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class LabelAdapter extends RecyclerView.Adapter<LabelViewHolder> {

    private final ArrayList<LabelInfo> labels;
    private final EventBus eventBus;

    public LabelAdapter(EventBus eventBus) {
        labels = new ArrayList<>();
        this.eventBus = eventBus;
    }

    @Override
    public LabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_label, parent, false);
        TextView textView = itemView.findViewById(R.id.labelView);
        LabelViewModel viewModel = new LabelViewModel(textView.getTextSize());
        ItemLabelBinding binding = ItemLabelBinding.bind(itemView);
        binding.setViewModel(viewModel);
        return new LabelViewHolder(itemView, binding, viewModel);
    }

    @Override
    public void onBindViewHolder(LabelViewHolder holder, int position) {
        holder.setData(labels.get(position));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    void onLabelsReceivedEvent(LabelsReceivedEvent event) {
        eventBus.removeStickyEvent(event);
        labels.clear();
        labels.addAll(event.getLabels());
        notifyDataSetChanged();
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    void onLabelsReceiveError(LabelsReceiveErrorEvent event) {
        eventBus.removeStickyEvent(event);
        labels.clear();
        notifyDataSetChanged();
    }
}
