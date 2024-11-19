package com.teamsoft.mgr.base.service;

import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.mgr.base.model.District;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

/**
 * 区域服务接口
 * @author zhangcc
 * @version 2023/10/16
 */
public interface IDistrictService extends ICommonService<District> {
	@Async
	Future<String> saveTest(District entity);
}