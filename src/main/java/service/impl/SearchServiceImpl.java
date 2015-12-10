package service.impl;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import service.CommonCache;
import service.SearchService;
import utils.Constants;
import utils.ResponsesDTO;
import utils.ReturnCode;
import dao.MongoSearchDao;
import domain.ShopDomain;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private MongoSearchDao mongoSearchDao;

	@Autowired
	private CommonCache commonCacheUtil;

	private Logger log = Logger.getLogger(SearchServiceImpl.class);

	@Override
	public ResponsesDTO searchShop(Long classifyId, double longitude,
			double latitude, double maxDistance, Integer limit,
			Integer pageNum, Integer pageSize, String sort, String sequence) {
		try {
			String sortKey = classifyId + "_" + longitude + "_" + latitude
					+ "_" + limit + "_sort";
			String noSortKey = classifyId + "_" + longitude + "_" + latitude
					+ "_" + limit;

			Map<String, Object> map = new HashMap<String, Object>();

			// sort == null, 数据默认按照距离排序，把数据放到redis里以备分页
			if (StringUtils.isEmpty(sort)) {

				Object value = commonCacheUtil.getObject(noSortKey);

				if (value == null || count(value) == 0) {
					// 查询数据
					ResponsesDTO response = mongoSearchDao
							.searchShop(classifyId, longitude, latitude,
									maxDistance, limit);
					if (response.getCode() == ReturnCode.ACTIVE_FAILURE.code()) {
						return response;
					}

					Object obj = response.getAttach();
					// 返回假分页数据
					if (pageNum != null && pageSize != null) {
						map.put("count", count(obj));
						map.put("content",
								fakePaging(obj, pageNum, pageSize, sequence));
						response.setAttach(map);
					} else {
						// 返回全部数据
						map.put("count", count(obj));
						map.put("content", obj);
						response.setAttach(map);
					}
					commonCacheUtil.setObject(noSortKey,
							Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, obj);
					return response;

				}

				ResponsesDTO response = new ResponsesDTO(
						ReturnCode.ACTIVE_FAILURE);

				response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

				if (pageNum != null && pageSize != null) {
					map.put("count", count(value));
					map.put("content",
							fakePaging(value, pageNum, pageSize, sequence));
					response.setAttach(map);
					return response;
				}
				map.put("count", count(value));
				map.put("content", value);
				response.setAttach(map);
				return response;

				// sort != null, 需要特殊排序
			}

			// 存在需要特殊排序的数据,那么取得数据，返回假分页数据
			Object sortKeyData = commonCacheUtil.getObject(sortKey);

			if (sortKeyData != null && count(sortKeyData) > 0) {
				List<ShopDomain> list = (List<ShopDomain>) commonCacheUtil
						.getObject(sortKey);

				ResponsesDTO response = new ResponsesDTO(
						ReturnCode.ACTIVE_SUCCESS);

				if (pageNum == null || pageSize == null) {
					if ("asc".equalsIgnoreCase(sequence)) {
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					} else {
						Collections.reverse(list);
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					}

				} else {
					map.put("count", list.size());
					map.put("content",
							fakePaging(list, pageNum, pageSize, sequence));
					response.setAttach(map);
				}
				return response;
				// 不存在需要特殊排序的数据
			}
			// 也不存在按照距离排序的数据
			if (commonCacheUtil.get(noSortKey) == null) {
				// 查询数据
				ResponsesDTO response = mongoSearchDao.searchShop(classifyId,
						longitude, latitude, maxDistance, limit);
				if (response.getCode() == ReturnCode.ACTIVE_FAILURE.code()) {
					return response;
				}

				Object obj = response.getAttach();

				// 解析数据，获得结果
				List<ShopDomain> list = parseResult(obj);

				// 排序list
				Collections.sort(list, new SortSearchShopByShopLevel());

				// 把结果放入redis
				commonCacheUtil.setObject(sortKey,
						Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, list);

				response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

				if (pageNum == null || pageSize == null) {
					if ("asc".equalsIgnoreCase(sequence)) {
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					} else {
						Collections.reverse(list);
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					}

				} else {
					map.put("count", list.size());
					map.put("content",
							fakePaging(list, pageNum, pageSize, sequence));
					response.setAttach(map);
				}
				return response;
				// 存在按照距离排序的数据
			} else {

				ResponsesDTO response = new ResponsesDTO(
						ReturnCode.ACTIVE_FAILURE);

				Object value = commonCacheUtil.getObject(noSortKey);

				// 解析数据，获得结果
				List<ShopDomain> list = parseResult(value);

				// 排序list
				Collections.sort(list, new SortSearchShopByShopLevel());

				commonCacheUtil.setObject(sortKey,
						Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, list);

				response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

				if (pageNum == null || pageSize == null) {
					if ("asc".equalsIgnoreCase(sequence)) {
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					} else {
						Collections.reverse(list);
						map.put("count", list.size());
						map.put("content", list);
						response.setAttach(map);
					}
				} else {
					map.put("count", list.size());
					map.put("content",
							fakePaging(list, pageNum, pageSize, sequence));
					response.setAttach(map);
				}

				return response;
			}

		} catch (Exception e) {
			log.error("searchShop by classifyid error", e);
			return new ResponsesDTO(ReturnCode.ACTIVE_EXCEPTION);
		}
	}

	@Override
	public ResponsesDTO searchShop(String key, double longitude,
			double latitude, double maxDistance, Integer limit,
			Integer pageNum, Integer pageSize, String sort, String sequence) {

		try {

			String sortKey = key + "_" + longitude + "_" + latitude + "_"
					+ limit + "_sort";
			String noSortKey = key + "_" + longitude + "_" + latitude + "_"
					+ limit;

			Map<String, Object> map = new HashMap<String, Object>();

			// sort == null, 数据默认按照距离排序，把数据放到redis里以备分页
			if (StringUtils.isEmpty(sort)) {
				if (commonCacheUtil.get(noSortKey) == null) {
					// 查询数据
					ResponsesDTO response = mongoSearchDao.searchShop(key,
							longitude, latitude, maxDistance, limit);
					if (response.getCode() == ReturnCode.ACTIVE_FAILURE.code()) {
						return response;
					} else {
						Object obj = response.getAttach();
						// 返回假分页数据
						if (pageNum != null && pageSize != null) {
							map.put("count", count(obj));
							map.put("content",
									fakePaging(obj, pageNum, pageSize, sequence));
							response.setAttach(map);
						} else {
							// 返回全部数据
							map.put("count", count(obj));
							map.put("content", obj);
							response.setAttach(map);
						}
						commonCacheUtil.setObject(noSortKey,
								Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, obj);
						return response;
					}
				} else {
					ResponsesDTO response = new ResponsesDTO(
							ReturnCode.ACTIVE_FAILURE);

					Object value = commonCacheUtil.getObject(noSortKey);
					response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

					if (pageNum != null && pageSize != null) {
						map.put("count", count(value));
						map.put("content",
								fakePaging(value, pageNum, pageSize, sequence));
						response.setAttach(map);
						return response;
					}
					map.put("count", count(value));
					map.put("content", value);
					response.setAttach(map);
					return response;
				}
				// sort != null, 需要特殊排序
			} else {
				// 存在需要特殊排序的数据,那么取得数据，返回假分页数据
				if (commonCacheUtil.get(sortKey) != null) {
					List<ShopDomain> list = (List<ShopDomain>) commonCacheUtil
							.getObject(sortKey);

					ResponsesDTO response = new ResponsesDTO(
							ReturnCode.ACTIVE_SUCCESS);

					if (pageNum == null || pageSize == null) {
						if ("asc".equalsIgnoreCase(sequence)) {
							map.put("count", list.size());
							map.put("content", list);
							response.setAttach(map);
						} else {
							Collections.reverse(list);
							map.put("count", list.size());
							map.put("content", list);
							response.setAttach(map);
						}

					} else {
						map.put("count", list.size());
						map.put("content",
								fakePaging(list, pageNum, pageSize, sequence));
						response.setAttach(map);
					}
					return response;
					// 不存在需要特殊排序的数据
				} else {
					// 也不存在按照距离排序的数据
					if (commonCacheUtil.get(noSortKey) == null) {
						// 查询数据
						ResponsesDTO response = mongoSearchDao.searchShop(key,
								longitude, latitude, maxDistance, limit);
						if (response.getCode() == ReturnCode.ACTIVE_FAILURE
								.code()) {
							return response;
						}

						Object obj = response.getAttach();

						// 解析数据，获得结果
						List<ShopDomain> list = parseResult(obj);

						// 排序list
						Collections.sort(list, new SortSearchShopByShopLevel());

						// 把结果放入redis
						commonCacheUtil.setObject(sortKey,
								Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, list);

						response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

						if (pageNum == null || pageSize == null) {
							if ("asc".equalsIgnoreCase(sequence)) {
								map.put("count", list.size());
								map.put("content", list);
								response.setAttach(map);
							} else {
								Collections.reverse(list);
								map.put("count", list.size());
								map.put("content", list);
								response.setAttach(map);
							}

						} else {
							map.put("count", list.size());
							map.put("content",
									fakePaging(list, pageNum, pageSize,
											sequence));
							response.setAttach(map);
						}
						return response;
						// 存在按照距离排序的数据
					} else {

						ResponsesDTO response = new ResponsesDTO(
								ReturnCode.ACTIVE_FAILURE);

						Object value = commonCacheUtil.getObject(noSortKey);

						// 解析数据，获得结果
						List<ShopDomain> list = parseResult(value);

						// 排序list
						Collections.sort(list, new SortSearchShopByShopLevel());

						commonCacheUtil.setObject(sortKey,
								Constants.SEARCH_SHOP_VALUE_EXISTS_TIME, list);

						response.setCode(ReturnCode.ACTIVE_SUCCESS.code());

						if (pageNum == null || pageSize == null) {
							if ("asc".equalsIgnoreCase(sequence)) {
								map.put("count", list.size());
								map.put("content", list);
								response.setAttach(map);
							} else {
								Collections.reverse(list);
								map.put("count", list.size());
								map.put("content", list);
								response.setAttach(map);
							}
						} else {
							map.put("count", list.size());
							map.put("content",
									fakePaging(list, pageNum, pageSize,
											sequence));
							response.setAttach(map);
						}
						return response;
					}
				}
			}
		} catch (Exception e) {
			log.error("searchShop by classifyid error", e);
			return new ResponsesDTO(ReturnCode.ACTIVE_EXCEPTION);
		}
	}

	@Override
	public ResponsesDTO incrComment(Long shopId, int comment) {
		try {
			return mongoSearchDao.incrComment(shopId, comment);
		} catch (Exception e) {
			return new ResponsesDTO(ReturnCode.ACTIVE_EXCEPTION);
		}
	}

	/**
	 * 解析搜索返回的数据，去除_id 和 _class
	 * 
	 * @param object
	 * @return
	 */
	private List<ShopDomain> parseResult(Object object) {

		JSONArray jsonArray = JSONArray.fromObject(object);
		List<ShopDomain> list = new LinkedList<ShopDomain>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject attachIteam = JSONObject.fromObject(jsonArray.get(i));

			ShopDomain shopDomain = new ShopDomain();

			shopDomain.setDis(attachIteam.getString("dis"));
			shopDomain.setShopId(attachIteam.getLong("shopId"));
			shopDomain.setShopName(attachIteam.getString("shopName"));
			shopDomain.setOpenTime(attachIteam.getString("openTime"));
			shopDomain.setEndTime(attachIteam.getString("endTime"));
			shopDomain.setCategoryId(attachIteam.getInt("categoryId"));
			shopDomain.setServiceType(attachIteam.getInt("serviceType"));
			shopDomain.setPosition(parseArray(attachIteam.get("position")));
			shopDomain.setAddress(attachIteam.getString("address"));
			shopDomain.setTel(attachIteam.getString("tel"));
			shopDomain.setState(attachIteam.getInt("state"));
			shopDomain.setShopLevel(attachIteam.getInt("shopLevel"));
			shopDomain.setShopPic(attachIteam.getString("shopPic"));
			shopDomain.setLogoPic(attachIteam.getString("logoPic"));
			shopDomain.setRemark(attachIteam.getString("remark"));
			shopDomain.setComment(attachIteam.getInt("comment"));
			list.add(shopDomain);
		}
		return list;
	}

	/**
	 * 计算json数组的长度
	 * 
	 * @param obj
	 * @return
	 */
	private Integer count(Object obj) {
		JSONArray jsonArray = JSONArray.fromObject(obj);
		return jsonArray.size();
	}

	/**
	 * 解析position数组
	 * 
	 * @return
	 */
	private double[] parseArray(Object obj) {
		double[] array = new double[2];
		JSONArray jsonArray = JSONArray.fromObject(obj);
		for (int i = 0; i < jsonArray.size(); i++) {
			double value = jsonArray.getDouble(i);
			array[i] = value;
		}
		return array;
	}

	/**
	 * 返回假分页数据
	 * 
	 * @param obj
	 *            查询获得的jsonArray数据
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            每页多少数据
	 * @param sequence
	 *            升序还是降序
	 * @return
	 */
	private Object fakePaging(Object obj, int pageNum, int pageSize,
			String sequence) {
		JSONArray jsonArray = JSONArray.fromObject(obj);
		// 倒序
		if ("desc".equalsIgnoreCase(sequence)) {
			Collections.reverse(jsonArray);
		}
		int fromIndex = 0 + (pageNum - 1) * pageSize > jsonArray.size() ? jsonArray
				.size() : (0 + (pageNum - 1) * pageSize);
		int toIndex = (fromIndex + pageSize) > jsonArray.size() ? jsonArray
				.size() : (fromIndex + pageSize);
		List result = jsonArray.subList(fromIndex, toIndex);
		return result;
	}

	/**
	 * 返回假分页数据
	 * 
	 * @param obj
	 *            查询获得的jsonArray数据
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            每页多少数据
	 * @param sequence
	 *            升序还是降序
	 * @return
	 */
	private Object fakePaging(List<ShopDomain> list, int pageNum, int pageSize,
			String sequence) {
		// 倒序
		if ("desc".equalsIgnoreCase(sequence)) {
			Collections.reverse(list);
		}
		int fromIndex = 0 + (pageNum - 1) * pageSize > list.size() ? list
				.size() : (0 + (pageNum - 1) * pageSize);
		int toIndex = (fromIndex + pageSize) > list.size() ? list.size()
				: (fromIndex + pageSize);
		List<ShopDomain> result = list.subList(fromIndex, toIndex);

		return result;
	}

	@Override
	public ResponsesDTO insertData(List<ShopDomain> list) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			mongoSearchDao.insertShopData(list);
			response.setCode(ReturnCode.ACTIVE_SUCCESS.code());
			response.setAttach(ReturnCode.ACTIVE_SUCCESS.msg());

			return response;
		} catch (Exception e) {
			return response;
		}
	}

	class SortSearchShopByShopLevel implements Comparator<ShopDomain> {

		@Override
		public int compare(ShopDomain shopDomain1, ShopDomain shopDomain2) {
			// 正序排列
			if (shopDomain1.getShopLevel() > shopDomain2.getShopLevel()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	//
	// public static void main(String[] args) {
	// List list = new LinkedList();
	// for(int i=0; i<100; i++){
	// list.add(i);
	// }
	// new SearchServiceImpl().fakePaging(list, 1, 7, "");
	// }

	@Override
	public ResponsesDTO deleteData(Long shopId) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			return mongoSearchDao.deleteData(shopId);
		} catch (Exception e) {
			e.printStackTrace();
			return response;
		}
	}
}