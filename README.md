# Turkish Programming Language

Unlike existing programming languages, our language is specifically designed for native Turkish speakers, with the goal of not only translating English syntax into Turkish but also embodying the naturalness and advantage of coding in one’s own language. Our project aims to create a unique syntax structure for Turkish programming languages by emphasizing the naturalness of the language, and to surpass similar projects.To accomplish this, we are using a combination of LALR parsing and syntax-directed translation methodologies. </br>

## 1.1 The Grammar of the Language </br>
You can see our language structure that we created below. </br>
NOT: The project is in the development phase and changes may occur in the language structure in the following stages. </br>

## Assignment Operator

The simplest statement assignment operator in our language does not use an indicator for the different data types when initializing. </br>

a = 5 // in this statement a is an Integer,         </br>
a = ‘‘5’’ // in this statement is a String,         </br>

The assignment operator does not end with a verb, the cause being to shorten the sentences and keep the natural flow of the language. The Boolean values are also assign in this way without quotation marks by writing “doğru” if true and “yanlış” if false.

## Arithmetic Operations </br>

Most programming language’s arithmetic operations are written in symbols, and they are mostly universal. When someone write “&&”, it is known as an “and” operation. Since our language is mostly for children that are unaware of these established symbols, using words instead of symbols seemed much more understandable when learning from scratch. However, we did not change some operators since these operators are used in mathematical operations as well and easy to understand. These operators are same as in the most of other languages: </br>

=, <, >, <=, >=                                    </br>

These operators are modified to be more understandable in our language: </br>
bölümünden kalan // %                              </br>
veya // ||                                         </br>
ve // &&                                           </br>
eşit ise // ==                                     </br>
eşit değil // !=                                   </br>
doğru/yanlış // boolean(True/False)                </br>

## List </br>

For lists, we used the name “liste”, but when initializing a list just as the assignment operator, we do not specify it as a list. Our lists are able to store more than one type of data and it is dynamic just like python list. This way we are hoping to create a list more like a basket where every type can enter, pop, and be used in different variations of an algorithm. In the assignment below, “a” is a liste that includes three strings and two integers.  </br>

a = ("elma", "muz", "kiraz", 5, 7)                </br>

## Loop Through a List </br>

In order to reach elements of a list, we will use for loop. The usage of this is similar to loop lists in python. For a list called a, we will reach the elements of list in this way. </br>

a içindeki her i için                             </br>
yaz(i)                                            </br>

## If Else Statement </br>

The if in the if else statement represented as “Eğer”. This word will be written at the beginning of the statement since it does not disrupt Turkish’s grammar order and is casually used this way. Then we get the required condition and at the end write “ise” to start the body of the if statement. To add another if statement to the relevant upper one, the use of “ya da” is needed. The same process as after “Eğer” will be used here, and if required to end with an else statement, “değil ise” is used. </br>

eğer (condition) ise                             </br>
// if statement body                             </br>
ya da (condition) ise                            </br>
// if else statement body                        </br>
değil ise                                        </br>
// else statement body                           </br>

## For Statement </br>

A normal for statement needs three statements to execute:  </br>
The first statement is executed before the execution of the code block and assigns a value to a variable. </br>
The second statement defines the condition. </br>
The third and final statement is executed every time after the code block has been executed once. </br>
To adjust these to our language, in our language’s for statement, the variable that keeps the counter will be written first, and after that, the word “değişkeni” will be written to keep the order. Then the boundaries will be written and at the end the variable’s situation at the end of each iteration. This situation could either be “artarken”, meaning increasing, or ”azalırken”, meaning decreasing. </br>

i değişkeni 1 ile 10 arasında 3 artarken          </br>
// for statement body                             </br>

In this example, “i” is the changing variable after each loop. It is bound by 1 through 10, and it will increase by 3 after each iteration. </br>

##  While Statement </br>

In order to be compatible with the Turkish language structure, the condition part of the while statement comes first, then the statement. </br>

(...) olduğu sürece                              </br>
// while statement body                          </br>

##  Functions </br>

In our programming language, functions are called “işi”. The general grammatical rule for functions is as follows: </br>

[parameters][conjunction][function name]{        </br>
// function body }                               </br>

Here is the syntax for declaring a function in our programming language: </br>

a,b ile topla işi {                              </br>
sonuç = a + b                                    </br>
sonuç döndür }                                   </br>

To call this function: </br>

3,4 ile topla                                    </br>

## 1.2 How can I run the language? </br>
The programming language is developed using Java CUP, a parser generator that allows for high flexibility and customization. This approach enables us to create a language that is tailored to the needs of Turkish speakers and that can help them understand and work with programming concepts in a more natural and intuitive way. </br>
You can access the Jflex and CUP setup tutorial on macOS and Windows from the links below. </br>

https://cdn.discordapp.com/attachments/1002578665277771776/1085266348818575470/JFlex_and_Cup_Installation_for_Mac.pdf </br>
[Installation of Jflex and CUP for macOS](file:///C:/Users/BUSE%20OZEL/Desktop/bel2.pdf)   </br>
















