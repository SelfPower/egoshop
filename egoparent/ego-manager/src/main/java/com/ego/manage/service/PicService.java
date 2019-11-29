package com.ego.manage.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PicService {
    Map<String,Object> upload(MultipartFile file) throws Exception;
}
