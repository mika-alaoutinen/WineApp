# WineParser notes

WineParser is a quick and dirty program that parses the text files in `resources/texts` and turns them into `Wine` and
`Review` entities.

## Discovered bugs

- The text files should end with two blank lines, otherwise the last entry is not parsed.

## Things to improve

- Review parsing is dodgy and probably has bugs.
