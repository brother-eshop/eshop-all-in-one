package com.eshop.service.impl.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.GoodsDao;
import com.eshop.dao.mongodb.ShopAndGoodsDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.Goods;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.ShopAndGoodsService;

@Service("shopAndGoodsService")
public class ShopAndGoodsServiceImpl extends AbstractService<ShopAndGoods, String> implements
		ShopAndGoodsService {

	@Autowired
	private ShopAndGoodsDao shopAndGoodsDao;
	
	@Autowired
	private GoodsDao goodsDao;

	@Override
	public DAO<ShopAndGoods, String> getDao() {
		return shopAndGoodsDao;
	}

	@Override
	public void insertShopAndGoods(ShopAndGoods sad) {
		shopAndGoodsDao.insert(sad);
	}

	@Override
	public List<ShopAndGoods> getShopperGoods(String userId,ShopAndGoods sGoods,PageEntity page) {
		Query query = new Query(Criteria.where("userId").is(userId));
		String goodsName = sGoods.getGoodsName();
		String goodsCode = sGoods.getGoodsCode();
		String goodsManufacturer = sGoods.getManufacturer();
		if (!"".equals(goodsName) && goodsName != null) {
			query.addCriteria(Criteria.where("goodsName").regex(
					Pattern.compile("^.*" + goodsName + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(goodsCode) && goodsCode != null) {
			query.addCriteria(Criteria.where("goodsCode").regex(
					Pattern.compile("^" + goodsCode + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(goodsManufacturer) && goodsManufacturer != null) {
			query.addCriteria(Criteria.where("manufacturer").regex(
					Pattern.compile("^.*" + goodsManufacturer + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		int count = (int) this.getShopperGoodsCount(query);
		page.setTotalResultSize(count);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "code"));
		query.with(new Sort(orders));
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return shopAndGoodsDao.findList(query, ShopAndGoods.class);
	} 
	
	@Override
	public long getShopperGoodsCount(Query query) {
		return shopAndGoodsDao.size(query, ShopAndGoods.class);
	}
	
}