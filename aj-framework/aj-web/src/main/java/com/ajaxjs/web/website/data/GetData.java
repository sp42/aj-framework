package com.ajaxjs.web.website.data;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Head;
import com.ajaxjs.net.http.SkipSSL;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.web.website.SiteStruStartUp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class GetData {
    private static String BASE_API;

    public static void setBaseApi(String baseApi) {
        if (BASE_API == null) {
            BASE_API = baseApi;
            SkipSSL.init();
        }
    }

    private static final String PAGE_API_TENANT = "/common_api/%s/page?auth_tenant_id=%s&start=%s&limit=%s";

    private static final String PAGE_API = "/common_api/%s/page?start=%s&limit=%s";

    public static void getPageList(HttpServletRequest req, String namespace, String accessToken) {
        getPageList(req, 0, namespace, accessToken);
    }

    public static void getPageList(HttpServletRequest req, int tenantId, String namespace, String accessToken) {
        String start = req.getParameter("start") == null ? "0" : req.getParameter("start");
        String limit = req.getParameter("pageSize") == null ? "10" : req.getParameter("pageSize");
        String url;

        if (tenantId == 0)
            url = BASE_API + String.format(PAGE_API, namespace, start, limit);
        else
            url = BASE_API + String.format(PAGE_API_TENANT, namespace, tenantId, start, limit);

        if (req.getParameter("type") != null)
            url += "&type=" + req.getParameter("type");

        Map<String, Object> resultMap = Get.api(url, Head.oauth(accessToken));
        ResponseResult result = EntityConvert.map2Bean(resultMap, ResponseResult.class);

        if (result.getStatus() == 1) {
            Map<String, Object> data = result.getData();

            req.setAttribute("LIST", data.get("rows"));
            req.setAttribute("PAGE_TOTAL", data.get("total"));
        }
    }

    private static final String INFO_API = "/common_api/%s/%s?auth_tenant_id=%s";

    public static void getInfo(HttpServletRequest req, int tenantId, String namespace, String accessToken) {
        String id = req.getParameter("id");
        String url = BASE_API + String.format(INFO_API, namespace, id, tenantId);

        Map<String, Object> resultMap = Get.api(url, Head.oauth(accessToken));
        ResponseResult result = EntityConvert.map2Bean(resultMap, ResponseResult.class);

        if (result.getStatus() == 1) {
            Map<String, Object> data = result.getData();
            req.setAttribute("info", data);
        }
    }

    /**
     * 获取其他 QueryString 参数
     *
     * @param request HttpServletRequest 对象
     * @return 其他 QueryString 参数
     */
    public static String getQueryString(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 遍历参数列表，并将每个参数的值添加到分页参数中
        StringBuffer sb = new StringBuffer();

        for (String name : parameterMap.keySet()) {
            if (!name.equals("start") && !name.equals("pageSize")) {
                String[] values = parameterMap.get(name);
                // 只取第一个值
                String value = values[0];
                // 编码
                value = StrUtil.urlEncodeQuery(value);
                // 添加到查询字符串中
                sb.append("&").append(name).append("=").append(value);
            }
        }

        return sb.toString();
    }

    public static String substring(String str, int length) {
        if (str.length() < length)
            return str;
        else return str.substring(0, length);
    }

    public static void setDataToServletCache(String name, String apiUrl, String token, HttpServletRequest req) {
        if (req.getServletContext().getAttribute(name) == null) {
            SkipSSL.init();
            Map<String, Object> result = Get.api(apiUrl, Head.oauth(token));
            List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("data");
            SiteStruStartUp.setDataToServletCache(name, list, req.getServletContext());
        }
    }
}
