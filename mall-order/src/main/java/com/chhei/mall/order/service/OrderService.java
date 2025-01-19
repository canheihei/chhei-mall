package com.chhei.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.exception.NoStockExecption;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.order.entity.OrderEntity;
import com.chhei.mall.order.vo.OrderConfirmVo;
import com.chhei.mall.order.vo.OrderResponseVO;
import com.chhei.mall.order.vo.OrderSubmitVO;

import java.util.Map;

/**
 * 订单
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:11:20
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

	OrderConfirmVo confirmOrder();

	OrderResponseVO submitOrder(OrderSubmitVO vo) throws NoStockExecption;
}

