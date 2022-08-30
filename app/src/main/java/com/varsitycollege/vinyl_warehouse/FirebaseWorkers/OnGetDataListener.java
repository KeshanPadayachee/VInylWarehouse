package com.varsitycollege.vinyl_warehouse.FirebaseWorkers;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    //OnSuccess from firebase send the data back
    void onSuccess(DataSnapshot dataSnapshot);

    //Once the data is done with request
    void onStartWork();

    //If request failed
    void onFailure();
}
