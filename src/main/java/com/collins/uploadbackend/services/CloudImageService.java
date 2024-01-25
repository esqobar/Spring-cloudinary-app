package com.collins.uploadbackend.services;

import com.collins.uploadbackend.entities.Image;
import com.collins.uploadbackend.repositories.CloudImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CloudImageService {
    private CloudImageRepository cloudImageRepository;

    public void upload(Image image){
        cloudImageRepository.save(image);
    }
    public void delete(Long id){
        cloudImageRepository.deleteById(id);
    }

    public Optional<Image> findById(Long id) {
        return cloudImageRepository.findById(id);
    }
}
