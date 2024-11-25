

有些特殊字符，比如：%作为like语句中的参数时，要对其进行转义处理。
使用sqlMap等代码检测工具，它能检测sql注入漏洞。

## 数据库 SQL 权限

正常情况下，我们需要控制线上账号的权限，只允许DML（data manipulation language）数据操纵语言语句，包括：select、update、insert、delete等。

不允许DDL（data definition language）数据库定义语言语句，包含：create、alter、drop等。也不允许DCL（Data Control Language）数据库控制语言语句，包含：grant,deny,revoke等。

DDL和DCL语句只有dba的管理员账号才能操作。

顺便提一句：如果被删表或删库了，其实还有补救措施，就是从备份文件中恢复，可能只会丢失少量实时的数据，所以一定有备份机制。