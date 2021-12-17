package com.yapp.project.aux.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.yapp.project.aux.common.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CloudStorageUtil {

    private static final String KEY_PATH = "classpath:storage/minning-335309-b237a3e1bfa5.json";
    private static final String BUCKET = "minning";
    private static final ArrayList<Acl> ACL = new ArrayList<>(List.of(Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.READER)));

    /**
     * @RequestParam
     * img : Image of MultipartFile type
     * dir : Image Path / ex) retrospect -> retrospect/{routineId}/
     * */
    public BlobInfo upload(MultipartFile image, String dir) throws IOException {
        String img = makeImageFileName(image);
        InputStream keyFile = ResourceUtils.getURL(KEY_PATH).openStream();
        Storage storage = StorageOptions.newBuilder().setProjectId(BUCKET)
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build().getService();
        return storage.create(
                BlobInfo.newBuilder(BUCKET, dir + img)
                        .setAcl(ACL)
                        .build(),
                image.getBytes());
    }

    private String makeImageFileName(MultipartFile image) {
        int dotIndex = image.getOriginalFilename().lastIndexOf(".");
        String fileName = image.getOriginalFilename().substring(0, dotIndex);
        String extension = image.getOriginalFilename().substring(dotIndex);
        return fileName + "_" + DateUtil.KST_LOCAL_DATETIME_NOW() + extension;
    }
}
