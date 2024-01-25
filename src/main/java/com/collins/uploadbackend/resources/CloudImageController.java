package com.collins.uploadbackend.resources;

import com.collins.uploadbackend.entities.Image;
import com.collins.uploadbackend.services.CloudImageService;
import com.collins.uploadbackend.services.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cloudinary")
@AllArgsConstructor
public class CloudImageController {
    private CloudinaryService cloudinaryService;
    private CloudImageService cloudImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
        if(bufferedImage == null){
            return new ResponseEntity<>("Not a valid Image", HttpStatus.BAD_REQUEST);
        }
        Map data = cloudinaryService.upload(multipartFile);
        Image image =
                new Image((String)data.get("original_filename"),
                (String)data.get("url"),
                (String)data.get("public_id"));
        cloudImageService.upload(image);
        return new ResponseEntity<>("Image saved successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        Optional<Image> optionalCloudImage = cloudImageService.findById(id);
        if(optionalCloudImage.isEmpty()){
            return new ResponseEntity<>("Image does not exist", HttpStatus.NOT_FOUND);
        }
        Image image = optionalCloudImage.get();
        String cloudinaryImageId = image.getImageId();
        cloudImageService.delete(Long.valueOf(cloudinaryImageId));
        cloudImageService.delete(id);
        return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
    }
}
