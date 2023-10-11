# config-tables

A set of artifacts to maintain pipe-separated tables. Yet another attempt to store relational data in codebase in human readable and git-frienly format.
## common
Lexer + standalone parser (TBD) for `.table` files

TODO: 
- Inner references for long cell values
- Multiple tables in one file

## tables-intellij-plugin
![image](https://github.com/Salauyou/config-tables/assets/7059765/0155210f-c413-44ca-ac2b-a7e8ad5c9a5b)

Intellij language plugin supporing features:
- Autoformatting
- Autocompletion
- Breadcrumbs
- Paste from clipboard recognizes `\t` as cell separator
- `Copy As Table` context menu action to copy into clipboard as html/tab-separated desktop-editor-friendly format

TODO:
- Better highlighting
- Highlight errors, e.g. if number of cells in a row exceeds header
- `Copy as SQL Select`—generates SQL select by header cells
- `Copy as SQL Insert`—generates SQL insert for selected rows or entire table

## config-tables-maven-plugin
Maven plugin to generate SQL scripts given set of tables and metadata (TBD)
