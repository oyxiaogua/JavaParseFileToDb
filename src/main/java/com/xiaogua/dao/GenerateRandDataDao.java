package com.xiaogua.dao;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateRandDataDao {
	private static String firstName = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏" + "水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷"
			+ "贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛" + "汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危"
			+ "江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁" + "贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄魏加封芮羿储靳汲邴糜松井段"
			+ "富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘姜詹束龙叶幸司" + "韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰"
			+ "郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨" + "居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠"
			+ "须丰巢关蒯相查后江红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公" + "冶宗政濮阳淳于仲孙太叔申屠公孙乐正轩辕令狐钟离闾丘长孙慕容鲜于宇文司徒司空亓官司寇仉督子车"
			+ "颛孙端木巫马公西漆雕乐正壤驷公良拓拔夹谷宰父谷粱晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊" + "舌微生岳帅缑亢况后有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福百家姓续";
	private static String secondName = "计伏成戴谈宋茅庞熊纪屈项祝董杜阮" + "蓝闵席季麻强贾路娄危江童颜郭刚勇毅俊峰强军平保东文辉力明"
			+ "永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清" + "飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮" + "会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之"
			+ "轮翰朗伯宏言若鸣朋斌梁" + "栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风梅盛" + "林刁钟徐邱骆高夏蔡田樊胡凌霍虞万" + "支柯咎管卢莫经房裘缪干解应宗宣丁"
			+ "贲邓郁单杭洪包诸左石崔吉钮龚程嵇" + "邢滑裴陆荣翁荀羊於惠甄加封芮羿储秀娟英华慧巧美娜静淑惠珠翠" + "雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月"
			+ "莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷" + "姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦" + "岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥"
			+ "筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Reader read(String resourceName, String encoding) throws Exception {
		return new InputStreamReader(new FileInputStream(resourceName), encoding);
	}

	public static String generateRandStr(String[] strArr) {
		return strArr[new Random().nextInt(strArr.length)];
	}

	public static String getFirstName() {
		Random random = new Random();
		return String.valueOf(firstName.charAt(random.nextInt(firstName.length())));
	}

	public static String getSecondName() {
		Random random = new Random();
		return String.valueOf(secondName.charAt(random.nextInt(secondName.length())))
				+ String.valueOf(secondName.charAt(random.nextInt(secondName.length())));
	}

	public static String generateRandDate(String startDateStr, String endDateStr) throws Exception {
		Date startDate = df.parse(startDateStr);
		Date endDate = df.parse(endDateStr);
		return generateRandDate(startDate, endDate);
	}

	public static String generateRandDate(Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		java.util.Date randDate = new java.util.Date(generateRandLong(startTime, endTime));
		return df.format(randDate);
	}

	public static long generateRandLong(long start, long end) {
		return start + (long) (Math.random() * (end - start));
	}
}
