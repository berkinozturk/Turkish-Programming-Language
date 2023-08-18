<div align="center">
  <img src="https://github.com/berkinozturk/Turkish-Programming-Language/assets/94238138/2730d42e-a78e-4d12-aa2c-b8e6c2a5d99b" alt="Unknown" width="600" height="300">
</div>


# Sigun Programming Language

Unlike existing programming languages, Sigun programming language is specifically designed for native Turkish speakers, with the goal of not only translating English syntax into Turkish but also embodying the naturalness and advantage of coding in one’s own language. Our project aims to create a unique syntax structure for Turkish programming languages by emphasizing the naturalness of the language, and to surpass similar projects.To accomplish this, we are using a combination of LALR parsing and syntax-directed translation methodologies. </br>

## Project Files
We created a basic language for executing simple instructions. There are four files that we need for this purpose. Jflex file for lexer, CUP file for the parser, sigun file, and main java file for executing the input code.

* JFlex is a lexical analyzer generator for Java, written in Java. It takes an input as a specification with a set of regular expressions and corresponding actions. It generates a program (a lexer) that reads input, matches the input against the regular expressions in the specific file, and runs the corresponding action if the regular expression matches.

* The CUP stands for Construction of Useful Parsers and is an LALR parser generator for Java. It implements standard LALR parser generation. As a parser author, you specify the symbols of your grammar.

* We have a sigun file where the user writes their code in Sigun Programming Language. In order to parse that file we need to first compile our Jflex and CUP files.

* In the main java file, we specify the corresponding actions. 

## Simple Syntax

Basic Types:
```
* Identifier ::= [A-Za-z_][A-Za-z_0-9]*;
* Number     ::= [0-9]+;
* String     ::= "\"" ([^\"] | "\\\"")* "\"";
* Comment    ::= "***" "***"
``` 
Number Operators:
``` 
*Num_op ::= “+” | “-“
| “*”
| “/”
| “bölümünden kalan”
;
``` 
Starting Rules:
```
*Program ::= instr:i
*instr ::= instr:i ";" Simple_instr:si
           | Simple_instr:si ;
```
Expressions:
```
*Expr ::= IDENT:i
| Num_expr:e
| Str_expr:e
| List_stat:e
| List_get:e
| List_length:e
| List_index:e
| Random_expr:e
| Call_return_function:e
;
```
```
*Num_expr ::= NUM:e
| “sayıyı oku”
| “-“ Expr:e
| “(“ Expr:e “)”
| "uzunluğunu al" “(” Expr:e “)”
| "pozisyon" “(” Expr:s “,” Expr:s2 “)” | Expr:e Num_op:o Expr:e2
;
```
```
*Str_expr ::= STRING:s
| "kelimeyi oku"
| "birleştir" “(” Expr:s “,” Expr:s2 “)”
| "aralığını göster" “(” Expr:s “,” Expr:pos “,” Expr:length “)”
;
```
```
*List_stat ::= "liste" “(” List_content:e “)”
| "liste" “(” “)”
;
```
```
*List_content ::= List_content:a “,” Expr:e
| Expr:e
;
```
```
*List_get ::= Expr:e "listesindeki" Expr:e1 "." "eleman"
;
```
```
*List_length ::= Expr:e "listesinin uzunluğu"
;
```
```
*List_index ::= Expr:e "listesindeki" Expr:e1 "elemanının sırası" ;
```
```
*Random_expr ::= "rastgele" “(” Expr:e “,” Expr:e1 “)” ;
```
```
*Call_return_function ::= "çağır" Recursive_id:e "ile" IDENT:i ;
```
```
*Bool_expr ::= "doğru" | "yanlış"
| "(" Bool_expr:e ")"
| Expr_or_bool:e "değil”
| Bool_expr:e "ve" Bool_expr:e2
| Bool_expr:e "veya" Bool_expr:e2
| expr: e Num_rel:o Expr_or_bool:e2 | expr:s Str_rel:o Expr_or_bool:s2
;
```
```
Expr_or_bool ::= Expr:e
                 | Bool_expr:e ;
```
Simple Instructions:
```
*Simple_instr
::= Assign_stat:si
| If_stat:si
| Break_stat:si
| Continue_stat:si | While_stat:si
| For_stat:si
| Foreach_stat:si | Function_stat:si | Call_function:si
| "başla" instr:si ";" "bitir" | Output_stat:si
| List_add:si
| List_remove:si
| List_clear:si
| List_update:si | “çıkış yap"
;
```
```
*Assign_stat ::= IDENT:i "=" Expr:e
| IDENT:i "=" Bool_expr:e
;
```
```
*If_stat ::= "eğer" Expr_or_bool:c "ise" "{" Recursive:s ";" "}"
| "eğer" Expr_or_bool:c "ise" "{" Recursive:s ";" "}" "değil" "ise" "{" Recursive:s2 ";" "}"
| "eğer" Expr_or_bool:c "ise" "{" Recursive:s ";" "}" If_content:cont
| "eğer" Expr_or_bool:c "ise" "{" Recursive:s ";" "}" If_content:cont "değil" "ise" "{" Recursive:s3 ";" "}"
;
```
```
*If_content ::= If_content:cont "ya da" Expr_or_bool:c2 "ise" "{" Recursive:s2 ";" "}"
| "ya da" Expr_or_bool:c2 "ise" "{" Recursive:s2 ";" "}" ;
```
```
*Recursive ::= Recursive:i ";" Simple_instr:si | Simple_instr:si
;
```
```
*Break_stat ::= "kır" ;
```
```
*Continue_stat ::= "atla" ;
```
```
*While_stat ::= Expr_or_bool:c "olduğu sürece" "{" Recursive:s ";" "}"
;
```
```
*For_stat ::= IDENT:i "değişkeni" Expr:e "ile" Expr:e2 "arasında" Expr:e3 "artarken" "{" Recursive:s ";" "}"
              | IDENT:i "değişkeni" Expr:e "ile" Expr:e2 "arasında" Expr:e3 "azalırken" "{" Recursive:s ";" "}" ;
```
```
*Foreach_stat ::= Expr:e "içindeki" "her" IDENT:i "değeri" "için" "{"Recursive:s ";" "}" ;
```
```
*Function_stat ::= Recursive_id:i "ile" IDENT:e "işi" "{" Recursive:si ";" "}" 
                   | Recursive_id:i "ile" IDENT:e "işi" "{" Recursive:si ";" Return_function:r "}" ;
```
```
*Recursive_id ::= Recursive_id:a “,” Expr:e 
                  | Expr:e;
```
```
*Return_function ::= Expr:e "döndür" ";"
;
```
```
*Call_function ::= Recursive_id:e "ile" IDENT:i
;
```
```
*Output_stat ::= "yaz" "(" expr:e ")"
;
```
```
*List_add ::= Expr:e "listesine" Expr:e1 "ekle"
;
```
```
*List_remove ::= Expr:e "listesindeki" Expr:e1 "." "elemanı" "sil” ;
```
```
*List_clear ::= Expr:e "listesini temizle" ;
```
```
*List_update ::= Expr:e "listesinin" Expr:e1 "." "elemanı" "=" Expr: e2 ;
```
```
*List_update ::= Expr:e "listesinin" Expr:e1 "." "elemanı" "=" Expr: e2 ;
```
Assignment Operators:
```
*Num_rel
::= "==" 
| "<"
| "<=" 
| ">"
| ">=" 
| "!="
;
```
```
*Str_rel ::= "eşit"
| "eşit değil" ;
```


#### For detailed information about the Sigun Programming Language you can visit the <a href="https://github.com/berkinozturk/Turkish-Programming-Language/wiki">wiki page</a>.

















