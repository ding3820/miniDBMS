/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *已做完table名稱括號後的第一個名稱有無空白以及能正確地切割出括號內的資料以及type*/
package sql;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

public class CreateTable {

    String temp = "";
    String record[] = new String[1000];
    String sub = "";

    static int total;

     ListMultimap<String, String> Create(String... s) throws Exception {

        int i = 0, j = 0, k, m = 0;

        StringBuilder path = new StringBuilder("C:\\Users");

        ListMultimap<String, String> map = ArrayListMultimap.create();

        for (i = 3; s[i] != null; i += 2) {
           /*if (i == 3) {
               s[3] = s[3].substring(1, s[3].length());
           }*/
           if (s[i + 1].toLowerCase().contains("varchar") || s[i + 1].toLowerCase().contains("int")) {
               map.put("attribute", s[i] + "-" + s[i + 1]);
               map.put(s[i], null);
           } 
           else {
               map.clear();
               throw new Exception("SQL Syntax error! Unknown type...");
           }
            //System.out.println("attribute: "+s[i]);
        }
       /* 		for (String value : myMultimap.values()) {
			System.out.println(value);
		}*/
        //System.out.println(i);
        List<String> Values = map.get("attribute");
        System.out.println(Values);

        return map;

    }

}
