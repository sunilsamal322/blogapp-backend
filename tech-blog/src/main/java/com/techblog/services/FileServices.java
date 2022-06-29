package com.techblog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileServices {

    String uploadImage(String path, MultipartFile file) throws IOException;

    InputStream getImage(String path,String fileName) throws FileNotFoundException;

    void deleteImage(String filePath,Integer postId);
}
