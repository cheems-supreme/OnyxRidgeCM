//*******************************************************************
// Project: BCS430W - Senior Project
//
// Project name: OnyxRidge
//
// File: Adapter_Documents.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/2/20
//
// Detail: Ooh, boy, part 2. Used for the RecyclerView to display the
//         documents available to the user after selecting a specific
//         directory. When on clicks on the file, it will launch
//         another activity (maybe a popup instead) that will give a
//         preview of the report, and give options to save to local
//         device.
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 10/8/20
// - R.O.
// - DETAILS:
//      - Actually fleshed out the class, gave it inner class, interface,
//        methods, and member variables.
// ------------------------------------------------
// - 10/19/20
// - R.O.
// - DETAILS:
//      - Attempted to have a context menu appear when list item is
//        clicked. Removed all code involving that in the class.
//      - Refactored name to the current version (from DocumentsAdapter.java)
//        to improve readability
//*******************************************************************
package com.icecrown.onyxridgecm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.Document;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentHolder> {
    private List<Document> documents;
    private DocumentAdapterListener listener;



    public class DocumentHolder extends RecyclerView.ViewHolder {
        private final TextView documentNameTV;
        private final TextView uploadedBy;
        private final TextView dateOfContent;

        public DocumentHolder(View v) {
            super(v);
            documentNameTV = v.findViewById(R.id.document_name_text_view);
            uploadedBy = v.findViewById(R.id.document_written_by_text_view);
            dateOfContent = v.findViewById(R.id.documentDateTV);
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

    public DocumentAdapter(List<Document> documents)
    {
        this.documents = documents;
    }

    @Override
    public DocumentAdapter.DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_documents_instance, parent, false);
        return new DocumentAdapter.DocumentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DocumentAdapter.DocumentHolder holder, final int position)
    {
        String documentName = documents.get(position).getFilename();
        String documentDate = documents.get(position).getDateOfContentAsString();
        String documentWrittenBy_Last = documents.get(position).getAuthorByLast();

        holder.documentNameTV.setText(documentName);
        holder.uploadedBy.setText(documentWrittenBy_Last);
        holder.dateOfContent.setText(documentDate);

    }


    @Override
    public int getItemCount() {
        return documents.size();
    }

    public void setDocs(List<Document> documents)
    {
        this.documents = documents;
        notifyDataSetChanged();
    }

    interface DocumentAdapterListener {
        void onItemClick(int position);
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setOnItemClickListener(DocumentAdapterListener listener)
    {
        this.listener = listener;
    }
}