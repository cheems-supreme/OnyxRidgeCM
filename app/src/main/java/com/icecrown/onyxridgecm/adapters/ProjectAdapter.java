//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: ProjectAdapter.java
//
// Written by: Raymond O'Neill
//
// Date written: 11/4/2020
//
// Detail: Holds location in storage for the project
//*******************************************************************

package com.icecrown.onyxridgecm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {
    private List<StorageReference> projectList;
    private ProjectAdapterListener listener;

    public class ProjectHolder extends RecyclerView.ViewHolder {
        public TextView projectTextView;

        public ProjectHolder(View v)
        {
            super(v);
            projectTextView = v.findViewById(R.id.dir_tv);
            v.setOnClickListener(v1 -> {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public ProjectAdapter(List<StorageReference> projectList) {
        this.projectList = projectList;
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_project_instance, parent, false);
        return new ProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position)
    {
        String dirName = projectList.get(position).getName();
        holder.projectTextView.setText(dirName);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void setDirList(List<StorageReference> dirList)
    {
        this.projectList = dirList;
        notifyDataSetChanged();
    }

    public interface ProjectAdapterListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ProjectAdapterListener listener)
    {
        this.listener = listener;
    }
}
