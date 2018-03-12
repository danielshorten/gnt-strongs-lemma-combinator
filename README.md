# Greek New Testament Strongs Lemma Combinator

This is some Java code to combine information from the [morphgnt](https://github.com/morphgnt/sblgnt) morphological tagging of the SBL Greek New Testament, and numbers from the [Strong's New Testament Dictionary](https://github.com/morphgnt/strongs-dictionary-xml) in order to tag words in the [SBL Greek New Testament OSIS XML](https://github.com/scott-fleischman/sblgnt-osis).

To build and run:
```
mvn package
java -jar target/<jar_file>
```

# License
Combinator code licensed under [CC-BY-SA License](https://creativecommons.org/licenses/by-sa/3.0/).

The SBLGNT text itself is subject to the [SBLGNT EULA](http://sblgnt.com/license/) and the morphological parsing and lemmatization is made available under a [CC-BY-SA License](https://creativecommons.org/licenses/by-sa/3.0/).

[Strong's dictionary](https://github.com/morphgnt/strongs-dictionary-xml) released under the [Creative Commons CC0 waiver](https://creativecommons.org/publicdomain/zero/1.0/).