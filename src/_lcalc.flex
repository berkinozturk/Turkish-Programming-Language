import java_cup.runtime.*;
      
%%

%class Lexer

%line
%column

%cup

%{  
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

NUM = [0-9]+
IDENT = [A-Za-z_][A-Za-z_0-9]*
STRING = \"([^\\\"]|\\.)*\"

%%
   
<YYINITIAL> {

    /** Keywords. */
    "ve"              { return symbol(sym.AND); }
    "veya"            { return symbol(sym.OR); }
    "not"             { return symbol(sym.NOT); }
    "doğru"           { return symbol(sym.TRUE); }
    "yanlış"          { return symbol(sym.FALSE); }

    "başla"           { return symbol(sym.BEGIN); }
    "bitir"           { return symbol(sym.END); }
    "exit"            { return symbol(sym.EXIT); }


    "eğer"            { return symbol(sym.EGER); }
    "ise"             { return symbol(sym.ISE); }
    "ya da"           { return symbol(sym.YADA); }
    "değil"           { return symbol(sym.DEGIL); }
    "olduğu sürece"   { return symbol(sym.WHILE); }
    "liste"           {return symbol((sym.LIST)); }
    "değişkeni"       { return symbol(sym.DEGIS); }
    "ile"             { return symbol(sym.ILE); }
    "arasında"        { return symbol(sym.ARASINDA); }
    "artarken"        { return symbol(sym.ART); }
    "azalırken"       { return symbol(sym.AZ); }
    "işi"             { return symbol(sym.FUNCTION); }
    "döndür"          { return symbol(sym.RETURN); }

    "içindeki"        { return symbol(sym.INSIDE); }
    "her"             { return symbol(sym.EVERY); }
    "değeri"          { return symbol(sym.VALUE); }
    "için"            { return symbol(sym.FOR); }

    "yaz"             { return symbol(sym.YAZ); }
    "liste"           { return symbol(sym.LISTE); }
    "readint"         { return symbol(sym.READINT); }
    "length"          { return symbol(sym.LENGTH); }
    "position"        { return symbol(sym.POSITION); }
    "readstr"         { return symbol(sym.READSTR); }
    "concatenate"     { return symbol(sym.CONCATENATE); }
    "substring"       { return symbol(sym.SUBSTRING); }
    "devam et"        { return symbol(sym.CONTINUE); }
    "kır"             { return symbol(sym.BREAK); }

    "="               {return symbol(sym.ASSIGN); }
    "=="              { return symbol(sym.EQ); }
    "<"               { return symbol(sym.LT); }
    "<="              { return symbol(sym.LE); }
    ">"               { return symbol(sym.GT); }
    ">="              { return symbol(sym.GE); }
    "!="              { return symbol(sym.NE); }
    "{"               { return symbol(sym.LCURLY); }
    "}"               { return symbol(sym.RCURLY); }


    "sonra"           { return symbol(sym.SONRA); }
    "eşit"            { return symbol(sym.STREQ); }
    "eşit değil"      { return symbol(sym.STRNOTEQ); }

    ";"                { return symbol(sym.SEMI); }
    ","                { return symbol(sym.COMMA); }
    "("                { return symbol(sym.LPAREN); }
    ")"                { return symbol(sym.RPAREN); }
    "+"                { return symbol(sym.PLUS); }
    "-"                { return symbol(sym.MINUS); }
    "*"                { return symbol(sym.TIMES); }
    "bölümünden kalan" { return symbol(sym.MODE);  }
    "/"                { return symbol(sym.DIVIDE); }

    {NUM}            { return symbol(sym.NUM, new Integer(yytext())); }
    {IDENT}          { return symbol(sym.IDENT, new String(yytext()));}
    {STRING}         { return symbol(sym.STRING, new String(yytext())); }

    {WhiteSpace}       { /* do nothing */ }
    <<EOF>> { return symbol(sym.EOF); }
}


/* error */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }