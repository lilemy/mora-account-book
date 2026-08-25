package com.lilemy.moraaccountbook.manager.oss.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lilemy.moraaccountbook.exception.BusinessException;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.manager.oss.OssProperties;
import com.lilemy.moraaccountbook.manager.oss.OssService;
import com.lilemy.moraaccountbook.manager.oss.entity.OssVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 对象存储服务实现类
 *
 * @author lilemy
 * @date 2026-08-25
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Resource
    private S3Client s3Client;

    @Resource
    private OssProperties ossProperties;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public String uploadFile(String prefix, Path filePath) {
        String originalFilename = filePath.getFileName().toString();
        String suffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(StringUtils.isEmpty(suffix), ErrorCode.PARAMS_ERROR, "请上传正确的文件格式");
        String objectKey = getPath(prefix, suffix);
        log.info("文件上传开始: {}", objectKey);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build(),
                filePath
        );
        log.info("文件上传成功: {}", objectKey);
        return getFullPath(objectKey);
    }

    @Override
    public String uploadFile(String prefix, MultipartFile multipartFile) {
        // 自动提取后缀名
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(StringUtils.isEmpty(suffix), ErrorCode.PARAMS_ERROR, "请上传正确的文件格式");
        String objectKey = getPath(prefix, suffix);
        try (InputStream is = multipartFile.getInputStream()) {
            return uploadInputStream(objectKey, is, multipartFile.getSize());
        } catch (IOException e) {
            log.error("文件流读取失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }

    @Override
    public String uploadFile(String prefix, String suffix, InputStream inputStream, long contentLength) {
        String objectKey = getPath(prefix, suffix);
        return uploadInputStream(objectKey, inputStream, contentLength);
    }

    @Override
    public String uploadFile(String prefix, String fileUrl) {
        // 1. 校验 URL
        ThrowUtils.throwIf(StringUtils.isBlank(fileUrl), ErrorCode.PARAMS_ERROR, "文件地址不能为空");
        try {
            URL url = URI.create(fileUrl).toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            // 获取文件类型（优先从 URL 中解析）
            String suffix = removeQueryParams(FileUtil.getSuffix(fileUrl));
            // 如果 URL 没有后缀，尝试从 contentType 获取
            if (StringUtils.isBlank(suffix)) {
                String contentType = connection.getContentType();
                suffix = getSuffixByContentType(contentType);
            }
            ThrowUtils.throwIf(StringUtils.isBlank(suffix), ErrorCode.PARAMS_ERROR, "无法识别文件类型");
            // 3. 获取输入流
            try (InputStream inputStream = connection.getInputStream()) {
                long contentLength = connection.getContentLengthLong();
                String objectKey = getPath(prefix, suffix);
                return uploadInputStream(objectKey, inputStream, contentLength);
            }
        } catch (Exception e) {
            log.error("URL图片上传失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }

    @Override
    public List<OssVO> getFileList(String prefix) {
        // 列出对象
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(ossProperties.getBucketName())
                        .prefix(prefix)
                        .build());
        return listResponse.contents().stream().map(object -> {
            OssVO vo = new OssVO();
            vo.setUrl(getFullPath(object.key()));
            vo.setSize(object.size());
            return vo;
        }).toList();
    }

    @Override
    public void downloadFileByUrl(String fullPath, Path destinationPath) {
        String objectKey = extractObjectKey(fullPath);
        downloadFile(objectKey, destinationPath);
    }

    @Override
    public void downloadFile(String objectKey, Path destinationPath) {
        log.info("文件下载开始: {}", objectKey);
        s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build(),
                destinationPath
        );
        log.info("文件下载成功: {}", objectKey);
    }

    @Override
    public void deleteFileByUrl(String fullPath) {
        String objectKey = extractObjectKey(fullPath);
        deleteFile(objectKey);
    }

    @Override
    public void deleteFile(String objectKey) {
        log.info("文件删除开始: {}", objectKey);
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .build()
        );
        log.info("文件删除成功: {}", objectKey);
    }

    @Override
    public String getPath(String prefix, String suffix) {
        // 生成 uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 生成日期路径
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        // 拼接路径
        String path = StringUtils.isNotEmpty(prefix) ?
                prefix + "/" + datePath + "/" + uuid : datePath + "/" + uuid;
        // 判断后缀是否有 . 如果没有，则添加
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        return path + suffix;
    }

    @Override
    public String getFullPath(String objectKey) {
        return ossProperties.getEndpoint() + "/" + ossProperties.getBucketName() + "/" + objectKey;
    }

    /**
     * 获取文件对象存储中的路径
     * 全路径 = Endpoint + "/" + Bucket + "/" + 对象存储中的路径
     *
     * @param fullPath 文件的完整路径
     * @return 文件对象存储中的路径
     */
    private String extractObjectKey(String fullPath) {
        ThrowUtils.throwIf(StringUtils.isBlank(fullPath), ErrorCode.PARAMS_ERROR, "文件路径不能为空");
        // 构造前缀
        String endpoint = ossProperties.getEndpoint();
        String bucket = ossProperties.getBucketName();
        // 确保 endpoint 不以 / 结尾，方便拼接
        String prefix = StrUtil.removeSuffix(endpoint, "/") + "/" + bucket + "/";
        if (fullPath.startsWith(prefix)) {
            // 截取前缀之后的部分
            return fullPath.substring(prefix.length());
        } else {
            // 如果 URL 格式不符合预期，尝试处理特殊情况或者抛出异常
            log.warn("全路径格式不符合配置前缀: {}, 尝试解析...", fullPath);
            // 兜底
            return fullPath;
        }
    }

    /**
     * 通过 RequestBody 处理流上传
     *
     * @param objectKey     文件路径
     * @param inputStream   输入流
     * @param contentLength 文件长度
     * @return 文件上传后的完整路径
     */
    private String uploadInputStream(String objectKey, InputStream inputStream, long contentLength) {
        log.info("文件流上传开始: {}", objectKey);
        String contentType = FileUtil.getMimeType(objectKey);
        ThrowUtils.throwIf(StringUtils.isEmpty(contentType), ErrorCode.PARAMS_ERROR, "请上传正确的文件格式");
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(ossProperties.getBucketName())
                        .key(objectKey)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, contentLength)
        );
        log.info("文件流上传成功: {}", objectKey);
        return getFullPath(objectKey);
    }

    /**
     * 根据 ContentType 获取后缀
     */
    private String getSuffixByContentType(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return null;
        }
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            default -> null;
        };
    }

    /**
     * 移除 URL 中的查询参数
     *
     * @param url URL
     * @return 去掉查询参数后的 URL
     */
    private String removeQueryParams(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        int index = url.indexOf("?");
        return index > -1 ? url.substring(0, index) : url;
    }
}
