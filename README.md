JavaTRE
=======

JavaTRE is Java bindig for approximate (fuzzy) regular expression
library [TRE](http://laurikari.net/tre/).

[Source code](https://github.com/laurikari/tre/)

In addition, this package has few classes to make using TRE more
Java-like, BUT the classes are not compatible with Java regular
expressions (package java.util.regex).

More information is in javadocs.


Example:
```java
TRE.regaparams_t.ByValue p = new AparamsBuilder().cost_ins(1).cost_del(1).cost_subst(1)
                     .max_cost(1).max_ins(1).max_del(1).max_subst(1).max_err(1).build();
String s = "To be or not to be or bee.";
RE re = RE.compile ("be", REG_EXTENDED, p);
Matcher m = re.matcher (s);
StringBuffer sb = new StringBuffer();
while (m.find()) {
  m.appendReplacement (sb, "X");
}
m.appendTail (sb);
```

Compiling and installing:  mvn install

Making documentation: mvn javadoc:javadoc


Maven dependency:
```xml
    <dependency>
      <groupId>peltomaa.javatre</groupId>
      <artifactId>javatre</artifactId>
      <version>0.0.1</version>
    </dependency>
```

 
Copyright (©) 2017-2018 Hannu Väisänen

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
