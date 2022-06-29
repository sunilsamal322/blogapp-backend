package com.techblog.servicesimpl;

import com.techblog.exception.FileFormatNotSupportException;
import com.techblog.exception.ImageNotFoundException;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.model.Post;
import com.techblog.repository.PostRepoitory;
import com.techblog.services.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServicesImpl implements FileServices {

    @Autowired
    private PostRepoitory postRepoitory;

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String fileExtension= StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(!(fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png")))
        {
            throw new FileFormatNotSupportException(fileExtension);
        }
        String fileName= UUID.randomUUID().toString()+"."+fileExtension;
        String filePath=path+ File.separator+fileName;
        File files=new File(path);
        if(!files.exists())
        {
            files.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getImage(String path, String fileName) throws FileNotFoundException {

        String filePath=path+File.separator+fileName;
        InputStream inputStream=new FileInputStream(filePath);
        return inputStream;
    }

    @Override
    public void deleteImage(String filePath,Integer postId)  {
        Post post=postRepoitory.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        String fileName=post.getPostImageName();
        if(!fileName.equals(""))
        {
            File file=new File(filePath+File.separator+fileName);
            file.delete();
            post.setPostImageName("");
            postRepoitory.save(post);
        }
        else {
            throw new ImageNotFoundException("No images available for delete");
        }
    }
}
