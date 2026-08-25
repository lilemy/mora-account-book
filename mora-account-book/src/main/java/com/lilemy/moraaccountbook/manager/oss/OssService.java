package com.lilemy.moraaccountbook.manager.oss;

import com.lilemy.moraaccountbook.manager.oss.entity.OssVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * 对象存储服务
 *
 * @author lilemy
 * @date 2026-08-25
 */
public interface OssService {

    /**
     * 上传文件 (基于 Path)
     *
     * @param prefix   前缀
     * @param filePath 文件路径
     * @return 文件上传后的完整路径
     */
    String uploadFile(String prefix, Path filePath);

    /**
     * 上传文件 (基于MultipartFile)
     *
     * @param prefix        前缀
     * @param multipartFile 文件
     * @return 文件上传后的完整路径
     */
    String uploadFile(String prefix, MultipartFile multipartFile);

    /**
     * 上传文件 (基于通用输入流)
     *
     * @param prefix        前缀
     * @param suffix        后缀
     * @param inputStream   输入流
     * @param contentLength 流的大小
     * @return 文件上传后的完整路径
     */
    String uploadFile(String prefix, String suffix, InputStream inputStream, long contentLength);

    /**
     * 上传文件 (基于 URL)
     *
     * @param prefix  前缀
     * @param fileUrl 图片 URL
     * @return 文件上传后的完整路径
     */
    String uploadFile(String prefix, String fileUrl);

    /**
     * 列出文件列表
     *
     * @param prefix 前缀，文件夹以 / 结尾
     * @return 文件列表
     */
    List<OssVO> getFileList(String prefix);

    /**
     * 根据完整路径下载文件
     *
     * @param fullPath        文件路径
     * @param destinationPath 文件保存路径
     */
    void downloadFileByUrl(String fullPath, Path destinationPath);

    /**
     * 下载文件
     *
     * @param objectKey       文件路径
     * @param destinationPath 文件保存路径
     */
    void downloadFile(String objectKey, Path destinationPath);

    /**
     * 根据完整路径删除文件
     *
     * @param fullPath 完整路径
     */
    void deleteFileByUrl(String fullPath);

    /**
     * 删除文件
     *
     * @param objectKey 文件路径
     */
    void deleteFile(String objectKey);

    /**
     * 生成一个符合特定规则的、唯一的文件路径。通过使用日期、UUID、前缀和后缀等元素的组合
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 文件路径
     */
    String getPath(String prefix, String suffix);

    /**
     * 获取文件的完整路径
     *
     * @param objectKey 文件路径
     * @return 文件完整路径
     */
    String getFullPath(String objectKey);
}
