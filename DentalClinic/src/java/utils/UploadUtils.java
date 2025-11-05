/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class UploadUtils {

    public static String uploadImage(Part filePart, String uploadPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        // Tạo folder nếu chưa có
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Lấy tên file gốc
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || fileName.isBlank()) {
            return null;
        }

        // Tạo đường dẫn lưu file
        String savedFileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = uploadPath + File.separator + savedFileName;
        filePart.write(filePath);

        return savedFileName; // Trả lại tên file để lưu DB
    }
}
