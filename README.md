# Inverted Index Generator

This project is meant to be later built into a search engine. When this program is given a path argument and value, it accesses every text file in the path and those deeply within folders. It then creates an index showing all the stemmed words found in this path's text files, including the location the words were found at and the position within the file it was found. This information can then either be output to the console or written to a file in pretty JSON format.

This program is also capable of parsing a text file holding multiple queries, and then query the inverted index. Results are then created, which contain where a query's information is found, how many times it's found, and a score generated for the file's relevance to the query. This information can also be either be output to the console or written to a file in pretty JSON format. 


Flags associated with this program:

-path path: The flag "-path" indicates the next argument is a path to either a text file or a directory containing text 
files. This file(s) will then be parsed and have their information put into an inverted index.

-index path: The flag "-index" is an optional flag. If an argument is provided afer this, then it will be the path for the inverted index to be output to, otherwise index.json will be used as default.

-counts filepath: The flag "-counts" is an optional flag. If an argument is present after this, then it will be used for the counts results to be output to, otherwise counts.json will be used as default.
    
-query filepath: The flag "-query" should have an argument following it which contains a text file of lines to be queried. 

-exact: The flag "-exact" is optional. This flag means all queries should be exact word matches. If this flag is not present, then queries will be partial word matches.

-results: The flag "-results" is an optional flag. If there is an argument present after this, then it will be used for the results to be out put to, otherwise results.json will be used as default.
