package com.example.finalprojectvirtualteacher.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class AssignmentsHelper {

    private static final String CLOUD_NAME = "dadyjdpd5";
    private static final String API_KEY = "974411475497154";
    private static final String API_SECRET = "NNuIeofkcvfVYCVMjLXW61uNDoU";

    private final Cloudinary cloudinary;


    public AssignmentsHelper() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("tempAssignment", ".zip");
        multipartFile.transferTo(tempFile);

        Map uploadResult = cloudinary.uploader().upload(tempFile,
                ObjectUtils.asMap(
                        "folder", "assignments",
                        "resource_type", "auto"
                ));

        tempFile.deleteOnExit();

        return uploadResult.get("url").toString();
    }

}