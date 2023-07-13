# json文件转java文件
> 造轮子。
>
> 关于将json文件转为java文件，搜索到最多的结果是GsonFormat，使用起来有点限制
>
> 就东拼西凑出了，适应自己目前需求的这个功能

### 需求是

要对接第三方的接口。

1. 而第三方接口的body体中，请求参数有那么一老坨，甚至还有字段套娃的情况。

2. 字段命名还是下划线的方式。作为java开发工程师来说，现在java中的属性命名一般是驼峰形式的，这就需要在参数传递时，将驼峰转为下划线的形式，使用序列化工具@JSONField来解决
3. String类型的字段手动拼接的注释



### 请求参数

```json
[
	{
		"class_name": "Aa",
		"fpqqlsh": "发票请求唯一流水号",
		"nsrmc": "开票方纳税人名称",
		"nsrsbh": "开票方纳税人识别号",
		"kplx": "开票类型",
		"xhfmc": "销货方名称",
		"xhf_nsrsbh": "销货方纳税人识别号",
		"xhf_dz": "销货方地址",
		"xhf_dh": "销货方电话",
		"xhf_yh": "销货方银行",
		"xhf_zh": "销货方银行账号",
		"ghfmc": "购货方名称",
		"ghf_nsrsbh": "购货方纳税人识别号",
		"ghf_dz": "购货方地址",
		"ghf_dh": "购货方电话",
		"ghf_yh": "购货方银行",
		"ghf_zh": "购货方银行账号",
		"ghf_email": "购货方邮箱",
		"ghf_sj": "购货方手机号",
		"kpy": "开票员",
		"jbr": "经办人",
		"jbr_sj": "经办人手机",
		"jbr_sflx": "经办人身份类型",
		"jbr_sfzhm": "经办人身份证号",
		"sky": "收款员",
		"fhr": "复核人",
		"fpzl_dm": "发票种类代码",
		"xsfzrr_bz": "销售方自然人标志",
		"gmfzrr_bz": "购买方自然人标志",
		"zzfp_bz": "纸质发票标志",
		"kphjje": "开票合计金额",
		"hjbhsje": "合计不含税金额",
		"hjse": "合计税额",
		"bz": "备注",
		"hzqrdbh": "红字确认单编号",
		"hzqrduuid": "红字确认单 uuid",
		"yfp_hm": "原发票号码",
		"yfp_dm": "原发票代码",
		"ykprq": "原开票日期",
		"yfpzl_dm": "原发票种类代码",
		"is_dae": "是否开具大额发票",
		"ddh": "订单号",
		"fjh": "分机号",
		"chyy": "冲红原因",
		"hsbz": "含税标志",
		"ncpsgzjlx": "农产品收购证件类型",
		"kpms": "开票模式",
		"mx": [
			{
				"xh": "商品行序号",
				"fphxz": "发票行性质",
				"xmmc": "项目名称",
				"spmc": "商品名称",
				"spbmjc": "税收分类编码简称",
				"xmdw": "项目单位",
				"ggxh": "规格型号",
				"xmsl": "项目数量",
				"hsbz": "含税标志",
				"xmdj": "项目单价",
				"spbm": "商品编码",
				"yhzcbs": "优惠政策标识",
				"lslbs": "零税率标识",
				"zzstsgl": "增值税特殊管理",
				"xmje": "项目金额",
				"slv": "税率",
				"se": "税额",
				"lzfpxh": "对应蓝字发票明细序号"
			}
		],
		"fjys": [
			{
				"fjysmc": "附加要素名称",
				"fjysz": "附加要素值",
				"sjlx": "数据类型"
			}
		],
		"hwys_ts_list": [
			{
				"xh": "序号",
				"ysgjzl": "运输工具种类",
				"ysgjhp": "运输工具号牌",
				"qyd": "起运地",
				"ddd": "到达地",
				"yshwmc1": "运输货物名称"
			}
		],
		"jzfw_tdys": {
			"tdzzsxmbh": "土地增值税项目编号",
			"jzfwfsd": "建筑服务发生地",
			"full_address": "详细地址",
			"jzxmmc": "建筑项目名称",
			"kdsbz": "跨地（市）标志"
		},
		"jzfw_fpmxlist": [
			{
				"jzfwfsd": "建筑服务发生地",
				"full_address": "详细地址",
				"jzxmmc": "建筑项目名称",
				"xh": "商品行序号",
				"fphxz": "发票行性质",
				"xmmc": "项目名称",
				"xmdw": "项目单位",
				"ggxh": "规格型号",
				"xmsl": "项目数量",
				"hsbz": "含税标志",
				"xmdj": "项目单价",
				"spbm": "商品编码",
				"yhzcbs": "优惠政策标识",
				"lslbs": "零税率标识",
				"zzstsgl": "增值税特殊管理",
				"xmje": "项目金额",
				"sl": "税率",
				"se": "税额"
			}
		],
		"bdc_tdys": {
			"wqhtbabh": "不动产单元代码/网签合同备案编号",
			"bdcdz": "不动产地址",
			"kdsbz": "跨地(市)标志",
			"tdzzsxmbh": "土地增值税项目编号",
			"hdjsjg": "核定计税价格",
			"sjcjhsje": "实际成交含税金额",
			"cqzsh": "房屋产权证书号/不动产权证号",
			"mjdw": "面积单位",
			"zlqqz": "租赁期起止"
		},
		"bdc_mx_tdys_list": [
			{
				"autofocus": "自动给定焦点",
				"handle_select": "手动选择商品",
				"auto_slv": "",
				"slv_loading": "",
				"bcmsbz": "报错描述标志",
				"jzjtlx_dm": "即征即退类型代码",
				"slvbcbz": "税率报错标志",
				"cqzsh": "房屋产权证书号/不动产权证号",
				"xh": "序号",
				"fphxz_dm": "发票行性质代码",
				"zkxh1": "折扣序号",
				"xmmc": "项目名称",
				"fullxmmc": "*完整项目名称",
				"hwhyslwfwmc": "货物或应税劳务、服务名称",
				"sphfwssflhbbm": "商品和服务税收分类合并编码",
				"spfwjc": "商品服务简称",
				"ggxh": "规格型号",
				"dw": "单位",
				"spsl": "商品数量",
				"dj": "单价",
				"je": "金额",
				"hsje": "含税金额",
				"slv": "税率/征收率",
				"se": "税额",
				"kce": "扣除额",
				"jzjtbl": "即征即退比例",
				"jzjtcsfl": "即征即退超税负率",
				"lslbz": "零税率标志",
				"xsyhzcbz": "享受优惠政策标志",
				"ssyhzclx_dm": "享受优惠政策类型代码",
				"zzstsgl": "增值税特殊管理",
				"tdyslx_dm": "特定要素类型代码",
				"tdzsfs_dm": "特定征收方式代码",
				"zspmDm": "征收品目代码",
				"zzszcyj_dm": "增值税政策依据代码",
				"hzfpdylzfpmxxh": "红字发票对应蓝字发票明细序号",
				"hsdj": "含税单价",
				"bhsdj": "不含税单价",
				"bdcqzh": "不含税金额",
				"slv_options": [
					{
						"label": "百分数税率",
						"value": "小数税率"
					}
				]
			}
		]
	},
	{
		"class_name": "Bbb",
		"invoiceTypeCode": "蓝字发票票种代码",
		"lzfpqd_hm": "蓝字发票数电号码",
		"lrfsf": "录入方身份",
		"xhf_nsrsbh": "销售方纳税人识别号 ",
		"xhf_mc": "销售方名称",
		"ghf_nsrsbh": "购买方纳税人识别号",
		"ghf_mc": "购买方名称",
		"lzfp_dm": "蓝字发票代码",
		"lzfp_hm": "蓝字发票号码",
		"lzkprq": "蓝字开票日期",
		"lzhjje": "蓝字合计金额",
		"lzhjse": "蓝字合计税额",
		"hzcxje": "红字撤销金额",
		"hzcxse": "红字撤销税额",
		"chyydm": "冲红原因代码",
		"jshjdx": "价税合计大写",
		"jshj": "价税合计",
		"sfzzfpbz": "是否纸质发票标志",
		"fplydm": "发票来源代码",
		"gjbq": "归集标签",
		"sfypsdnsrbz": "是否用票试点纳税人标志",
		"ybfhcbz": "部分冲红标志",
		"qrjkpbz": "确认即开票标志",
		"qrjkpxgbz": "确认即开票修改标志",
		"qrjkpBztxBz": "确认及开票不再提醒标志",
		"hsbz": "含税标志",
		"hzqrxxmxlist": [
			{
				"lzfp_dm": "蓝字发票代码",
				"lzfp_hm": "蓝字发票号码",
				"lzmxxh": "蓝字明细序号",
				"xh": "序号",
				"sphfwssflhbbm": "商品和服务税收分类合并编码",
				"spmc": "商品名称",
				"spfwjc": "商品服务简称",
				"xmmc": "项目名称",
				"ggxh": "规格型号",
				"dw": "单位",
				"xmdj": "项目单价",
				"xmsl": "项目数量",
				"xmje": "项目金额",
				"slv": "税率",
				"se": "税额",
				"zeroTaxSchemeMark": "优惠政策标识",
				"oldje": "原发票金额",
				"oldfpspsl ": "原发票项目数量",
				"oldfpspdj": "原发票项目单价",
				"taxfpspdj": "含税发票单价",
				"taxje": "含税金额"
			}
		]
	}
]
```

