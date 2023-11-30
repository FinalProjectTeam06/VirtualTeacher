package com.example.finalprojectvirtualteacher.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class AssignmentsHelper {

    private static final String PUBLIC_KEY = "public_HAihc//OklG2CYl/IYpnWC6GRjk=";
    private static final String PRIVATE_KEY = "private_6EBcYIJc6YWdk+kxcvriK9piUF4=";
    private static final String URL_ENDPOINT = "https://ik.imagekit.io/vd81nq14b";

    private final ImageKit imageKit;


    public AssignmentsHelper() {
        this.imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(PUBLIC_KEY, PRIVATE_KEY, URL_ENDPOINT);
        imageKit.setConfig(config);
    }

    public String uploadAssignment(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("tempAssignment", ".docx");
        multipartFile.transferTo(tempFile);

        String base64 = Utils.fileToBase64(tempFile);
        FileCreateRequest fileCreateRequest = new FileCreateRequest(base64, tempFile.getName());
        fileCreateRequest.setFolder("assignments");

        try {
            Result result = imageKit.upload(fileCreateRequest);
            return result.getUrl();
        } catch (InternalServerException | BadRequestException | UnknownException | ForbiddenException |
                 TooManyRequestsException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }

}
