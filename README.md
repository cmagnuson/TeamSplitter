# Team Splitter
Takes a CSV file and selected column, and outputs new files split on unique values in that column.  So the 1st unique value will go into file 1, 2nd file 2, etc.  This way you can for example split a file on team name, and get one file with the first instance of each team name, second file with second instance, etc.

Blank values are not split - they will all go into the first file.