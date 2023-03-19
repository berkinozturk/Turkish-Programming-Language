## Turkish Programming Language

Unlike existing programming languages, our language is specifically designed for native Turkish speakers, with the goal of not only translating English syntax into Turkish but also embodying the naturalness and advantage of coding in one’s own language. Our project aims to create a unique syntax structure for Turkish programming languages by emphasizing the naturalness of the language, and to surpass similar projects.To accomplish this, we are using a combination of LALR parsing and syntax-directed translation methodologies. </br>

You can see our language structure that we created below. </br>

# Assignment Operator

The simplest statement assignment operator in our language does not use an indicator for the different data types when initializing. </br>

a = 5 //in this statement a is an Integer, </br>
a = ‘‘5’’ //in this statement is a String, </br>

The assignment operator does not end with a verb, the cause being to shorten the sentences and keep the natural flow of the language. The Boolean values are also assign in this way without quotation marks by writing “doğru” if true and “yanlış” if false.

# Arithmetic Operations </br>

Most programming language’s arithmetic operations are written in symbols, and they are mostly universal. When someone write “&&”, it is known as an “and” operation. Since our language is mostly for children that are unaware of these established symbols, using words instead of symbols seemed much more understandable when learning from scratch. However, we did not change some operators since these operators are used in mathematical operations as well and easy to understand. These operators are same as in the most of other languages: </br>

=, <, >, <=, >= </br>

These operators are modified to be more understandable in our language: </br>
bölümünden kalan // %                              </br>
veya // ||                                         </br>
ve // &&                                           </br>
eşit ise // ==                                     </br>
eşit değil // !=                                   </br>
doğru/yanlış // boolean(True/False)                </br>

# List </br>







