# config-tables

A set of artifacts to maintain pipe-separated tables. Yet another attempt to store relational data in codebase in human readable and git-frienly format.
## common
Lexer + standalone parser (TBD) for `.table` files

TODO: 
- Inner references for long cell values
- Multiple tables in one file

## tables-intellij-plugin
![image](https://github.com/Salauyou/config-tables/assets/7059765/3047ee31-90d4-4d4d-976c-3153609bc44b)

Intellij language plugin supporting features:
- Autoformatting: when `|` is entered, it is automatically adjusted
- Manual formatting: `Ctrl + Shift + L` adjusts all columns width
- Autocompletion
- Breadcrumbs
- Folding: rows are grouped by first column value
- Paste from clipboard: if clipboard text contains `\t`, it is treated as column separator
- `Copy As Table` context menu action: copy into clipboard in html/tab-separated format, useful for desktop editors (e.g. Excel)

TODO:
- `Copy as SQL Select` to generate SQL SELECT by header
- `Copy as SQL Insert` to generate SQL INSERT for selected rows

## config-tables-maven-plugin
Maven plugin to generate SQL scripts given set of tables and metadata (TBD)
