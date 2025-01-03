package com.chhei.mall.ware.service.impl;

import com.chhei.common.dto.SkuHasStockDto;
import com.chhei.common.utils.R;
import com.chhei.mall.ware.feign.ProductFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.Query;

import com.chhei.mall.ware.dao.WareSkuDao;
import com.chhei.mall.ware.entity.WareSkuEntity;
import com.chhei.mall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;

@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    private WareSkuDao skuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        // 查询的条件 skuId=10&wareId=1
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 入库操作
     * @param skuId 商品编号
     * @param wareId 仓库编号
     * @param skuNum  采购商品的数量
     */
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 判断是否有改商品和仓库的入库记录
        List<WareSkuEntity> list = skuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(list == null || list.size() == 0){
            // 如果没有就新增商品库存记录
            WareSkuEntity entity = new WareSkuEntity();
            entity.setSkuId(skuId);
            entity.setWareId(wareId);
            entity.setStock(skuNum);
            entity.setStockLocked(0);
            try {
                // 动态的设置商品的名称
                R info = productFeignService.info(skuId); // 通过Feign远程调用商品服务的接口
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if(info.getCode() == 0){
                    entity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }
            skuDao.insert(entity); // 插入商品库存记录
        }else{
            // 如果有就更新库存
            skuDao.addStock(skuId,wareId,skuNum);
        }
    }

    @Override
    public List<SkuHasStockDto> getSkusHasStock(List<Long> skuIds) {
        log.info("查询库存的 SKU IDs: {}", skuIds);

        List<SkuHasStockDto> list = skuIds.stream().map(skuId -> {
            Long count = baseMapper.getSkuStock(skuId);

            // 防御性处理：确保 `count` 不为 null，避免异常
            if (count == null) {
                log.warn("SKU ID: {} 查询库存返回 null，设置为 0", skuId);
                count = 0L;
            }

            log.info("SKU ID: {}, 库存数量: {}", skuId, count);

            SkuHasStockDto dto = new SkuHasStockDto();
            dto.setSkuId(skuId);
            dto.setHasStock(count > 0); // 库存大于 0 时才设置为有库存

            return dto;
        }).collect(Collectors.toList());

        log.info("返回的库存信息: {}", list);
        return list;
    }


}