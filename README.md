# miniDBMS

A miniDBMS that support some SQL comment: 
  
  for example:
  1. CREATE TABLE table1 (
      attribute1 int PRIMARY KEY,
      attribute2 varchar(20),
      attribute3 varchar(40),
      attribute4 int
     );
     
  2. INSERT INTO table1 (attribute1, attribute2, attribute3) VALUES (0, 'abcd', '123456789', 1);
    or INSERT INTO table1 VALUES (0, 'abcd', '123456789, 1);
    
  3. SELECT query including join operation
    ex. SELECT *
        FROM table1, table2
        WHERE atattribute4 < 5 AND table1.atattribute1 = table2.atattribute4;
     and also support COUNT, SUM operation.
     
  4. Applying index to certain attribute:
    ex. CREATE INDEX indexname on table1 (attribute1) using hashing;  
    or CREATE INDEX indexname on table1 (attribute1) using btree;  
   
