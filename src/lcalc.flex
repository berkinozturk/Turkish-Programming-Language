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
Comment   = "***" [^*] ~"***"

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

    "çağır"    { return symbol(sym.CAGIR); }

    "içindeki"        { return symbol(sym.INSIDE); }
    "her"             { return symbol(sym.EVERY); }
    "değeri"          { return symbol(sym.VALUE); }
    "için"            { return symbol(sym.FOR); }
    "yaz"             { return symbol(sym.YAZ); }

    "eleman"                { return symbol(sym.VARIABLE); }
    "listesine"             { return symbol(sym.TOLIST); }
    "listesindeki"          { return symbol(sym.LISTGET); }
    "listesinin"            { return symbol(sym.LISTUPDATE);}
    "ekle"                  { return symbol(sym.LISTADD); }
    "elemanı"               { return symbol(sym.LISTDELETEVARIABLE); }
    "sil"                   { return symbol(sym.LISTREMOVE); }
    "listesinin uzunluğu"   { return symbol(sym.LISTLENGTH); }
    "listesini temizle"     { return symbol(sym.LISTCLEAR); }
    "elemanının sırası"     { return symbol(sym.LISTINDEX); }

    "readint"         { return symbol(sym.READINT); }
    "readstr"         { return symbol(sym.READSTR); }
    "length"          { return symbol(sym.LENGTH); }
    "position"        { return symbol(sym.POSITION); }
    "concatenate"     { return symbol(sym.CONCATENATE); }
    "substring"       { return symbol(sym.SUBSTRING); }
    "atla"            { return symbol(sym.CONTINUE); }
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

    "eşit"              { return symbol(sym.STREQ); }
    "eşit değil"        { return symbol(sym.STRNOTEQ); }

    "."                { return symbol(sym.DOT); }
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
    {Comment}                      { /* ignore */ }
    {WhiteSpace}       { /* do nothing */ }
    <<EOF>> { return symbol(sym.EOF); }
}


/* error */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }