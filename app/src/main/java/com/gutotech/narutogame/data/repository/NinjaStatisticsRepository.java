package com.gutotech.narutogame.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.gutotech.narutogame.data.firebase.FirebaseConfig;
import com.gutotech.narutogame.data.model.NinjaStatistics;

import java.util.ArrayList;
import java.util.List;

public class NinjaStatisticsRepository {
    private static NinjaStatisticsRepository sInstance;

    private DatabaseReference ninjaStatisticsRef;

    private NinjaStatisticsRepository() {
        ninjaStatisticsRef = FirebaseConfig.getDatabase().child("ninja-statistics");
    }

    public static NinjaStatisticsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new NinjaStatisticsRepository();
        }
        return sInstance;
    }

    public void add(int ninjaId) {
        DatabaseReference ninjaStatisticsRef = FirebaseConfig.getDatabase()
                .child("ninja-statistics")
                .child(String.valueOf(ninjaId));

        ninjaStatisticsRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                NinjaStatistics ninjaStatistics = mutableData.getValue(NinjaStatistics.class);

                if (ninjaStatistics == null) {
                    return Transaction.success(mutableData);
                }

                ninjaStatistics.totalPlayers++;

                mutableData.setValue(ninjaStatistics);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b,
                                   @Nullable DataSnapshot dataSnapshot) {
            }
        });
    }

    public void remove(int ninjaId) {
        DatabaseReference ninjaStatisticsRef = FirebaseConfig.getDatabase()
                .child("ninja-statistics")
                .child(String.valueOf(ninjaId));

        ninjaStatisticsRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                NinjaStatistics ninjaStatistics = mutableData.getValue(NinjaStatistics.class);

                if (ninjaStatistics == null) {
                    return Transaction.success(mutableData);
                }

                if (ninjaStatistics.totalPlayers > 0) {
                    ninjaStatistics.totalPlayers--;
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public LiveData<List<NinjaStatistics>> getAllNinjaStatistics() {
        MutableLiveData<List<NinjaStatistics>> data = new MutableLiveData<>();

        List<NinjaStatistics> ninjaStatisticsList = new ArrayList<>();

        ninjaStatisticsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ninjaStatisticsList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    NinjaStatistics ninjaStatistics = data.getValue(NinjaStatistics.class);

                    if (ninjaStatistics.totalPlayers > 0) {
                        ninjaStatisticsList.add(ninjaStatistics);
                    }
                }

                data.postValue(ninjaStatisticsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return data;
    }
}
