package com.ajaxjs.base.controller;

import com.ajaxjs.data.data_service.DataServiceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据服务接口
 */
@RestController
@RequestMapping("/common_api")
public interface CommonApiController extends DataServiceController {
}