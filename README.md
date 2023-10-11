# config-tables

A set of artifacts to maintain pipe-separated tables. Yet another attempt to store relational data in codebase in human-readable and git-frienly format.
## common
Parser for `.table` files (TBD)

TODO: 
- Inner references for long cell values
- Multiple tables in one file

## tables-intellij-plugin
![image](https://github.com/Salauyou/config-tables/assets/7059765/0155210f-c413-44ca-ac2b-a7e8ad5c9a5b)

Intellij plugin supporing features:
- Autoformatting
- Autocompletion
- Breadcrumbs
- Paste from clipboard recognizes `\t` as cell separator
- `Copy As Table` action to copy into clipboard as html/tab-separated desktop-editor-friendly format

TODO:
- Better highlighting
- Highlight errors, e.g. unmatching number of cells in a row

## config-tables-maven-plugin
Maven plugin to generate SQL scripts given set of tables and metadata (TBD)
