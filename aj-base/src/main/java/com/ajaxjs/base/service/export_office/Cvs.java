package com.ajaxjs.base.service.export_office;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class Cvs {
    /**
     * 将一个包含不同类型数据的二维列表 csv 格式写入到输出流中
     *
     * @param csv       一个包含不同类型数据的二维列表，每个一维列表表示一行，二维列表中的元素表示每个字段的值
     * @param separator csv 文件中的字段分隔符
     * @param output    输出流对象
     * @throws IOException 如果在写入过程中发生 I/O 错误
     */
    public static <T> void writeCsv(List<List<T>> csv, char separator, OutputStream output) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
            for (List<T> row : csv) {
                for (Iterator<T> iter = row.iterator(); iter.hasNext(); ) {
                    String field = String.valueOf(iter.next()).replace("\"", "\"\"");
                    if (field.indexOf(separator) > -1 || field.indexOf('"') > -1)
                        field = '"' + field + '"';

                    writer.append(field);

                    if (iter.hasNext())
                        writer.append(separator);
                }

                writer.newLine();
            }

            writer.flush();
        }
    }

    /**
     * 接收一个CSV格式的二维列表作为参数，将列表内容以GET请求的形式返回给客户端进行下载
     *
     * @param csv      CSV格式的二维列表
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     */
    public static void doGet(List<List<Object>> csv, HttpServletRequest request, HttpServletResponse response) {
        // 从请求路径中获取文件名
        String filename = request.getPathInfo().substring(1);

        // 设置响应的Content-Type和Content-Disposition头部字段
        response.setHeader("content-type", "text/csv");
        response.setHeader("content-disposition", "attachment;filename=\"" + filename + "\"");

        try {
            // 将CSV列表内容写入响应输出流中
            writeCsv(csv, ';', response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
