/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import com.google.common.collect.ListMultimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shasha1001
 */
public class COutput {

    //for dot process ex. a.author --> table = author, attribute = author
    String table;
    String attribute;
    String pt_attribute;
    String aggre;
    boolean aggre_b = false;

    List<Integer> correct = new ArrayList<>(); // store the index of tuples under certain conditions
    List<Integer> correct2 = new ArrayList<>();

    void atri(String s[], int i, MyLinkedMap<String, String> tables, Map<String, ListMultimap<String, String>> map) {

        //System.out.println("----"+aggre);
        //System.out.println("----"+aggre_b);
        if (aggre_b && s[i].contains("(") && (s[i].contains(")"))) {
            
                pt_attribute = s[i].substring(s[i].indexOf("(") + 1, s[i].indexOf(")"));
        }
        else {
            pt_attribute = s[i];
        }
        System.out.println(pt_attribute);

        String a, b;

        //process "." ---> atri[0](attri)  atri[1](table-real name)
        if (s[i].contains(".")) {
            //a-->table alias
            //b-->attribute
            int dot = s[i].length();
            for (int ll = 0; ll < s[i].length(); ll++) {
                if (s[i].charAt(ll) == '.') {
                    dot = ll;
                }
            }

            a = s[i].substring(0, dot);
            b = s[i].substring(dot + 1, s[i].length());

            attribute = b;

            //alias --> real name
            boolean alias = false;
            int ss = 0;
            for (i = 0; i < tables.size(); i++) {
                if (a.equalsIgnoreCase(tables.getValue(i))) {
                    ss = i;
                    alias = true;
                    break;
                }
            }
            if (alias == true) {
                table = tables.getKey(ss);
            }
            else {
                table = a;
            }
            System.out.println("in COutput "+ alias);
            System.out.println("in COutput "+ tables);
            System.out.println("in COutput "+ a);
            System.out.println("in COutput "+ table);
            System.out.println("in COutput "+ attribute);

        }
        else {
            attribute = pt_attribute; //name of attribute
            if (attribute.equals("*")) {
                table = tables.getKey(tables.size());
            }

            for (int taab = 0; taab < tables.size(); taab++) {
                for (int aaa = 0; aaa < map.get(tables.getKey(taab)).get("attribute").size(); aaa++) {
                    //System.out.println("s[i]"+ s[i]);
                    //System.out.println(map.get(tables.getKey(taab)).get("attribute").get(aaa));
                    if (map.get(tables.getKey(taab)).get("attribute").get(aaa).contains(attribute)) {
                        System.out.println("s[i]" + s[i]);
                        table = tables.getKey(taab);
                    }
                }
            }
            //System.out.printlntable);

        }
        //System.out.println("attributeeeeeeeeeeeeeeeeee="+attribute);
    }

    List<Integer> RC(String table1, MyLinkedMap<String, String> tables, List<Integer> correct, List<Integer> correct2) {

        if (table1.equalsIgnoreCase(tables.getKey(0))) {
            return correct;
        }
        else {
            return correct2;
        }
    }

    void COutput(Map<String, ListMultimap<String, String>> map, String table1, int i, MyLinkedMap<String, String> tables, boolean compare) {

        System.out.println("zzzzzzzz" + table1);
        System.out.println("zzzzzzzz" + compare);

        if (!compare) {
            correct.add(i);
        }
        else {
            correct2.add(i);
        }

    }
}
