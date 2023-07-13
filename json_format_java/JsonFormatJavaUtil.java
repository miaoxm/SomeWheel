package com.example.validated.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author miaoxm
 * @date 2023-07-13
 */
public class JsonFormatJavaUtil {

    /**
     * 包名-路径
     */
    public static final String PACKAGE_NAME = "com.example.validated.entity.gen";

    /**
     * 文件生成位置
     */
    public static final String DIRECTORY = "D:\\Desktop\\gen";

    /**
     * 文件夹内已经存在的java类
     */
    public static final List<String> DIRECTORY_FILE = new ArrayList<>();

    /**
     * 读取需要将json转化为java的文件
     */
    public static final String JSON_FILE_PATH = "D:\\Desktop\\json.json";

    public static void main(String[] args) {

        List<String> fileList = directoryFile(DIRECTORY);
        DIRECTORY_FILE.addAll(fileList);

        File jsonFile = new File(JSON_FILE_PATH);
        String jsonData = getStr(jsonFile);

        List<JSONObject> list = JSONObject.parseArray(jsonData, JSONObject.class);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (JSONObject item : list) {
            // 这里json文件中需要单独指定最外层的java类命名
            String className = item.getString("class_name");
            item.remove("class_name");

            body(className, item);
        }
    }

    /**
     * 读取文件
     *
     * @param jsonFile
     * @return
     */
    public static String getStr(File jsonFile) {
        String jsonStr = "";
        try {
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 转换方法(拼接实现)
     *
     * @param fileName java文件命名
     * @param item 字段
     */
    public static void body(String fileName, JSONObject item) {

        // String类型的字段直接拼接
        Map<String, String> stringField = new LinkedHashMap<>();

        // Object类型字段进行递归处理
        Map<String, String> objectField = new LinkedHashMap<>();

        Set<String> fields = item.keySet();
        for (String key : fields) {
            Object value = item.get(key);
            if (value instanceof String) {
                // key是下划线(需要转驼峰)value存的是字段注释
                stringField.put(key, value.toString());
            } else if (value instanceof JSONArray) {
                // array类型直接取第一个Object去处理
                JSONArray jsonArray = (JSONArray) value;
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                // 获取import对象值
                String importFileName = getImportFileName(key);
                body(importFileName, jsonObject);

                // 对象类型存的是字段值和import对象值
                objectField.put(key, importFileName);
            } else if (value instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) value;
                String className = StrUtil.upperFirst(StrUtil.toCamelCase(key));
                body(className, jsonObject);

                // 对象类型存的是字段值和import对象值
                objectField.put(key, className);
            }
        }

        // 当前文件中已经存在了不再创建
        if (DIRECTORY_FILE.contains(fileName)) {
            return;
        }

        List<String> dataList = new ArrayList<>();
        dataList.add("package " + PACKAGE_NAME + ";");
        dataList.add("");

        dataList.add("import com.alibaba.fastjson.annotation.JSONField;");
        dataList.add("import lombok.Data;");
        dataList.add("");

        dataList.add("import java.util.List;");
        dataList.add("");

        // 使用了lombok
        dataList.add("@Data");
        dataList.add("public class " + fileName + " {");
        dataList.add("");
        if (!stringField.isEmpty()) {
            for (String s : stringField.keySet()) {
                String remark = stringField.get(s);
                dataList.add("    /**");
                dataList.add("     * " + remark);
                dataList.add("     */");
                dataList.add("    @JSONField(name = \"" + s +"\")");
                String field = StrUtil.toCamelCase(s);

                dataList.add("    private String " + field + ";");
                dataList.add("");
            }
        }
        if (!objectField.isEmpty()) {
            for (String s : objectField.keySet()) {
                String field = StrUtil.toCamelCase(s);
                String importFile = objectField.get(s);
                dataList.add("    @JSONField(name = \"" + s +"\")");
                if (StrUtil.containsIgnoreCase(s, "list")) {
                    dataList.add("    private List<" + importFile + "> " + field + ";");
                } else {
                    dataList.add("    private " + importFile + " " + field + ";");
                }
                dataList.add("");
            }
        }
        dataList.add("}");

        try {
            // 写入文件
            createFile(fileName, dataList);
            DIRECTORY_FILE.add(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createFile(String fileName, List<String> dataList) throws Exception {
        File file = new File(DIRECTORY);
        if (!file.exists()) {
            //创建文件夹
            if (file.mkdirs()) {
                System.out.println("创建" + DIRECTORY + "成功");
            } else {
                System.out.println("创建" + DIRECTORY + "失败");
            }
        }
        String filePath = DIRECTORY + "\\" + fileName + ".java";
        System.out.println("写入文件::" + filePath);
        File file2 = new File(filePath);
        if (file2.exists()) {
            System.out.println("该文件已存在,不能重复创建");
        } else {
            //创建文件
            if (file2.createNewFile()) {
                System.out.println("文件创建成功");
                //写入内容
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                for (String s : dataList) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.close();
            } else {
                System.out.println("文件创建失败");
            }
        }
    }

    public static List<String> directoryFile(String filePath) {
        List<String> existFileList = new ArrayList<>();
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String fileName = file.getName();
                if (!fileName.contains(".java")) {
                    continue;
                }
                existFileList.add(fileName.substring(0, fileName.lastIndexOf(".")));
            }
        }
        return existFileList;
    }

    /**
     * list嵌套实体
     * 需要依赖实体,将字段后面的list去掉
     * 如字段item_list、itemlist=>Item
     *
     * @param key
     * @return
     */
    public static String getImportFileName(String key) {
        if (key.contains("list")) {
            int last = key.lastIndexOf("_");
            int listIndex = key.lastIndexOf("list");
            String className;
            if (listIndex - last == 1) {
                className = key.substring(0, last);
            } else {
                className = key.substring(0, listIndex);
            }
            return StrUtil.upperFirst(StrUtil.toCamelCase(className));
        } else {
            return StrUtil.upperFirst(StrUtil.toCamelCase(key));
        }
    }
}
