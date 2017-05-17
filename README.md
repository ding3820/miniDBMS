# miniDBMS

A miniDBMS that support some SQL comment: 
  
  for example:
  1. CREATE TABLE table1 ( <br>
      attribute1 int PRIMARY KEY,<br>
      attribute2 varchar(20),<br>
      attribute3 varchar(40),<br>
      attribute4 int<br>
     );<br>
     <br>
  2. INSERT INTO table1 (attribute1, attribute2, attribute3) VALUES (0, 'abcd', '123456789', 1);<br>
    or INSERT INTO table1 VALUES (0, 'abcd', '123456789, 1);<br>
    <br>
  3. SELECT query including join operation<br>
    ex. SELECT *<br>
        FROM table1, table2<br>
        WHERE atattribute4 < 5 AND table1.atattribute1 = table2.atattribute4;<br>
     and also support COUNT, SUM operation.<br>
     <br>
  4. Applying index to certain attribute:<br>
    ex. CREATE INDEX indexname on table1 (attribute1) using hashing;  <br>
    or CREATE INDEX indexname on table1 (attribute1) using btree;  <br>
   
