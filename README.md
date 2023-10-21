# config-tables

A set of artifacts to maintain pipe-separated tables. Yet another attempt to store relational data in codebase in human readable and git-frienly format.
## common
Lexer + standalone parser (TBD) for `.table` files

TODO: 
- Inner references for long cell values
- Multiple tables in one file

## tables-intellij-plugin
![image](https://github.com/Salauyou/config-tables/assets/7059765/0155210f-c413-44ca-ac2b-a7e8ad5c9a5b)

Intellij language plugin supporting features:
- Autoformatting: entered `|` is automatically adjusted by column with
- Quick formatting by `Ctrl+Shift+L`
- Autocompletion
- Breadcrumbs
- Paste from clipboard: if text contains `\t`, it is recognized as column separator
- `Copy As Table` context menu action: copy into clipboard in html/tab-separated format, useful for desktop editors (e.g. Excel)

TODO:
- Better highlighting
- Error highligting (e.g. exceessive cells in a row)
- `Copy as SQL Select` to generate SQL SELECT by header cells
- `Copy as SQL Insert` to generate SQL INSERT for rows

## config-tables-maven-plugin
Maven plugin to generate SQL scripts given set of tables and metadata (TBD)
