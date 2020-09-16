package com.example.android.socializer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Downloads extends Fragment implements AdapterView.OnItemSelectedListener {


    List<downloadImage> downloadImageList = new ArrayList<>();
    List<downloadVideo> downloadVideoList = new ArrayList<>();

    int current = 0;
    RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerDownload);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.downloadsOption, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        recyclerView = view.findViewById(R.id.recylerDownload);


    }

    public Downloads() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        current = position;
        switch (position) {

            case 0:
                initalizeImageList(GlobalAccessibleClass.wifile);
                downloadAdapter downloadAdapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                recyclerView.setAdapter(downloadAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            case 1:

                inializeVideoList(GlobalAccessibleClass.wvfile);

                downloadAdapter downloadAdapter1 = new downloadAdapter(getActivity(), null, downloadVideoList, true);

                recyclerView.setAdapter(downloadAdapter1);

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                break;
            case 4:

                initalizeImageList(GlobalAccessibleClass.ipifile);
                downloadAdapter ipiadapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                recyclerView.setAdapter(ipiadapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                break;
            case 5:
                inializeVideoList(GlobalAccessibleClass.ipvfile);
                downloadAdapter ipvadapter = new downloadAdapter(getActivity(), null, downloadVideoList, true);
                recyclerView.setAdapter(ipvadapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            case 2:
                initalizeImageList(GlobalAccessibleClass.isifile);
                downloadAdapter isiadapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                recyclerView.setAdapter(isiadapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            case 3:
                inializeVideoList(GlobalAccessibleClass.isvfile);
                downloadAdapter isvfile = new downloadAdapter(getActivity(), null, downloadVideoList, true);
                recyclerView.setAdapter(isvfile);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.dframe, fragment);
        fragmentTransaction.commit();


    }

    private void initalizeImageList(File Src) {

        downloadImageList.clear();

        File src = Src;

        File[] files = src.listFiles();

        for (int i = 0; i < files.length; i++) {

            String path = files[i].getAbsolutePath();

            Bitmap bitmap = BitmapFactory.decodeFile(path);

            downloadImageList.add(new downloadImage(bitmap, path));

        }


    }

    private void inializeVideoList(File Src) {

        downloadVideoList.clear();
        try {
            File src = Src;

            File[] files = src.listFiles();
            Log.e("VideoList", files.length + "");
            for (int l = 0; l < files.length; l++) {

                String path = files[l].getAbsolutePath();

                downloadVideoList.add(new downloadVideo(path));
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
       Log.e("OnRes","Download");
        HomeActivity homeActivity = new HomeActivity();
        homeActivity.setBottom(3);
        try {
            switch (current) {

                case 0:
                    initalizeImageList(GlobalAccessibleClass.wifile);
                    downloadAdapter downloadAdapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                    recyclerView.setAdapter(downloadAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    break;
                case 1:

                    inializeVideoList(GlobalAccessibleClass.wvfile);

                    downloadAdapter downloadAdapter1 = new downloadAdapter(getActivity(), null, downloadVideoList, true);

                    recyclerView.setAdapter(downloadAdapter1);

                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    break;
                case 4:

                    initalizeImageList(GlobalAccessibleClass.ipifile);
                    downloadAdapter ipiadapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                    recyclerView.setAdapter(ipiadapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    break;
                case 5:
                    inializeVideoList(GlobalAccessibleClass.ipvfile);
                    downloadAdapter ipvadapter = new downloadAdapter(getActivity(), null, downloadVideoList, true);
                    recyclerView.setAdapter(ipvadapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    break;
                case 2:
                    initalizeImageList(GlobalAccessibleClass.isifile);
                    downloadAdapter isiadapter = new downloadAdapter(getActivity(), downloadImageList, null, false);
                    recyclerView.setAdapter(isiadapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    break;
                case 3:
                    inializeVideoList(GlobalAccessibleClass.isvfile);
                    downloadAdapter isvfile = new downloadAdapter(getActivity(), null, downloadVideoList, true);
                    recyclerView.setAdapter(isvfile);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    break;


            }
        }
        catch (Exception e){


        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("On Pause","download");

    }
}
