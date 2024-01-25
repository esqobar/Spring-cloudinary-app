package com.collins.uploadbackend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.collins.uploadbackend.utils.CloudinaryConstant.*;

@Service
@AllArgsConstructor
public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService(){
        Map<String , String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        cloudinary = new Cloudinary(config);
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        try{
            File file = convert(multipartFile);
            Map data = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            file.delete();
            return data;
        } catch (IOException e){
            throw new IOException("Failed to upload file");
        }
    }

    public Map delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }
}
