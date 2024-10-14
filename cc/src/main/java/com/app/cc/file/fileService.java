package com.app.cc.file;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
@Service
public class fileService implements fileservice1{
    private filerepo attachmentRepository;

    public fileService(filerepo attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public file saveAttachment(MultipartFile file) throws Exception {
        if (file == null) {
            // return null or throw an exception, depending on your use case
            return null;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                        + fileName);
            }

            file attachment
                    = new file(fileName,
                    file.getContentType(),
                    file.getBytes());
            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public file getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }
    public void deleteAttachment(String fileId) throws Exception {
        file attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
        attachmentRepository.delete(attachment);
    }

}
