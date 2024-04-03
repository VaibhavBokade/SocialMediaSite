package org.example.socialMN.encodedImgData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageToBase64 {

    public static void main(String[] args) {
        String imagePath = "/home/perennial/Pictures/omkar.jpeg";
        String base64String = convertImageToBase64(imagePath);

        System.out.println("Base64 Encoded Image String:\n" + base64String);
    }

    private static String convertImageToBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
