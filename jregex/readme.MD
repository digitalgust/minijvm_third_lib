What is JRegex?    
The JRegex is compact and efficient regular expression library for Java. It provides support for full perl5.6 regex syntax and is compliant with unicode.org's regex guidelines.    
Besides that, jregex supports named groups (existing in Python regexes but neither in perl nor in other java libs), and incomplete matching, which is hardly found elsewhere.    
Fast string search algorithms also are on the schedule.    
Feature list    
Perl5.6 regex syntax, including lookahead/lookbehind assertions.    
Compliancy with the level 1 of Unicode Regular Expression Guidelines.    
Thread safety.    
High performance.    
Replacing.    
Tokenizing.    
File search classes.    
Named groups.    
Incomplete matching.    
BSD license.    
How JRegex compares to other regex libs    
JRegex vs. java.util.regex    
(+) JRegex is a Free Software    
(+) JRegex is portable (runs under any version of java)    
(+) JRegex supports more features (named groups, Perl5.6's conditional statements and more)    
(-) java.util.regex implements 2'd level of Unicode support (JRegex is level 1)    
(-) JRegex is not a 'standard' library     
    
JRegex vs. jakarta-oro    
(+) JRegex supports more features (unicode, lookbehind assertions, conditional statements,named groups)    
(+) JRegex-enabled code is much less verbose    
(-) ??? (waiting for input...)     
    
The schedule outline    
Some utility methods: Pattern.matches(String), MatchResult.parseInt(int group), MatchIterator.toStringArray(int group), MatchIterator.toStringArrays(), v.1.1x - 1.2x    
Make the PathPattern to accept the "/*/" (any-directory) and "/**/" (any-path) patterns, v.1.2x    
Method Pattern.asPlainString(String), disregads special symbols, v.1.2x - 1.3x; suggest a better name for the method    
Implement fast string search algorithms (presumably BNDM for short and Apostolico-Giancarlo for long patterns), v.1.2x - 1.3x