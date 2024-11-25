/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.base.service.file_upload;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.net.http.SetConnection;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.ObjectHelper;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * 阿里云 OSS Api 版本（不使用 SDK）工具类
 * <p>
 * 阿里云官方文档地址：<a href="https://helpcdn.aliyun.com/document_detail/31947.html">...</a>
 */
@Deprecated
public class OssUpload implements IFileUpload {
    @Value("${S3Storage.Oss.bucket}")
    private String ossBucket;

    @Value("${S3Storage.Oss.accessKeyId}")
    private String accessKeyId;

    @Value("${S3Storage.Oss.secretAccessKey}")
    private String secretAccessKey;

    @Value("${S3Storage.Oss.endpoint}")
    private String endpoint;

    /**
     * 上传文件到 OSS（Amazon S3 兼容的存储服务）。
     *
     * @param filename 要上传的文件名。
     * @param content  要上传的文件内容，以字节数组形式提供。
     * @return 返回一个布尔值，表示文件是否成功上传。成功返回 true，失败返回 false。
     */
    @Override
    public boolean upload(String filename, byte[] content) {
        String date = DateUtil.getGMTDate();// 获取当前 GMT 时间，用于请求头 Date 字段
        String signResourcePath = "/" + ossBucket + "/" + filename;   // 构建资源签名路径
        String signature = MessageDigestHelper.getHmacSHA1AsBase64(secretAccessKey, buildPutSignData(date, signResourcePath));// 计算请求签名
        String url = "http://" + ossBucket + "." + endpoint + "/" + filename;// 构建上传 URL

        ResponseEntity result = Post.put(url, content, conn -> {  // 执行 PUT 请求上传文件
            conn.setRequestProperty("Date", date); // 设置请求头 Date
            conn.setRequestProperty("Authorization", "OSS " + accessKeyId + ":" + signature); // 设置请求头 Authorization
        });

        // 判断上传是否成功
        // 根据 HTTP 返回码和 ETag 是否存在来判断上传结果
        return result.getHttpCode() == 200 && result.getConnection().getHeaderField("ETag") != null;
    }

    /**
     * 从 OSS（对象存储服务）获取指定对象的内容。
     *
     * @param key 对象在 OSS 中的键（KEY），用于标识对象。
     * @return 返回从 OSS 获取的对象内容的字符串表示。
     */
    public String getOssObj(String key) {
        String signResourcePath = "/" + ossBucket + key; // 构造资源路径，包括 bucket 名称和 key
        String date = DateUtil.getGMTDate();// 获取当前时间，用于签名
        String signature = MessageDigestHelper.getHmacSHA1AsBase64(secretAccessKey, buildGetSignData(date, signResourcePath));   // 使 用HMAC-SHA1 算法生成签名

        // 构建请求头，包括日期和授权信息
        Map<String, String> head = ObjectHelper.hashMap("Date", date, "Authorization", "OSS " + accessKeyId + ":" + signature);
        String url = "http://" + ossBucket + "." + endpoint;// 构造请求的 URL

        return Get.get(url + key, SetConnection.map2header(head)).toString(); // 发起 GET 请求并返回结果
    }

    /**
     * 构建用于签名的 GET 请求数据字符串。
     *
     * @param date              请求的时间，格式为字符串。
     * @param canonicalResource 规范化的资源字符串，描述了被请求的资源。
     * @return 返回构建好的用于签名的字符串，该字符串包含了请求方法、空行、日期和规范化的资源。
     */
    private static String buildGetSignData(String date, String canonicalResource) {
        return "GET" + "\n\n\n" + date + "\n" + canonicalResource;// 按照规定的格式构建字符串，包括请求方法、空行、日期和规范化的资源
    }

    private static String buildPutSignData(String date, String canonicalResource) {
        return "PUT" + "\n\n\n" + date + "\n" + canonicalResource;
    }

}