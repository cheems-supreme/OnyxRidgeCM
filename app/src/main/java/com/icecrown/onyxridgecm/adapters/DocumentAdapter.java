//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: DocumentsAdapter.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/2/2020
// Date added: ~11/5/2020
//
// Detail: Used to connect Document data to RecyclerView in Browse-
//         ReportsFragment
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/9/2020
// - R.O.
// - DETAILS:
//      - Made interface public
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Removed variables and placed 'get' method
//        calls in place of
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_document_instance, parent, false);
        return new DocumentAdapter.DocumentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DocumentAdapter.DocumentHolder holder, final int position)
    {
        holder.documentNameTV.setText(documents.get(position).getFilename());
        holder.uploadedBy.setText(documents.get(position).getAuthorByLast());
        holder.dateOfContent.setText(documents.get(position).getDateOfContentAsString());
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

    public interface DocumentAdapterListener {
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