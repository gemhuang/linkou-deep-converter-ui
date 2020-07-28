package org.iii.converter.service;

import org.iii.converter.datamodel.Provider;
import org.iii.converter.mapper.ProviderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderMapper providerMapper;


    public Optional<Provider> getTop1() {
        return Optional.ofNullable(providerMapper.getTop1());
    }

    public void save(Provider provider) {
        if (getTop1().isPresent()) {
            providerMapper.update(provider);
        } else {
            providerMapper.insert(provider);
        }
    }

    public void delete() {
        providerMapper.delete();
    }

    public void saveImage(byte[] image, String fileName) throws IOException {
        String path = System.getProperty("user.dir") + "/workspace/images/brands/" + fileName;
        Files.write(Paths.get(path), image);
    }

    public Path getImageFile(String logo) {
        String logoFile = System.getProperty("user.dir") + "/workspace/images/brands/" + logo;
        return Paths.get(logoFile);
    }
}
