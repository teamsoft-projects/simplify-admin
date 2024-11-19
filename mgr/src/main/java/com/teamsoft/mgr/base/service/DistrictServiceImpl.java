package com.teamsoft.mgr.base.service;

import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.mgr.base.mapper.DistrictMapper;
import com.teamsoft.mgr.base.model.District;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 区域服务类
 * @author zhangcc
 * @version 2023/10/16
 */
@Slf4j
@Service
public class DistrictServiceImpl extends CommonService<District> implements IDistrictService {
	@Autowired
	public DistrictServiceImpl(DistrictMapper mapper) {
		super.mapper = mapper;
	}

	@Transactional(rollbackFor = Exception.class)
	public Future<String> saveTest(District entity) {
		log.info("进入saveTest的Service实现");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException ignored) {
		}
		Integer ret = mapper.save(entity);
		try {
			Integer.parseInt("aa");
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		}
		log.info("推出saveTest的Service实现");
		return new AsyncResult<>("saveTest的Service方法执行完成");
	}
}