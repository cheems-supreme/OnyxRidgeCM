//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: BrowsePhotosFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for logic regarding viewing and downloading
//          photos
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Changed the List of Photos to a final variable
//      - Changed format of onCreateView(...) (put recView def
//        lower in the method)
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Refactored `LoadPhotoList(...)` to
//        `loadPhotoList(...)`
//      - Reformatted spacings
//      - Implemented the use of `LocalDate` and removed `Date`
//        instances.
//      - Removed unused imports
// ------------------------------------------------
// - 12/1/2020
// - R.O.
// - DETAILS:
//      - Added code to handle filtering and sorting
//        - Since there isn't any filtering type for photos
//        (yet), filtering for photos is currently disabled.
// ------------------------------------------------
// - 12/3/2020
// - R.O.
// - DETAILS:
//      - Added code to handle letting the user know that
//        filtering and sorting cannot be done, if there are no
//        photos loaded.
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.adapters.PhotosAdapter;
import com.icecrown.onyxridgecm.enums.SortType;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;
import com.icecrown.onyxridgecm.utility.Photo;
import com.icecrown.onyxridgecm.utility.PhotoFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrowsePhotosFragment extends Fragment {

    private RecyclerView recView;
    private String projectNameString;
    private MaterialTextView projectNameTextView;
    private final BrowsePhotosFragment singleton = this;
    private PhotosAdapter photosAdapter;
    private final List<Photo> photoList = new ArrayList<>();
    private FragmentManager manager;
    private MaterialTextView sortTV;
    private MaterialTextView filterTV;



    private final IProjectSelectedCallback callback = new IProjectSelectedCallback() {
        @Override
        public void onProjectSelected(String projectName) {
            projectNameString = projectName;
            projectNameTextView.setText(projectName);
            loadPhotoList();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = getActivity().getSupportFragmentManager();

        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);

        sortTV = v.findViewById(R.id.sort_text_view);
        sortTV.setOnClickListener(l -> {
            if(photosAdapter.getPhotos().size() == 0) {
                Snackbar.make(projectNameTextView, R.string.no_images_to_sort, Snackbar.LENGTH_SHORT).show();
            }
            else {
                PopupMenu sortOptions = new PopupMenu(getActivity(), sortTV);
                sortOptions.getMenuInflater().inflate(R.menu.menu_sort_options, sortOptions.getMenu());
                sortOptions.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.no_sort) {
                        Log.d("EPOCH-3", "No sort entered.");
                        photosAdapter.setPhotos(photoList);
                    } else if (itemId == R.id.last_name_sort_asc) {
                        Log.d("EPOCH-3", "last name asc sort entered.");
                        sortPhotosByOption(photosAdapter.getPhotos(), SortType.LAST_NAME_ASC);
                    } else if (itemId == R.id.last_name_sort_desc) {
                        Log.d("EPOCH-3", "last name desc sort entered.");
                        sortPhotosByOption(photosAdapter.getPhotos(), SortType.LAST_NAME_DESC);
                    } else if (itemId == R.id.date_sort_asc) {
                        Log.d("EPOCH-3", "date asc sort entered.");
                        sortPhotosByOption(photosAdapter.getPhotos(), SortType.DATE_ASC);
                    } else if (itemId == R.id.date_sort_desc) {
                        Log.d("EPOCH-3", "date desc sort entered.");
                        sortPhotosByOption(photosAdapter.getPhotos(), SortType.DATE_DESC);
                    }
                    photosAdapter.notifyDataSetChanged();
                    return true;
                });
                sortOptions.show();
            }
        });
        filterTV = v.findViewById(R.id.filter_text_view);
        filterTV.setOnClickListener(l -> {
            if(photosAdapter.getPhotos().size() == 0) {
                Snackbar.make(projectNameTextView, R.string.no_images_to_filer, Snackbar.LENGTH_SHORT).show();
            } else {
                PopupMenu filterOptions = new PopupMenu(getActivity(), filterTV);
                filterOptions.getMenuInflater().inflate(R.menu.menu_filter_options, filterOptions.getMenu());
                filterOptions.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.no_filter) {
                        photosAdapter.setPhotos(photoList);
                    } else if (itemId == R.id.accident_filter) {
                        Snackbar.make(filterTV, R.string.filter_option_not_valid, Snackbar.LENGTH_SHORT).show();
                    }
                    photosAdapter.notifyDataSetChanged();
                    return true;
                });
                filterOptions.show();
            }
        });

        projectNameTextView = v.findViewById(R.id.project_name);

        projectNameTextView.setOnClickListener(v1 -> {
            SelectProjectFragment fragment = new SelectProjectFragment();
            fragment.setCallback(callback);
            manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
        });

        photosAdapter = new PhotosAdapter(photoList);
        photosAdapter.setOnItemClickListener(position -> {
            final File f = PhotoFactory.generateImageFile(getContext());

            if(f != null) {
                photoList.get(position).getRef().getFile(f).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ViewPhotoFragment fragment = new ViewPhotoFragment();
                        fragment.setImageFile(f);
                        manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
                    } else {
                        Snackbar.make(recView, R.string.photo_cannot_be_loaded, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recView = v.findViewById(R.id.content_browse_rec_view);

        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(recView.getContext(), DividerItemDecoration.VERTICAL));

        recView.setAdapter(photosAdapter);

        return v;
    }


    private void loadPhotoList() {
        if(!photoList.isEmpty()) {
            photoList.clear();
        }

        FirebaseStorage.getInstance().getReference(projectNameString + "/photos/").listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                final List<StorageReference> reference = task.getResult().getItems();
                if(reference.size() != 0) {
                    new Thread(() -> {
                        for(final StorageReference photo : reference) {
                            photo.getMetadata().addOnCompleteListener(task1 -> {
                                if(task.isSuccessful()) {
                                    final StorageMetadata meta = task1.getResult();
                                    LocalDate dateOfPhoto = null;
                                    try {
                                        dateOfPhoto = LocalDate.parse(meta.getCustomMetadata("date_uploaded"), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                                        Log.d("EPOCH-3", "Date first format successfully parsed.");
                                    } catch (Exception e) {
                                        Log.d("EPOCH-3", "DateFormatter try/catch entered");
                                        try {
                                            dateOfPhoto = LocalDate.parse(meta.getCustomMetadata("date_uploaded"), DateTimeFormatter.ofPattern("MM/d/yyyy"));
                                            Log.d("EPOCH-3", "Date second format successfully parsed.");
                                        }
                                        catch (Exception e1) {
                                            Log.d("EPOCH-3", "Date exception for 'date_uploaded' occurred.");
                                            e1.printStackTrace();
                                        }
                                    }

                                    File f = null;
                                    try {
                                        f = File.createTempFile("temp", ".jpg");
                                    } catch (IOException ioe) {
                                        Log.d("EPOCH-3", "createTempFile(...) IOException encountered.");
                                        ioe.printStackTrace();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    final File fileTemp = f;

                                    Log.d("EPOCH-3", "Date format for image: " + dateOfPhoto.toString());


                                    final LocalDate finalDateOfPhoto = dateOfPhoto;

                                    photo.getFile(fileTemp).addOnCompleteListener(task2 -> {
                                        Photo adder = new Photo(fileTemp.getName(), Uri.fromFile(fileTemp), photo, finalDateOfPhoto, meta.getCustomMetadata("taken_by_last"), meta.getCustomMetadata("taken_by_first"));
                                        adder.generateBitmapForUri(getActivity(), getActivity().getContentResolver());
                                        photoList.add(adder);

                                        if(photoList.size() == reference.size()) {
                                            photosAdapter.setPhotos(photoList);

                                        }
                                    });
                                }
                            });
                        }
                    }).start();

                }
                else {
                    Snackbar.make(recView, R.string.no_photos_to_show, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(recView, R.string.no_photos_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void sortPhotosByOption(List<Photo> photos, SortType type) {
        Comparator<Photo> c;

        switch(type) {
            case LAST_NAME_ASC:
                c = (o1, o2) -> {
                    int docLastVal = o1.getTakenByLastNameFirst().compareTo(o2.getTakenByLastNameFirst());
                    return Integer.compare(docLastVal, 0);
                };
                break;
            case LAST_NAME_DESC:
                c = (o1, o2) -> {
                    int docLastVal = o1.getTakenByLastNameFirst().compareTo(o2.getTakenByLastNameFirst());
                    return Integer.compare(docLastVal, 0);
                };
                c = c.reversed();
                break;
            case DATE_ASC:
                c = (o1, o2) -> {
                    if(o1.getDate().isBefore(o2.getDate())) {
                        return -1;
                    }
                    else if (o2.getDate().isAfter(o2.getDate())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                };
                break;
            case DATE_DESC:
                c = (o1, o2) -> {
                    if(o1.getDate().isBefore(o2.getDate())) {
                        return -1;
                    }
                    else if (o2.getDate().isAfter(o2.getDate())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                };
                c = c.reversed();
                break;
            default:
                c = null;
                break;
        }
        photos.sort(c);
    }
}
