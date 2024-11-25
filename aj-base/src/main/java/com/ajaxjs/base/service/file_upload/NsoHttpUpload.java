/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.base.service.file_upload;

import com.ajaxjs.net.http.Delete;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Map;

/**
 * 网易云对象存储 HTTP 文件上传
 *
 * @author sp42 frank@ajaxjs.com
 */
public class NsoHttpUpload implements IFileUpload {
    /**
     * App ID
     */
    @Value("${S3Storage.Nso.accessKey}")
    private String accessKeyId;

    /**
     * App 密钥
     */
    @Value("${S3Storage.Nso.accessSecret}")
    private String accessSecret;

    @Value("${S3Storage.Nso.api}")
    private String api;

    @Value("${S3Storage.Nso.bucket}")
    private String bucket;

    /**
     * 列出所有的桶
     *
     * @return XML 结果
     */
    public Map<String, String> listBuk() {
        String now = DateUtil.getGMTDate();
        String canonicalHeaders = "", canonicalResource = "/";
        String data = "GET\n" + "\n" + "\n" + now + "\n" + canonicalHeaders + canonicalResource;

        return Get.apiXML("http://nos-eastchina1.126.net", conn -> {
            conn.addRequestProperty("Authorization", getAuthorization(data));
            conn.addRequestProperty("Date", now);
            conn.addRequestProperty("Host", "nos-eastchina1.126.net");
        });
    }

    /**
     * 生成验证的字符串
     *
     * @param data 数据
     * @return 验证的字符串
     */
    private String getAuthorization(String data) {
//        String signature = Digest.doHmacSHA256(accessSecret, data);
        String signature = MessageDigestHelper.getHmacSHA256AsBase64(accessSecret, data);

        return "NOS " + accessKeyId + ":" + signature;
    }

    /**
     * 创建空文件 TODO 报错
     *
     * @param filename 文件名
     */
    public void createEmptyFile(String filename) {
        String now = DateUtil.getGMTDate();
        String canonicalHeaders = "", canonicalResource = "/" + bucket + "/" + filename;
        String data = "PUT\n" + "\n\n" + now + "\n" + canonicalHeaders + canonicalResource;

        Post.put(api + filename, new byte[0], conn -> {
            conn.addRequestProperty("Authorization", getAuthorization(data));
            conn.addRequestProperty("Content-Length", "0");
            conn.addRequestProperty("Date", now);
//			conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
        });
    }

    /**
     * 删除指定的文件。
     *
     * @param filename 要删除的文件名称。
     * @return 总是返回 false，可能用于未来扩展。
     */
    public boolean delete(String filename) {
        String now = DateUtil.getGMTDate();// 获取当前时间，用于请求头
        String canonicalHeaders = "", canonicalResource = "/" + bucket + "/" + filename;// 构建规范化的请求头和资源路径
        String data = "DELETE\n" + "\n\n" + now + "\n" + canonicalHeaders + canonicalResource; // 构建用于授权认证的数据字符串

        Delete.del(api + filename, conn -> { // 发起 DELETE 请求删除文件
            conn.addRequestProperty("Authorization", getAuthorization(data));   // 设置请求授权头和日期头
            conn.addRequestProperty("Date", now);
        });

        return false;  // 当前实现总是返回 false
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     */
    public boolean uploadFile(String filePath) {
        return uploadFile(null, filePath);
    }

    /**
     * 上传文件。该方法用于将指定的文件上传，但不能指定上传到的具体目录。
     *
     * @param filePath 文件的路径。
     * @param filename 要上传的文件名，如果不指定，则使用文件原本的名称。
     * @return 返回一个布尔值，表示文件是否上传成功。
     */
    public boolean uploadFile(String filePath, String filename) {
        File file = new File(filePath); // 创建一个File对象，用于表示文件路径

        if (filename == null)   // 如果未指定文件名，则使用原文件名
            filename = file.getName();

        // 将文件以字节流形式打开，并计算其 MD5 值，然后调用 upload 方法进行上传
        return upload(FileHelper.openAsByte(file), filename, MessageDigestHelper.calcFileMD5(file, null));
    }

    /**
     * 上传
     *
     * @param bytes    文件字节数组
     * @param filename 文件名，可在前面设置目录名，如 folder + "/" + saveFileName
     * @param md5      MD5 值
     */
    public boolean upload(byte[] bytes, String filename, String md5) {
        String now = DateUtil.getGMTDate();
        String canonicalHeaders = "", canonicalResource = "/" + bucket + "/" + filename;
        String data = "PUT\n" + md5 + "\n\n" + now + "\n" + canonicalHeaders + canonicalResource;

        ResponseEntity result = Post.put(api + filename, bytes, conn -> {
            conn.addRequestProperty("Authorization", getAuthorization(data));
            conn.addRequestProperty("Content-Length", String.valueOf(bytes.length));
            conn.addRequestProperty("Content-MD5", md5);
            conn.addRequestProperty("Date", now);
//			conn.addRequestProperty("HOST", "gdhdc-org.nos-eastchina1.126.net/cover");
            // conn.addRequestProperty("x-nos-entity-type", "json");
        });

        // 判定是否上传成功
        String ETag = result.getConnection().getHeaderField("ETag");

        if (ETag == null)
            return false;

        return result.getHttpCode() == 200 && ETag.equalsIgnoreCase("\"" + md5 + "\"");
    }

    /**
     * 上传
     *
     * @param bytes    文件字节数组
     * @param filename 文件名，可在前面设置目录名，如 folder + "/" + saveFileName
     * @return 是否成功
     */
    @Override
    public boolean upload(String filename, byte[] bytes) {
        return upload(bytes, filename, MessageDigestHelper.calcFileMD5(null, bytes));
    }

    /**
     * 适合我自己写的文件上传
     *
     * @param bytes    文件字节数组
     * @param filename 文件名，可在前面设置目录名，如 folder + "/" + saveFileName
     * @return 是否成功
     */
    public boolean save(byte[] bytes, int offset, int length, String filename) {
        bytes = StreamHelper.subBytes(bytes, offset, length); // 内存中的字节数组上传到空间中

        return upload(filename, bytes);
    }
}
