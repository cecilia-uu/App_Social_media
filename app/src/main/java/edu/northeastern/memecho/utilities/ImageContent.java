package edu.northeastern.memecho.utilities;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.memecho.models.GalleryImages;

public class ImageContent {
    private static final List<GalleryImages> list = new ArrayList<>();
    public static void loadImages(File file) {
        GalleryImages images = new GalleryImages();
        images.setPicUri(Uri.fromFile(file));
        addImages(images);
    }

    /**
     * Helper function to add images
     * @param images GalleryImages, the newly added item.
     */
    private static void addImages(GalleryImages images) {
        list.add(0, images);
    }

    public static void loadSavedImages(File directory) {
        list.clear();

        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg") || extension.equals(".png")) {
                    loadImages(file);
                }
            }
        }
    }
}
