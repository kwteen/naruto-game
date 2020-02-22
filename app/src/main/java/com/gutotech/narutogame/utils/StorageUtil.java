package com.gutotech.narutogame.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.gutotech.narutogame.R;
import com.gutotech.narutogame.data.firebase.FirebaseConfig;
import com.gutotech.narutogame.data.model.CharOn;
import com.gutotech.narutogame.data.repository.Callback;

import java.security.SecureRandom;
import java.util.List;

public class StorageUtil {
    private final static SecureRandom random = new SecureRandom();

    public static void downloadImage(Context context, StorageReference imageReference, ImageView imageView) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .into(imageView);
    }

    // @param path Example: images/profiles/4/
    public static void listAll(String path, Callback<List<StorageReference>> callback) {
        StorageReference listRef = FirebaseConfig.getStorage().child(path);

        listRef.listAll().addOnSuccessListener(listResult -> callback.call(listResult.getItems()));
    }

    public static void downloadProfileForMsg(Context context, ImageView imageView) {
        StorageReference imageReference;

        if (CharOn.character != null) {
            imageReference = FirebaseConfig.getStorage().child("images")
                    .child("msg")
                    .child(String.valueOf(CharOn.character.getVillage().id))
                    .child(generateProfileId() + ".png");
        } else {
            imageReference = FirebaseConfig.getStorage().child("images")
                    .child("msg")
                    .child(generateVillageId())
                    .child(generateProfileId() + ".png");
        }

        downloadImage(context, imageReference, imageView);
    }

    public static void downloadProfile(Context context, ImageView imageView, int ninjaId) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("profile")
                .child(String.valueOf(ninjaId))
                .child("1.png");
        downloadImage(context, imageReference, imageView);
    }

    public static void downloadProfile(Context context, ImageView imageView, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        StorageReference imageReference = FirebaseConfig.getStorage().child(path);
        downloadImage(context, imageReference, imageView);
    }

    public static void downloadSmallProfile(Context context, ImageView imageView, int ninjaId) {
        StorageReference imageRef = FirebaseConfig.getStorage()
                .child("images")
                .child("criacao")
                .child("pequenas")
                .child(ninjaId + ".png");
        downloadImage(context, imageRef, imageView);
    }

    public static void downloadLotteryItem(ImageView imageView, String image) {
        StorageReference imageRef = FirebaseConfig.getStorage()
                .child("images/loteria/")
                .child(image + ".png");
        downloadImage(imageView.getContext(), imageRef, imageView);
    }

    public static void downloadSprite(ImageView imageView, int ninjaId) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images/sprites")
                .child(ninjaId + ".png");
        downloadImage(imageView.getContext(), imageReference, imageView);
    }

    public static void downloadJutsu(ImageView imageView, String name) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("jutsu")
                .child(name + (name.equals("Hana-Kyouka-Sai")
                        || name.equals("Dynamic_Entry")
                        || name.equals("Soushuriken_no_Jutsu")
                        || name.equals("Kage-Bunshin-no-Jutsu") ?
                        ".gif" : ".jpg"));
        downloadImage(imageView.getContext(), imageReference, imageView);
    }

    public static void downloadRamen(Context context, ImageView imageView, String image) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("comidas")
                .child(image + ".jpg");
        downloadImage(context, imageReference, imageView);
    }

    public static void downloadScroll(Context context, ImageView imageView, String id) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("pergaminhos")
                .child(id + ".png");
        downloadImage(context, imageReference, imageView);
    }

    public static void downloadTopImage(Context context, ImageView imageView, int ninjaId) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("topo-logado")
                .child(ninjaId + ".jpg");
        downloadImage(context, imageReference, imageView);
    }

    public static void baixarFidelityDia(Context context, ImageView imageView, int dia) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("fidelity")
                .child(dia + ".png");
        downloadImage(context, imageReference, imageView);
    }

    public static void baixarArmaImage(Context context, ImageView imageView, String name, String alcance) {
        StorageReference imageReference = FirebaseConfig.getStorage()
                .child("images")
                .child("armas")
                .child(alcance)
                .child(name + ".jpg");
        downloadImage(context, imageReference, imageView);
    }

    private static String generateVillageId() {
        return String.valueOf(new SecureRandom().nextInt(8) + 1);
    }

    private static String generateProfileId() {
        return String.valueOf(random.nextInt(6) + 1);
    }
}
