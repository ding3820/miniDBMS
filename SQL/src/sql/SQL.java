/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//change the filePath if needed.
//#56 what if x ( y? should we move the parentheses to y?
//#28 string pathr why "" nothing?
package sql;

import com.google.common.collect.ArrayListMultimap;
import java.util.*;
import java.util.Arrays;

import com.google.common.collect.ListMultimap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author shasha1001
 */
public class SQL {

    public static void main(String[] args) {

        COutput c = new COutput();
        String command = "sql>";
        String s[] = new String[1000];
        Scanner sc = new Scanner(System.in); //import java.util.*
        String last_w;
        String str = null;
        String pr = null;
        CreateTable CT = new CreateTable();
        boolean flag = false; //check for duplicate
        boolean InsertWithKey = false;

        Map<String, ListMultimap<String, String>> map = new HashMap<>();

        String regEx = "[`~!@#$%^&()+=|{}':',\\[\\]/?~！@#￥%……&（）——+|{}【】『：」「』。，、？_]";  // delete *
        Pattern p = Pattern.compile(regEx);

        //words put into array
        while (true) {

            int i = 0, j = 0, k = 0, l = 0;
            int temp = 0, index = 0;
            int cal = 0;
            flag = false;
            InsertWithKey = false;
            boolean samekey = false;
            boolean error = false;
            Arrays.fill(s, null);
            System.out.print(command);
            //preprocessing: put words in array, ; deletion
            while (true) {   //�L���j��
                s[i] = sc.next();
                last_w = String.valueOf(s[i].charAt(s[i].length() - 1));
                // ;-->"  "
                if (last_w.equals(";")) {
                    if (s[i].length() > 1) {
                        s[i] = s[i].substring(0, s[i].length() - 1);
                    }
                    else {
                        s[i] = null;
                    }
                    break;
                }
                i++;
            }
            while (true) {

                //preprocessing: parenthesis
                if ((s[2].charAt(s[2].length() - 1) + "").equals("(")) {
                    // x( y ---> x  (y 
                    if ((s[2].charAt(s[2].length() - 1) + "").equals("(")) {
                        s[2] = s[2].substring(0, s[2].length() - 1);
                        s[3] = "(" + s[3];
                    } // x(y ---> x  (y
                    else if (!s[2].contains("(") && (s[2].charAt(s[2].length() - 1) + "").equals("(")) {
                        while (true) {
                            if ((s[2].charAt(cal) + "").equals("(")) {
                                break;
                            }
                            cal++;
                        }
                        s[3] = s[3].substring(cal, s[2].length()) + s[3];
                        s[2] = s[2].substring(0, cal);
                    }
                }

                // handle x ( y 
                for (k = 0; s[k] != null; k++) {
                    if (s[k].equals("(")) {
                        for (j = k; s[j + 1] != null; j++) {
                            if (j == k) {
                                s[j] = s[j] + s[j + 1];
                            }
                            else {
                                s[j] = s[j + 1];
                            }
                        }
                        s[j] = null;

                    }
                }
                /*
                if(s[0].equalsIgnoreCase("CREATE") &&!s[3].contains("(")){
                    System.out.println("SQL syntax error! Losing left parenthesis...");
                    break;
                }
                if(s[0].equalsIgnoreCase("INSERT") &&!s[4].contains("(")){
                    System.out.println("SQL syntax error! Losing left parenthesis...");
                    break;
                }*/

                for (j = 0; s[j] != null; j++) {
                    if (s[j].equalsIgnoreCase("VALUES")) {
                        if (!s[j + 1].contains("(")) {
                            System.out.println("SQL syntax error! Losing left parenthesis...");
                            break;
                        }

                    }
                }
                if (s[0].equalsIgnoreCase("CREATE") && s[k - 2].contains(",") || s[k - 1].contains(",")) {
                    System.out.println("SQL syntax error! Found unnecessary semicolon...");
                    break;
                }
                // remove "," "(" ")"
                for (j = 0; s[j] != null; j++) {
                    if (s[0].equalsIgnoreCase("SELECT")) {
                        if (s[j].contains(",")) {
                            s[j] = s[j].substring(0, s[j].length() - 1);
                        }
                    }
                    else {
                        if (String.valueOf(s[j].charAt(0)).equals("(")) {
                            s[j] = s[j].substring(1, s[j].length());
                        }
                        if (s[j].equals(")")) {
                            s[j] = null;
                            break;
                        }
                        if (String.valueOf(s[j].charAt(s[j].length() - 1)).equals(",") || (String.valueOf(s[j].charAt(s[j].length() - 1)).equals(")") && !(Character.isDigit(s[j].charAt(s[j].length() - 2))) && s[0].equalsIgnoreCase("CREATE")) || (String.valueOf(s[j].charAt(s[j].length() - 1)).equals(")") && s[0].equalsIgnoreCase("INSERT"))) {
                            s[j] = s[j].substring(0, s[j].length() - 1);
                        }
                    }
                }
                // handle case like: '  John Snow  ' 
                for (j = 0; s[j] != null; j++) {
                    if (String.valueOf(s[j].charAt(0)).equals("'") && s[j].length() == 1) {
                        for (k = j; s[k + 1] != null; k++) {
                            if (k == j) {
                                s[k] = s[k] + s[k + 1];
                            }
                            else {
                                s[k] = s[k + 1];
                            }
                        }
                        s[k] = null;
                    }

                }
                // handle case like: 'John Snow' 
                String cache;
                int move;
                for (j = 0; s[j] != null; j++) {
                    if (s[j].contains("'")) {
                        cache = s[j];
                        int m;
                        if (String.valueOf(s[j].charAt(0)).equals("'") && String.valueOf(s[j].charAt(s[j].length() - 1)).equals("'")) {
                            if (!s[0].equalsIgnoreCase("SELECT")) {
                                s[j] = s[j].substring(1, s[j].length() - 1);
                            }
                        }
                        else {
                            for (k = j + 1; s[k] != null; k++) {
                                if (s[k].contains("'")) {
                                    break;
                                }
                            }
                            for (l = j; l < k; l++) {
                                cache = cache + " " + s[l + 1];
                            }
                            s[j] = cache;
                            move = k - j;
                            for (m = j + 1; s[m + move] != null; m++) {
                                s[m] = s[m + move];
                            }
                            for (; s[m] != null; m++) {
                                s[m] = null;
                            }
                            if (!s[0].equalsIgnoreCase("SELECT")) {
                                //System.out.println("**********");
                                s[j] = s[j].substring(1, s[j].length() - 1);
                            }
                        }
                    }
                }
                for (j = 0; s[j] != null; j++) {
                    if (s[j].equalsIgnoreCase("PRIMARY") && s[j + 1].equalsIgnoreCase("KEY")) {
 
                        for (k = j; s[k + 2] != null; k++) {
                            if (k == j) {
                                s[k - 1] = s[k - 1] + "*";
                                s[k] = s[k + 2];
                            }
                            else {
                                s[k] = s[k + 2];
                            }
                        }
                        s[k] = null;
                        s[k + 1] = null;
                    }
                }

                //check for valid
                Matcher m = p.matcher(s[2]);
                if (m.find()) {
                    System.out.println("指令中包含非法字元\n");
                    break;
                }
                else {
                    for (j = 0; s[j] != null; j++) {
                        System.out.println(s[j]);
                    }
                    //Syntax c.correct, action.
                    try {

                        ListMultimap<String, String> newmap = ArrayListMultimap.create();
                        MyLinkedMap<String, String> tables = new MyLinkedMap<>(); // name and alias
                        int aggrenum = 0;
                       
                        ListMultimap<String, String> SelAtr = ArrayListMultimap.create(); // index,  <selected attributes, table>
                        List<String> coltitle = new ArrayList<String>();

                        MyLinkedMap<String, Integer> syntax = new MyLinkedMap<>();

                        //indexes of syntax
                        int from = 0; //index of FROM
                        int where = 0;
                        int[] as = new int[10]; //The system must allow SQL SELECT from up to 10 attributes in a table.
                        int as_n = 0;
                        boolean innerJoin = false;
                        String key = null;
                        boolean ambuguous = false;

                        c.aggre_b = false;

                        if (s[0].equalsIgnoreCase("SELECT")) {

                            //map:syntax (FROM, index in s[]) (Se Fr As Wh)
                            for (i = 0; s[i] != null; i++) {
                                if (s[i].equalsIgnoreCase("FROM")) {
                                    from = i;
                                    syntax.put("FROM", from);
                                }
                                else if (s[i].equalsIgnoreCase("WHERE")) {
                                    where = i;
                                    syntax.put("WHERE", where);
                                }
                                else if (s[i].equalsIgnoreCase("AS")) {
                                    as[as_n] = i;
                                    syntax.put("AS", as[as_n]);
                                    as_n++;
                                }
                            }

                            for (i = 1; i < from; i++) { //select
                                if (!s[i].contains(".") && (where - from) > 1) {
                                    System.out.println(map.get(s[from + 1]).get("attribute").size());
                                    for (j = 0; j < map.get(s[from + 1]).get("attribute").size(); j++) {
                                        if (map.get(s[from + 1]).get("attribute").get(j).contains(s[i])) {
                                            for (k = 0; k < map.get(s[from + 2]).get("attribute").size(); k++) {
                                                if (map.get(s[from + 2]).get("attribute").get(k).contains(s[i])){
                                                    System.out.println("Ambuguous Input!!");
                                                    ambuguous = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(ambuguous){
                                break;
                            }

                            //map:tables (realname, alias) (Se Fr '*' As '*' Wh)
                            if (where != 0) {
                                for (i = from + 1; i < syntax.getValue(1); i++) {
                                    if (s[i + 1].equalsIgnoreCase("AS")) {
                                        tables.put(s[i], s[i + 2]);
                                        i = i + 2;
                                    }
                                    else if (!s[i - 1].equalsIgnoreCase("AS") && !s[i + 1].equalsIgnoreCase("AS")) {
                                        tables.put(s[i], null);
                                    }
                                }
                            }
                            else {
                                if (s[from + 2] != null) {
                                    for (i = from + 1; s[i] != null; i++) {
                                        //System.out.println(i);
                                        if (s[i + 1].equalsIgnoreCase("AS")) {
                                            tables.put(s[i], s[i + 2]);
                                            i = i + 2;
                                        }
                                        else {
                                            tables.put(s[i], null);
                                        }
                                    }
                                }
                                else {
                                    tables.put(s[from + 1], null);
                                }
                            }
                            System.out.println(tables);

                            //SELECT aggregation function 似乎只有一個table 兩個table-->ex. SELECT a.friendid, SUM(b.number)
                            //SELECT COUNT(*) FROM Book; HERE!!!  沒有處理單獨的＊without"a."
                            //SELECT COUNT(*)FROM Author WHERE nationality = 'Taiwan';
                            //SELECT COUNT(editorial) FROM Book;
                            //SELECT SUM(pages) FROM Book WHERE authorId = 2;
                            //
                            //multimap:SelAtr (index, attribute, table-real-name) (Se '*' Fr * As * Wh) 
                            for (i = 1; i < from; i++) {//�@��check one attribute: [stuID] or [a.stuId] or [COUNT(stuID)] or [COUNT(a.stuID)]

                                String table1;
                                String attribute1;
                                //String[] atri = new String[2]; //store A.name as name and A

                                //aggregation bool
                                if (s[i].toUpperCase().contains("COUNT") || s[i].toUpperCase().contains("SUM")) {
                                    c.aggre = s[i].substring(0, s[i].indexOf("(")).toUpperCase();
                                    c.aggre_b = true;
                                }
                                System.out.println(c.aggre_b);
                                System.out.println("Im here1");

                                c.atri(s, i, tables, map);
                                System.out.println("Im here2");
                                table1 = c.table;
                                System.out.println("table1:" + table1);
                                attribute1 = c.attribute;
                                System.out.println("attribute1:" + attribute1);

                                if (attribute1.equals("*")) {

                                    for (k = 0; k < map.get(c.table).get("attribute").size(); k++) {
                                        SelAtr.put(c.table, map.get(c.table).get("attribute").get(k));
                                        String coll;
                                        if (c.pt_attribute.contains(".")) {
                                            int len_todot = c.pt_attribute.indexOf(".");
                                            coll = c.pt_attribute.substring(0, len_todot) + map.get(c.table).get("attribute").get(k);
                                        }
                                        else {
                                            coll = map.get(c.table).get("attribute").get(k);
                                        }
                                        coltitle.add(coll);
                                    }
                                }
                                else {
                                    for (j = 0; j < map.get(table1).get("attribute").size(); j++) {
                                        //System.out.println("000000000000" + map.get(table1).get("attribute").get(j));
                                        if (map.get(table1).get("attribute").get(j).contains(attribute1)) {

                                            SelAtr.put(table1, map.get(table1).get("attribute").get(j));
                                            coltitle.add(attribute1);
                                        }
                                    }
                                }
                            }//end of [attributes --> SelAtr] for loop

                            System.out.println(SelAtr);
                            System.out.println(coltitle);
                            System.out.println("Im here1");

                            boolean compare = false;
                            // if there is no WHERE clause, set the c.correct
                            if (where == 0) {
                                System.out.println("im here in where");
                                String atriTemp;
                                String table_0 = tables.getKey(0);

                                atriTemp = map.get(table_0).get("attribute").get(0);
                                for (i = 0; i < map.get(table_0).get(atriTemp).size(); i++) {
                                    c.correct.add(i);
                                }
                                if (tables.getKey(1) != null) {
                                    String table_1 = tables.getKey(1);
                                    atriTemp = map.get(table_1).get("attribute").get(0);
                                    for (i = 0; i < map.get(table_1).get(atriTemp).size(); i++) {
                                        c.correct2.add(i);
                                    }
                                }
                            }
                            else if (where != 0) {//if where is written

                                boolean andor = false;
                                boolean and = false;
                                boolean or = false;

                                int mapcount;

                                for (i = where + 1; s[i] != null; i = i + 4) { //i=s[], start from where
                                    System.out.println("--------------------" + i);
                                    /*1*/
                                    String table1, attribute1; //---> atri[0](attri)  atri[1](table-real name)
                                    /*2*/
                                    String opt;
                                    /*3*/
                                    String third;
                                    /*4*/ //bool : AND
                                    if (s[i + 3] != null) {
                                        if (s[i + 3].equalsIgnoreCase("AND")) {
                                            and = true;
                                            andor = true;
                                        }
                                        else if (s[i + 3].equalsIgnoreCase("OR")) {
                                            or = true;
                                            andor = true;
                                        }
                                    }
                                    System.out.println("Im here 2");

                                    c.atri(s, i, tables, map);
                                    table1 = c.table;
                                    attribute1 = c.attribute;
                                    System.out.println("table1:" + table1);
                                    System.out.println("attribute1:" + attribute1);

                                    /*2*/ //opt
                                    opt = s[i + 1];
                                    System.out.println("opt:" + opt);

                                    /*3*/ //third
                                    third = s[i + 2];
                                    System.out.println("third:" + third);

                                    for (mapcount = 0; mapcount < map.get(table1).get("attribute").size(); mapcount++) {
                                        if (map.get(table1).get("attribute").get(mapcount).contains(attribute1)) {
                                            System.out.println(map.get(table1).get("attribute").get(mapcount));
                                            break;
                                        }
                                    }
                                    int at_size = map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).size(); // the size of the selected attribute (compared to the number)

                                    //System.out.println("at_size:" + at_size);
                                    /*case 1*/ //+num 
                                    if (StringUtils.isNumeric(third)) {

                                        System.out.println("im here 3" + map.get(table1).get("attribute").get(mapcount));

                                        int num = Integer.valueOf(third); // the number

                                        switch (opt) {
                                            case ">":
                                                //System.out.println(">>>>>>>");
                                                for (j = 0; j < at_size; j++) {
                                                    if (Integer.valueOf(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j)) > num) {
                                                        c.COutput(map, table1, j, tables, compare);
                                                    }
                                                }
                                                break;
                                            case "<":
                                                for (j = 0; j < at_size; j++) {
                                                    if (Integer.valueOf(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j)) < num) {
                                                        c.COutput(map, table1, j, tables, compare);
                                                    }
                                                }
                                                break;
                                            case "=":
                                                for (j = 0; j < at_size; j++) {
                                                    //System.out.println(j);
                                                    if (Integer.valueOf(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j)) == num) {
                                                        //System.out.println(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j));
                                                        c.COutput(map, table1, j, tables, compare);
                                                    }
                                                }
                                                break;
                                            case "<>":
                                                for (j = 0; j < at_size; j++) {
                                                    if (Integer.valueOf(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j)) != num) {
                                                        c.COutput(map, table1, j, tables, compare);
                                                    }
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                    }// end if third = integer

                                    /*case 2*/ //+string 
                                    else if (!third.contains(".") && !StringUtils.isNumeric(third)) {

                                        System.out.println("In case 2----------------");

                                        third = third.substring(1, third.length() - 1);
                                        System.out.println("third:" + third);
                                        if (opt.equals("<>")) {
                                            for (j = 0; j < at_size; j++) {
                                                if (!map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j).equals(third)) {
                                                    c.COutput(map, table1, j, tables, compare);
                                                }
                                            }
                                        }
                                        else if (opt.equals("=")) {

                                            for (j = 0; j < map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).size(); j++) {
                                                System.out.println(map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j));
                                                if (map.get(table1).get(map.get(table1).get("attribute").get(mapcount)).get(j).equals(third)) {

                                                    c.COutput(map, table1, j, tables, compare);
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        /*case 3*/ //inner join a.studentID = b.studentID 
                                        //String table2, attribute2;
                                        //process third dot
                                        c.atri(s, i, tables, map);

                                        innerJoin = true;
                                        key = s[i].substring(s[i].indexOf(".") + 1, s[i].length());
                                        System.out.println("XXXXXXXXXXXXXXX:" + key);

                                    }

                                    System.out.println(c.correct);
                                    System.out.println(c.correct2);

                                    if (compare) {
                                        if (and && !innerJoin) {
                                            c.correct.retainAll(c.correct2);
                                            Set<Integer> hs = new HashSet<>();
                                            hs.addAll(c.correct);
                                            c.correct.clear();
                                            c.correct.addAll(hs);

                                        }
                                        else if (or && !innerJoin) {
                                            c.correct.addAll(c.correct2);
                                            Set<Integer> hs = new HashSet<>();
                                            hs.addAll(c.correct);
                                            c.correct.clear();
                                            c.correct.addAll(hs);
                                        }
                                    }
                                    System.out.println(c.correct);

                                    if (andor) {
                                        compare = true;
                                    }
                                    else {
                                        compare = false;
                                    }
                                    System.out.println("AND:" + and);
                                    System.out.println("OR:" + or);
                                    System.out.println("andor:" + andor);
                                    System.out.println("compare:" + compare);

                                }

                                if (c.aggre_b == true) {
                                    if (c.aggre.equalsIgnoreCase("COUNT")) {
                                        if (c.table.equals(tables.getKey(0))) {
                                            aggrenum = c.correct.size();
                                        }
                                        else {
                                            aggrenum = c.correct2.size();
                                        }
                                    }
                                    else if (c.aggre.equalsIgnoreCase("SUM")) {
                                        int value;
                                        List<Integer> cor;
                                        if (c.table.equals(tables.getKey(0))) {
                                            cor = c.correct;
                                        }
                                        else {
                                            cor = c.correct2;
                                        }
                                        for (i = 0; i < cor.size(); i++) {
                                            value = Integer.valueOf(map.get(c.table).get(c.attribute).get(cor.get(i)));
                                            aggrenum = aggrenum + value;
                                        }
                                    }
                                    else {
                                        System.out.println("Wrong aggregation function.");
                                    }
                                }
                            }

                            //print!!!
                            System.out.println("coltitle" + coltitle);
                            System.out.println("tables" + tables);
                            System.out.println("correct" + c.correct);
                            System.out.println("SelAtr" + SelAtr);
                            //System.out.println("table" + c.table);
                            System.out.println("-----------------------------------------------------------");
                            String tab;

                            if (innerJoin) {
                                String biggerTable, smalTable, temp1;
                                String attiTemp1 = null, attiTemp2 = null;
                                if (tables.getKey(0).equals(c.table)) {
                                    tab = tables.getKey(1);
                                }
                                else {
                                    tab = tables.getKey(0);
                                }

                                for (i = 0; i < map.get(c.table).get("attribute").size(); i++) {
                                    //System.out.println("key:" + map.get(c.table).get("attribute").get(i));
                                    if (map.get(c.table).get("attribute").get(i).contains(key)) {
                                        attiTemp1 = map.get(c.table).get("attribute").get(i);
                                        //System.out.println("key:" + attiTemp1);
                                        break;
                                    }
                                }
                                for (j = 0; j < map.get(tab).get("attribute").size(); j++) {
                                    //System.out.println("key:" + map.get(tab).get("attribute").get(l));
                                    if (map.get(tab).get("attribute").get(j).contains(key)) {
                                        attiTemp2 = map.get(tab).get("attribute").get(j);
                                        //System.out.println("key:" + attiTemp2);
                                        break;
                                    }
                                }
                                if (map.get(c.table).get(attiTemp1).size() > map.get(tab).get(attiTemp2).size()) {
                                    biggerTable = c.table;
                                    smalTable = tab;
                                }
                                else {
                                    temp1 = attiTemp2;
                                    attiTemp2 = attiTemp1;
                                    attiTemp1 = temp1;
                                    biggerTable = tab;
                                    smalTable = c.table;
                                }
                                /*
                                System.out.println(smalTable);
                                System.out.println("key:" + attiTemp2);
                                System.out.println(biggerTable);
                                System.out.println("key:" + attiTemp1);*/

 /*for (j = 0; j < c.correct2.size(); j++) {
                                    for (k = 0; k < map.get(tab).get(map.get(tab).get("attribute").get(l)).size(); k++) {
                                        //System.out.println(tab+":"+map.get(tab).get(key).get(k));
                                        //System.out.println(c.table+"r:"+map.get(c.table).get(map.get(c.table).get("attribute").get(i)).get(c.correct2.get(j)));
                                        if (map.get(tab).get(map.get(tab).get("attribute").get(l)).get(k).equals(map.get(c.table).get(map.get(c.table).get("attribute").get(i)).get(c.correct2.get(j)))) {
                                            c.correct.add(k);
                                        }
                                    }
                                }*/
                                for (l = 0; l < map.get(biggerTable).get(map.get(biggerTable).get("attribute").get(0)).size(); l++) {
                                    c.correct.add(l);
                                }
                                //System.out.println("******************");
                                String[] stringArray = Arrays.copyOf(SelAtr.keySet().toArray(), SelAtr.keySet().toArray().length, String[].class);

                                //System.out.println("SelAtr.size()" + SelAtr.size());
                                if (stringArray.length == 1) {
                                    if (!compare) {
                                        /*

                                        for (k = 0; k < c.correct.size(); k++) {
                                            System.out.println(k);
                                            for (String set : SelAtr.keySet()) {
                                                // System.out.println("Set:"+SelAtr.get(set).size());
                                                for (i = 0; i < SelAtr.get(set).size(); i++) {
                                                    //System.out.println(set);
                                                    System.out.format("%30s", map.get(set).get(SelAtr.get(set).get(i)).get(c.correct.get(k)));
                                                }
                                            }
                                            System.out.println();
                                        }*/
                                    }
                                    else {

                                        c.correct.clear();
                                        List<Integer> tempList = new ArrayList<>();
                                        //System.out.println("c.correct2.size():"+c.correct2.size());
                                        for (i = 0; i < map.get(biggerTable).get(attiTemp1).size(); i++) {
                                            for (j = 0; j < c.correct2.size(); j++) {

                                                if (map.get(biggerTable).get(attiTemp1).get(i).equals(map.get(smalTable).get(attiTemp2).get(c.correct2.get(j)))) {
                                                    c.correct.add(i);
                                                    tempList.add(c.correct2.get(j));
                                                    break;
                                                }
                                            }
                                        }

                                        for (k = 0; k < c.correct.size(); k++) {
                                            for (j = 0; j < SelAtr.get(biggerTable).size(); j++) {
                                                System.out.format("%30s", map.get(biggerTable).get(SelAtr.get(biggerTable).get(j)).get(c.correct.get(k)));
                                            }
                                            for (i = 0; i < SelAtr.get(smalTable).size(); i++) {
                                                System.out.format("%30s", map.get(smalTable).get(SelAtr.get(smalTable).get(i)).get(tempList.get(k)));
                                            }
                                            System.out.println();
                                        }
                                    }
                                }
                                else {

                                    if (!compare) {

                                        for (i = 0; i < c.correct.size(); i++) {
                                            for (j = 0; j < map.get(smalTable).get(attiTemp2).size(); j++) {
                                                if (map.get(biggerTable).get(attiTemp1).get(i).equals(map.get(smalTable).get(attiTemp2).get(c.correct.get(j)))) {
                                                    c.correct2.add(j);
                                                    break;
                                                }
                                                if (j == map.get(smalTable).get(attiTemp2).size() - 1) {
                                                    c.correct.remove(i);
                                                }
                                            }
                                        }
                                        System.out.println(c.correct);
                                        System.out.println(c.correct2);
                                        for (k = 0; k < c.correct.size(); k++) {
                                            for (j = 0; j < SelAtr.get(smalTable).size(); j++) {
                                                System.out.format("%30s", map.get(smalTable).get(SelAtr.get(smalTable).get(j)).get(c.correct2.get(k)));
                                            }
                                            for (i = 0; i < SelAtr.get(biggerTable).size(); i++) {
                                                System.out.format("%30s", map.get(biggerTable).get(SelAtr.get(biggerTable).get(i)).get(c.correct.get(k)));
                                            }
                                            System.out.println();
                                        }

                                    }
                                    else {
                                        if (c.table.equals(biggerTable)) {
                                            for (i = 0; i < c.correct2.size(); i++) {
                                                for (j = 0; j < map.get(smalTable).get(attiTemp2).size(); j++) {
                                                    if (map.get(smalTable).get(attiTemp2).get(j).equals(map.get(biggerTable).get(attiTemp1).get(c.correct2.get(i)))) {
                                                        c.correct.add(j);
                                                    }
                                                }
                                            }
                                            for (k = 0; k < c.correct2.size(); k++) {
                                                for (j = 0; j < SelAtr.get(smalTable).size(); j++) {
                                                    System.out.format("%30s", map.get(smalTable).get(SelAtr.get(smalTable).get(j)).get(c.correct.get(k)));
                                                }
                                                for (i = 0; i < SelAtr.get(biggerTable).size(); i++) {
                                                    System.out.format("%30s", map.get(biggerTable).get(SelAtr.get(biggerTable).get(i)).get(c.correct2.get(k)));
                                                }
                                                System.out.println();
                                            }

                                        }
                                        else {

                                            c.correct.clear();
                                            List<Integer> tempList = new ArrayList<>();
                                            //System.out.println("c.correct2.size():"+c.correct2.size());
                                            for (i = 0; i < map.get(biggerTable).get(attiTemp1).size(); i++) {
                                                for (j = 0; j < c.correct2.size(); j++) {
                                                    //System.out.println("1----"+map.get(biggerTable).get(attiTemp1).get(i));
                                                    //System.out.println("2----"+map.get(smalTable).get(attiTemp2).get(c.correct2.get(j)));

                                                    if (map.get(biggerTable).get(attiTemp1).get(i).equals(map.get(smalTable).get(attiTemp2).get(c.correct2.get(j)))) {
                                                        //System.out.println("**************************"+j);
                                                        //System.out.println("1----"+map.get(biggerTable).get(attiTemp1).get(i));
                                                        //System.out.println("2----"+map.get(smalTable).get(attiTemp2).get(c.correct2.get(j)));
                                                        c.correct.add(i);
                                                        tempList.add(c.correct2.get(j));
                                                        break;
                                                    }
                                                }
                                            }
                                            System.out.println(c.correct);
                                            System.out.println(tempList);

                                            for (k = 0; k < c.correct.size(); k++) {
                                                for (j = 0; j < SelAtr.get(biggerTable).size(); j++) {
                                                    System.out.format("%30s", map.get(biggerTable).get(SelAtr.get(biggerTable).get(j)).get(c.correct.get(k)));
                                                }
                                                for (i = 0; i < SelAtr.get(smalTable).size(); i++) {
                                                    System.out.format("%30s", map.get(smalTable).get(SelAtr.get(smalTable).get(i)).get(tempList.get(k)));
                                                }
                                                System.out.println();
                                            }
                                        }

                                    }
                                    /*for (k = 0; k < c.correct.size(); k++) {
                                        if (c.table.equals(stringArray[0])) {
                                            for (j = 0; j < SelAtr.get(stringArray[0]).size(); j++) {
                                                System.out.format("%30s", map.get(stringArray[0]).get(SelAtr.get(stringArray[0]).get(j)).get(c.correct2.get(k)));
                                            }
                                            for (i = 0; i < SelAtr.get(stringArray[1]).size(); i++) {
                                                System.out.format("%30s", map.get(stringArray[1]).get(SelAtr.get(stringArray[1]).get(i)).get(c.correct.get(k)));
                                            }
                                            System.out.println();
                                        }
                                        else if (c.table.equals(stringArray[1])) {
                                            for (j = 0; j < SelAtr.get(stringArray[0]).size(); j++) {
                                                System.out.format("%30s", map.get(stringArray[0]).get(SelAtr.get(stringArray[0]).get(j)).get(c.correct.get(k)));
                                            }
                                            for (i = 0; i < SelAtr.get(stringArray[1]).size(); i++) {
                                                System.out.format("%30s", map.get(stringArray[1]).get(SelAtr.get(stringArray[1]).get(i)).get(c.correct2.get(k)));
                                            }
                                            System.out.println();
                                        }
                                    }*/
                                }
                                //System.out.println(c.correct);
                            }
                            else if (!c.aggre_b && !innerJoin) {
                                if (SelAtr.get(tables.getKey(0)) != null) {
                                    //先印第一個table的東西 --> correct
                                    //String tab = tables.getKey(0);

                                    for (k = 0; k < c.correct.size(); k++) {
                                        for (String set : SelAtr.keySet()) {
                                            for (i = 0; i < SelAtr.get(set).size(); i++) {
                                                System.out.format("%30s", map.get(set).get(SelAtr.get(set).get(i)).get(c.correct.get(k)));
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                            }
                            else {
                                System.out.println(c.correct.size());
                            }

                            c.correct.clear();
                            c.correct2.clear();

                            //create table, insert values
                        }
                        else if (s[0].equalsIgnoreCase("CREATE") && s[1].equalsIgnoreCase("TABLE")) {

                            if (!map.containsKey(s[2])) {
                                map.put(s[2], CT.Create(s));
                                System.out.println("Table [" + s[2] + "] has been created successfully\n");
                            }
                            else {
                                System.out.println("Table [" + s[2] + "] has already existed\n");
                            }
                            break;
                        }
                        else if (s[0].equalsIgnoreCase("INSERT") && s[1].equalsIgnoreCase("INTO")) {

                            if (map.containsKey(s[2])) {

                                for (i = 0; i < map.get(s[2]).get("attribute").size(); i++) {
                                    if (map.get(s[2]).get("attribute").get(i).contains("*")) {
                                        InsertWithKey = true;
                                        //System.out.println("PR:"+InsertWithKey);
                                    }
                                }
                                if (!InsertWithKey) {

                                    if (s[3].equalsIgnoreCase("VALUES")) {
                                        int attri_count = map.get(s[2]).get("attribute").size();

                                        for (j = 4, k = 0; s[j] != null; j++, k++) {
                                            //System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                            //System.out.println("data: " + s[j]);
                                            if ((map.get(s[2]).get("attribute")).get(k).contains(")")) {
                                                int num = Integer.parseInt((map.get(s[2]).get("attribute")).get(k).substring((map.get(s[2]).get("attribute")).get(k).indexOf("(") + 1, (map.get(s[2]).get("attribute")).get(k).indexOf(")")));
                                                //System.out.println(num);
                                                if (s[j].length() > num) {
                                                    System.out.println(s[j] + " exceed varchar limit of " + num);
                                                    error = true;
                                                    break;
                                                }
                                            }
                                            else {
                                                if (!StringUtils.isNumeric(s[j])) {
                                                    System.out.println(s[j] + " not in int type");
                                                    error = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!error) {
                                            for (j = 4, k = 0; s[j] != null; j++, k++) {
                                                System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                System.out.println("data: " + s[j]);
                                                map.get(s[2]).put((map.get(s[2]).get("attribute")).get(k), s[j]);
                                            }

                                            for (k = 0; k < attri_count; k++) {
                                                System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                str = map.get(s[2]).get("attribute").get(k);
                                                System.out.println(map.get(s[2]).get(str));
                                            }
                                        }
                                    }
                                    else {
                                        int attri_count = map.get(s[2]).get("attribute").size();
                                        for (i = 0; s[i] != null; i++) {
                                            if (s[i].equalsIgnoreCase("VALUES")) {
                                                break;
                                            }
                                        }

                                        for (j = 3, k = i + 1; !s[j].equalsIgnoreCase("VALUES"); j++, k++) {
                                            //System.out.println("attribute: " + s[j]);
                                            //System.out.println("data: " + s[k]);
                                            for (l = 0; l < attri_count; l++) {
                                                str = map.get(s[2]).get("attribute").get(l);
                                                //System.out.println("str: " + str);
                                                if (str.contains(s[j])) {
                                                    break;
                                                }
                                            }
                                            if (str.contains(")")) {
                                                int num = Integer.parseInt(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
                                                //System.out.println(num);
                                                if (s[k].length() > num) {
                                                    System.out.println(s[k] + " exceed varchar limit of " + num);
                                                    error = true;
                                                    break;
                                                }
                                            }
                                            else {
                                                if (!StringUtils.isNumeric(s[k])) {
                                                    System.out.println(s[k] + " not in int type");
                                                    error = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!error) {
                                            for (j = 3, k = i + 1; !s[j].equalsIgnoreCase("VALUES"); j++, k++) {
                                                System.out.println("attribute: " + s[j]);
                                                System.out.println("data: " + s[k]);
                                                for (l = 0; l < attri_count; l++) {
                                                    str = map.get(s[2]).get("attribute").get(l);
                                                    //System.out.println("str: " + str);
                                                    if (str.contains(s[j])) {
                                                        break;
                                                    }
                                                }
                                                map.get(s[2]).put(str, s[k]);
                                            }

                                            for (k = 0; k < attri_count; k++) {
                                                System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                str = map.get(s[2]).get("attribute").get(k);
                                                System.out.println(map.get(s[2]).get(str));
                                            }
                                        }
                                    }
                                } // PR
                                else {
                                    if (s[3].equalsIgnoreCase("VALUES")) {

                                        int attri_count = map.get(s[2]).get("attribute").size();

                                        //find primary key
                                        for (i = 0; i < attri_count; i++) {
                                            if (map.get(s[2]).get("attribute").get(i).contains("*")) {
                                                pr = map.get(s[2]).get("attribute").get(i);   //primary key
                                                break;
                                            }
                                        }

                                        for (j = 0; j < map.get(s[2]).get(pr).size(); j++) {
                                            if (s[4 + i].equals(map.get(s[2]).get(pr).get(j))) {
                                                samekey = true;
                                                System.out.println("Error! Invalid key...");
                                                break;
                                            }
                                        }
                                        if (samekey == false) {
                                            for (j = 4, k = 0; s[j] != null; j++, k++) {
                                                //System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                //System.out.println("data: " + s[j]);
                                                if ((map.get(s[2]).get("attribute")).get(k).contains(")")) {
                                                    int num = Integer.parseInt((map.get(s[2]).get("attribute")).get(k).substring((map.get(s[2]).get("attribute")).get(k).indexOf("(") + 1, (map.get(s[2]).get("attribute")).get(k).indexOf(")")));
                                                    //System.out.println(num);
                                                    if (s[j].length() > num) {
                                                        System.out.println(s[j] + " exceed varchar limit of " + num);
                                                        error = true;
                                                        break;
                                                    }
                                                }
                                                else {
                                                    if (!StringUtils.isNumeric(s[j])) {
                                                        System.out.println(s[j] + " not in int type");
                                                        error = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!error) {
                                                for (j = 4, k = 0; s[j] != null; j++, k++) {
                                                    System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                    System.out.println("data: " + s[j]);
                                                    map.get(s[2]).put((map.get(s[2]).get("attribute")).get(k), s[j]);
                                                }

                                                for (k = 0; k < attri_count; k++) {
                                                    System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                    str = map.get(s[2]).get("attribute").get(k);
                                                    System.out.println(map.get(s[2]).get(str));
                                                }

                                            }
                                        }

                                    }
                                    else {
                                        int attri_count = map.get(s[2]).get("attribute").size();
                                        for (l = 0; s[l] != null; l++) {
                                            if (s[l].equalsIgnoreCase("VALUES")) {
                                                break;
                                            }
                                        }
                                        for (i = 0; i < attri_count; i++) {
                                            if (map.get(s[2]).get("attribute").get(i).contains("*")) {
                                                pr = map.get(s[2]).get("attribute").get(i);   //primary key
                                                break;
                                            }
                                        }

                                        for (j = 3; j < l; j++) {
                                            System.out.println(pr.substring(0, pr.length() - 1));
                                            System.out.println(s[j]);
                                            if (pr.contains(s[j])) {

                                                break;
                                            }
                                        }

                                        for (k = 0; k < map.get(s[2]).get(pr).size(); k++) {
                                            if (s[j - 2 + l].equals(map.get(s[2]).get(pr).get(k))) {
                                                samekey = true;
                                                System.out.println("Error! Invalid key...");
                                                break;
                                            }
                                        }
                                        if (samekey == false) {

                                            for (j = 3, k = l + 1; !s[j].equalsIgnoreCase("VALUES"); j++, k++) {
                                                ///System.out.println("attribute: " + s[j]);
                                                //System.out.println("data: " + s[k]);
                                                for (int n = 0; n < attri_count; n++) {
                                                    str = map.get(s[2]).get("attribute").get(n);
                                                    //System.out.println("str: " + str);
                                                    if (str.contains(s[j])) {
                                                        break;
                                                    }
                                                }
                                                if (str.contains(")")) {
                                                    int num = Integer.parseInt(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
                                                    //System.out.println(num);
                                                    if (s[k].length() > num) {
                                                        System.out.println(s[k] + " exceed varchar limit of " + num);
                                                        error = true;
                                                        break;
                                                    }
                                                }
                                                else {
                                                    if (!StringUtils.isNumeric(s[k])) {
                                                        System.out.println(s[k] + " not in int type");
                                                        error = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!error) {
                                                for (j = 3, k = l + 1; !s[j].equalsIgnoreCase("VALUES"); j++, k++) {
                                                    System.out.println("attribute: " + s[j]);
                                                    System.out.println("data: " + s[k]);
                                                    for (int n = 0; n < attri_count; n++) {
                                                        str = map.get(s[2]).get("attribute").get(n);
                                                        //System.out.println("str: " + str);
                                                        if (str.contains(s[j])) {
                                                            break;
                                                        }
                                                    }
                                                    map.get(s[2]).put(str, s[k]);
                                                }
                                                for (k = 0; k < attri_count; k++) {
                                                    System.out.println("attribute: " + (map.get(s[2]).get("attribute")).get(k));
                                                    str = map.get(s[2]).get("attribute").get(k);
                                                    System.out.println(map.get(s[2]).get(str));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                System.out.println("No such table");
                            }
                        }

                        break;

                    }
                    catch (Exception e) {
                        System.out.println(e.getClass().getCanonicalName());
                        e.printStackTrace();
                        System.out.println("SQL語法錯誤");
                        break;
                    }
                }
            }
        }
    }
}
