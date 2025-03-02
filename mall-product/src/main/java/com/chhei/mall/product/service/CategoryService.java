package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.CategoryEntity;
import com.chhei.mall.product.vo.Catalog2VO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<CategoryEntity> queryPageWithTree(Map<String, Object> params);

	void removeCategoryByIds(List<Long> ids);

	Long[] findCatelogPath(Long catelogId);

	void updateDetail(CategoryEntity categoryEntity);

	List<CategoryEntity> getLeve1Category();

	Map<String, List<Catalog2VO>> getCatelog2JSON();
}

