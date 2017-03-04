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
RE r = RE.compile (...);
Matcher m = r.matcher (...);
if (m.wmatch()) ...
```

Compiling and installing: mvn install

Making documentation: mvn javadoc:javadoc


Maven dependency:
```xml
    <dependency>
      <groupId>peltomaa.javatre</groupId>
      <artifactId>javatre</artifactId>
      <version>0.0.1</version>
    </dependency>
```

 
Copyright (©) 2017 Hannu Väisänen

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
