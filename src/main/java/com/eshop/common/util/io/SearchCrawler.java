package com.eshop.common.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

// 搜索Web爬行者  
public class SearchCrawler implements Runnable {
	public static final String patternString = "href=\"(http://item.yhd.com/item/[0-9]+)\"";
	public static Pattern pattern = Pattern.compile(patternString);
	String startUrl;// 开始搜索的起点
	String path;
	boolean caseSensitive = false;// 是否区分大小写
	final String END = "?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";

	public SearchCrawler(String startUrl, String path) {
		this.startUrl = startUrl;
		this.path = path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void run() {// 启动搜索线程
		crawl(startUrl);
	}

	// 搜索下载Web页面的内容，判断在该页面内有没有指定的搜索字符串
	private boolean searchStringMatches(String url) {
		Document doc;
		String urls;
		try {
			System.out.println(url);
			doc = Jsoup.connect(url).get();
			Elements elements = doc.select("a.search_prod_img");
			if (elements.size() > 0) {
				for (int i = 0; i < elements.size(); i++) {
					Element element = elements.get(i);
					urls = element.attr("href");
					getProductByUrl(urls);
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	// 执行实际的搜索操作
	public void crawl(String startUrl) {
		String s = startUrl;
		int i = 1;

		while (i > 0) {
			System.out.println(i);
			if (searchStringMatches(s)) {
				String str = downloadPage(startUrl);
				JSONObject object = JSONObject.parseObject(str);
				String jsonValue = object.getString("value");
				Matcher matcher = pattern.matcher(jsonValue);
				matcher.groupCount();
				String subUrl = "";
				String currentUrl;
				while (matcher.find()) {
					currentUrl = matcher.group(1);
					if (subUrl.equals(currentUrl)) {
						continue;
					}
					subUrl = currentUrl;
					getProductByUrl(currentUrl);
				}
				s = s.replace("-p" + (i) + "-", "-p" + (i + 1) + "-");
				i++;
			} else {
				i = 0;
			}
		}
		System.out.println(startUrl + " ===== is end……");
	}

	void getProductByUrl(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			//System.out.println(url);
			Element field = doc.getElementById("productMainName");
			if (field != null) {
				String fileName = field.text();
				String filePath = doc.getElementById("J_prodImg").attr("src");
				download(filePath, path
						+ fileName.replaceAll("/", "-").replaceAll("\\*", "_")
						+ ".jpg");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void download(String urlString, String filename) {
		// 构造URL
		URL url;
		OutputStream os = null;
		InputStream is = null;
		try {
			url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			os = new FileOutputStream(filename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String downloadPage(String pageUrl) {
		try {
			URL url = new URL(pageUrl.replace("http://list.yhd.com/",
					"http://list.yhd.com/searchPage/") + END);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
			return pageBuffer.toString();
		} catch (Exception e) {
		}
		return null;
	}

	// 主函数
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>(){{
			put("http://list.yhd.com/c34660-0-81605/b/a-s1-v0-p1-price-d0-f0d-m1-rt0-pid-mid0-k/","H:/goods_type/休闲零食/豆干/");
			put("http://list.yhd.com/c34669-0-81614/b/a133123-s1-v0-p1-price-d0-f0d-m1-rt0-pid-mid0-k/","H:/goods_type/休闲零食/猪肉/猪肉脯/");
			put("http://list.yhd.com/c34669-0-81614/b/a133123-s1-v0-p1-price-d0-f0d-m1-rt0-pid-mid0-k/","H:/goods_type/休闲零食/猪肉/猪肉脯/");
		}};
		Set<String> set = map.keySet();
		for(String url : set){
			SearchCrawler crawler = new SearchCrawler(url,map.get(url));
			Thread search = new Thread(crawler);
			System.out.println(url+" Start searching...");
			search.start();
		}
	}
}
